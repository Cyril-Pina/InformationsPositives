package com.pinalopes.informationspositives.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.application.IPApplication;
import com.pinalopes.informationspositives.cognitivetext.OnDownloadModelListener;
import com.pinalopes.informationspositives.cognitivetext.TextRecognition;
import com.pinalopes.informationspositives.utils.DateUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.FIRST_INDEX;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;
import static com.pinalopes.informationspositives.Constants.MODEL_FILE_NAME;
import static com.pinalopes.informationspositives.Constants.NB_DAYS_BEFORE_DOWNLOAD_MODEL;
import static com.pinalopes.informationspositives.Constants.SIZE_EMPTY_LIST;
import static com.pinalopes.informationspositives.application.IPApplication.localDB;

public class DataStorageHelper {

    private static final String TAG = "DataStorageHelper";

    private static SharedPreferences sharedPref;
    private static Gson gson;
    private static UserSettings userSettings;
    private static File modelFile;

    private DataStorageHelper() {
        throw new AssertionError();
    }

    public static void initDataStorage(Context context) {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        gson = new Gson();
        userSettings = getUserSettings(context);
    }

    // GETTER

    public static UserSettings getUserSettings() {
        return userSettings;
    }

    public static File getModelFile() {
        return modelFile;
    }

    // SETTINGS

    public static UserSettings getUserSettings(Context context) {
        boolean isNotificationsEnabled = sharedPref.getBoolean(context.getString(R.string.saved_notifications_state_key),
                false);
        int currentTheme = sharedPref.getInt(context.getString(R.string.saved_current_theme_key),
                R.style.AppTheme_NoActionBar);
        String selectedCategoriesJson = sharedPref.getString(context.getString(R.string.saved_selected_categories_key),
                "");
        if (selectedCategoriesJson != null) {
            Type type = new TypeToken<List<Integer>>(){}.getType();
            List<Integer> selectedCategories = gson.fromJson(selectedCategoriesJson, type);
            return new UserSettings.Builder()
                    .setCategoriesSelected(selectedCategories)
                    .setNotificationsActivated(isNotificationsEnabled)
                    .setCurrentTheme(currentTheme)
                    .build();
        } else {
            return new UserSettings.Builder()
                    .setNotificationsActivated(isNotificationsEnabled)
                    .setCurrentTheme(currentTheme)
                    .build();
        }
    }

