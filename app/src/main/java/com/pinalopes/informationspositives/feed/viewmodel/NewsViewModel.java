package com.pinalopes.informationspositives.feed.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.newsapi.responsebody.News;

public class NewsViewModel extends ViewModel {

    MutableLiveData<News> newsMutableLiveData;

    public MutableLiveData<News> getNewsMutableLiveData() {
        if (newsMutableLiveData == null) {
            newsMutableLiveData = new MutableLiveData<>();
        }
        return newsMutableLiveData;
    }
}
