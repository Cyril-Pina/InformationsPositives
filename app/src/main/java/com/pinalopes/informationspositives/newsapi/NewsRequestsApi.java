package com.pinalopes.informationspositives.newsapi;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pinalopes.informationspositives.newsapi.responsebody.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pinalopes.informationspositives.Constants.NEWS_API_TOKEN;

public class NewsRequestsApi extends NewsApi {

    private static final String TAG = "NewsRequestsApi";

    private static final NewsRequestsApi instance = new NewsRequestsApi();

    public static NewsRequestsApi getInstance() {
        return instance;
    }

    public void getTopHeadlineNews(MutableLiveData<News> newsMutableLiveData, String country) {
        Call<News> call = service.getTopHeadlinesNews(country, NEWS_API_TOKEN);
        call.enqueue(getNewsCallBack(newsMutableLiveData));
    }

    public void getLatestNews(MutableLiveData<News> newsMutableLiveData, String language, String from, int page) {
        Call<News> call = service.getLatestNews(language, from, page, NEWS_API_TOKEN);
        call.enqueue(getNewsCallBack(newsMutableLiveData));
    }

    public void getFilteredNews(MutableLiveData<News> newsMutableLiveData, String keywords, String language, int page) {
        Call<News> call = service.getFilteredNews(language, keywords, page, NEWS_API_TOKEN);
        call.enqueue(getNewsCallBack(newsMutableLiveData));
    }

    public void getFilteredNews(MutableLiveData<News> newsMutableLiveData, String keywords, String to, String language, int page) {
        Call<News> call = service.getFilteredNews(language, keywords, to, page, NEWS_API_TOKEN);
        call.enqueue(getNewsCallBack(newsMutableLiveData));
    }

    public void getFilteredNews(MutableLiveData<News> newsMutableLiveData, String keywords, String from, String to, String language, int page) {
        Call<News> call = service.getFilteredNews(language, keywords, from, to, page, NEWS_API_TOKEN);
        call.enqueue(getNewsCallBack(newsMutableLiveData));
    }

    private Callback<News> getNewsCallBack(MutableLiveData<News> newsMutableLiveData) {
        return new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                newsMutableLiveData.setValue(response.isSuccessful() ? response.body() : null);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, t.toString());
                newsMutableLiveData.setValue(null);
            }
        };
    }
}