    public static void updateSelectedCategories(Context context, int position, boolean isSelected) {
        userSettings.updateSelectedCategories(position, isSelected);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.saved_selected_categories_key),
                gson.toJson(userSettings.getCategoriesSelected()));
        editor.apply();
    }

    public static void updateNotificationsState(Context context, boolean isChecked) {
        userSettings.modifyNotificationsState(isChecked);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(R.string.saved_notifications_state_key),
                userSettings.isNotificationsEnabled());
        editor.apply();
    }

    public static void updateCurrentTheme(Context context) {
        userSettings.modifyCurrentTheme();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.saved_current_theme_key),
                userSettings.getCurrentTheme());
        editor.apply();
    }

    // MODEL

    public static void getSavedSentimentsModelFromDatabase(Context context, OnFetchModelResponseListener listener) {
        MutableLiveData<SentimentsModel> modelMutableLiveData = new MutableLiveData<>();
        modelMutableLiveData.observe((LifecycleOwner) context, model -> {
            if (model != null && !DateUtils.isDateLaterThan(NB_DAYS_BEFORE_DOWNLOAD_MODEL,
                    getLastModifiedDate(model.lastModifiedTime))) {
                modelSavedAndDateOK(new Gson().fromJson(model.model, File.class), listener);
            } else if (model != null && DateUtils.isDateLaterThan(NB_DAYS_BEFORE_DOWNLOAD_MODEL,
                    getLastModifiedDate(model.lastModifiedTime))) {
                modelSavedAndDateKO(model, listener);
            } else {
                modelUnsaved(listener);
            }
        });
        DataStorageHelper.getModel(modelMutableLiveData);
    }

    private static Date getLastModifiedDate(String timestamp) {
        try {
            return DateUtils.timestampToDate(timestamp);
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
        return new Date();
    }

    private static void modelSavedAndDateOK(File file, OnFetchModelResponseListener listener) {
        modelFile = file;
        listener.onModelResponseSucceed(true);
    }

    private static void modelSavedAndDateKO(SentimentsModel model, OnFetchModelResponseListener listener) {
        TextRecognition.downloadModel(MODEL_FILE_NAME, new OnDownloadModelListener() {
            @Override
            public void onDownloadSucceed(File downloadedFile) {
                model.model = gson.toJson(downloadedFile);
                model.lastModifiedTime = DateUtils.dateToTimestamp(new Date());
                updateModel(model);
                modelFile = downloadedFile;
                listener.onModelResponseSucceed(true);
                Log.i(TAG, "Model file downloaded.");
            }
            @Override
            public void onDownloadFailed(Exception exception) {
                Log.e(TAG, exception.toString());
                listener.onModelResponseSucceed(false);
            }
        });
    }

    private static void modelUnsaved(OnFetchModelResponseListener listener) {
        TextRecognition.downloadModel(MODEL_FILE_NAME, new OnDownloadModelListener() {
            @Override
            public void onDownloadSucceed(File downloadedFile) {
                String newModel = gson.toJson(downloadedFile);
                insertModel(createSentimentsModel(newModel));
                modelFile = downloadedFile;
                listener.onModelResponseSucceed(true);
                Log.i(TAG, "Model file downloaded.");
            }
            @Override
            public void onDownloadFailed(Exception exception) {
                Log.e(TAG, exception.toString());
                listener.onModelResponseSucceed(false);
            }
        });
    }

    private static SentimentsModel createSentimentsModel(String newModel) {
        SentimentsModel sentimentsModel = new SentimentsModel();
        sentimentsModel.model = newModel;
        sentimentsModel.lastModifiedTime = DateUtils.dateToTimestamp(new Date());
        return sentimentsModel;
    }

    // DATABASE OPERATION

    public static void saveRecentSearchInLocalDB(String recentArticleSearched) {
        RecentSearch recentSearch = new RecentSearch();
        recentSearch.articleSearched = recentArticleSearched;
        IPApplication.executorService.execute(() -> localDB.recentSearchesDao().insertLastSearch(recentSearch));
    }

    public static void getRecentSearchesFromLocalDB(MutableLiveData<List<RecentSearch>> recentSearchMutableLiveData) {
        IPApplication.executorService.execute(() ->
                recentSearchMutableLiveData.postValue(localDB.recentSearchesDao().getRecentSearches()));
    }

    public static void deleteAllRecentSearchesInLocalDB() {
        IPApplication.executorService.execute(() -> localDB.recentSearchesDao().deleteAll());
    }

    public static void deleteRecentSearchInLocalDB(RecentSearch recentSearch) {
        IPApplication.executorService.execute(() -> localDB.recentSearchesDao().deleteRecentSearch(recentSearch));
    }

    public static void insertModel(SentimentsModel model) {
        IPApplication.executorService.execute(() -> localDB.sentimentsModelDao().insertModel(model));
    }

    public static void updateModel(SentimentsModel model) {
        IPApplication.executorService.execute(() -> localDB.sentimentsModelDao().updateModel(model));
    }

    public static void getModel(MutableLiveData<SentimentsModel> modelMutableLiveData) {
        IPApplication.executorService.execute(() -> {
            List<SentimentsModel> models = IPApplication.localDB.sentimentsModelDao().getSentimentsModels();
            if (models != null && models.size() > SIZE_EMPTY_LIST) {
                modelMutableLiveData.postValue(models.get(FIRST_INDEX));
            } else {
                modelMutableLiveData.postValue(null);
            }
        });
    }

    public static void getLikeFromTitle(String currentTitle, MutableLiveData<LikeModel> likeMutableLiveData) {
        IPApplication.executorService.execute(() -> {
            List<LikeModel> likes = IPApplication.localDB.likesModelDao().getLike(currentTitle);
            if (likes != null && likes.size() > MIN_SIZE) {
                likeMutableLiveData.postValue(likes.get(FIRST_INDEX));
            } else {
                likeMutableLiveData.postValue(null);
            }
        });
    }

    public static void insertLike(LikeModel like) {
        IPApplication.executorService.execute(() -> localDB.likesModelDao().insertLike(like));
    }

    public static void deleteLike(LikeModel like) {
        IPApplication.executorService.execute(() -> localDB.likesModelDao().deleteLike(like));
    }
}
