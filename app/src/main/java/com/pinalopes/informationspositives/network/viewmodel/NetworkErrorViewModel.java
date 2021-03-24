package com.pinalopes.informationspositives.network.viewmodel;

import androidx.lifecycle.ViewModel;

public class NetworkErrorViewModel extends ViewModel {

    private String cause;
    private boolean isReloadDataEnabled;

    public void setNetworkErrorData(String cause, boolean isReloadDataEnabled) {
        setCause(cause);
        setReloadDataEnabled(isReloadDataEnabled);
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public boolean isReloadDataEnabled() {
        return isReloadDataEnabled;
    }

    public void setReloadDataEnabled(boolean reloadDataEnabled) {
        isReloadDataEnabled = reloadDataEnabled;
    }
}
