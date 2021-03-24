package com.pinalopes.informationspositives.feed.viewmodel;

import androidx.databinding.BaseObservable;

public class DataLoadingViewModel extends BaseObservable {

    private boolean isDataLoaded;
    private boolean isNetworkOn;

    public DataLoadingViewModel(boolean isDataLoaded, boolean isNetworkOn) {
        this.isDataLoaded = isDataLoaded;
        this.isNetworkOn = isNetworkOn;
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public boolean isNetworkOn() {
        return isNetworkOn;
    }

    public void setNetworkOn(boolean networkOn) {
        isNetworkOn = networkOn;
    }

    public void setDataLoaded(boolean dataLoaded) {
        isDataLoaded = dataLoaded;
    }
}
