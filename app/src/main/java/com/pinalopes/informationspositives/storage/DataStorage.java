package com.pinalopes.informationspositives.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinalopes.informationspositives.R;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class DataStorage {

    private static SharedPreferences sharedPref;
    private static Gson gson;
    private static UserSettings userSettings;
    public static final Random rand = new Random();

    private DataStorage() {
        throw new AssertionError();
    }

    public static void initDataStorage(Context context) {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        gson = new Gson();
        userSettings = getUserSettings(context);
    }

    public static UserSettings getUserSettings() {
        return userSettings;
    }

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

    public static void updateNotificationsState(Context context) {
        userSettings.modifyNotificationsState();
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
}
