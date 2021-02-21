package com.pinalopes.informationspositives.settings.viewmodel;

import androidx.lifecycle.ViewModel;

public class AboutUsViewModel extends ViewModel {

    private String appVersion;

    public AboutUsViewModel(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
