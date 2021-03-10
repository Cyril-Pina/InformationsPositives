package com.pinalopes.informationspositives.cognitivetext;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.pinalopes.informationspositives.application.IPApplication;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

import java.io.File;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.MAX_NEGATIVE_SCORE;
import static com.pinalopes.informationspositives.Constants.NEGATIVE_CATEGORY;

public class TextRecognition {

    private TextRecognition() {
        throw new AssertionError();
    }

    public static synchronized void downloadModel(String modelName, OnDownloadModelListener listener) {
        final FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel
                        .Builder(modelName)
                        .build();
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        final FirebaseModelManager firebaseModelManager = FirebaseModelManager.getInstance();
        firebaseModelManager
                .download(remoteModel, conditions)
                .continueWithTask(task ->
                        firebaseModelManager.getLatestModelFile(remoteModel)
                )
                .continueWith(IPApplication.executorService, (Continuation<File, Void>) task -> {
                    File modelFile = task.getResult();
                    listener.onDownloadSucceed(modelFile);
                    return null;
                })
                .addOnFailureListener(listener::onDownloadFailed);
    }

    public static void classifyText(NLClassifier textClassifier, String textToClassify, OnTextClassifyResponseListener listener) {
        IPApplication.executorService.execute(() -> {
            List<Category> categories = textClassifier.classify(textToClassify);
            for (Category category : categories) {
                if (category.getLabel().equals(NEGATIVE_CATEGORY)) {
                    listener.onTextClassifyResponse(category.getScore() < MAX_NEGATIVE_SCORE);
                    break;
                }
            }
        });
    }
}
