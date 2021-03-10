package com.pinalopes.informationspositives.cognitivetext;

import java.io.File;

public interface OnDownloadModelListener {
    void onDownloadSucceed(File downloadedFile);
    void onDownloadFailed(Exception exception);
}
