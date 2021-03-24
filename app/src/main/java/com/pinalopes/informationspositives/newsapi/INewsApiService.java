package com.pinalopes.informationspositives.newsapi;

import com.pinalopes.informationspositives.newsapi.responsebody.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INewsApiService {

    @GET("/v2/top-headlines")
    Call<News> getTopHeadlinesNews(@Query("country") String country, @Query("apiKey") String key);

    @GET("/v2/everything?q=a&excludeDomains=fark.com,9gag.com&sortBy=publishedAt")
    Call<News> getLatestNews(@Query("language") String language, @Query("from") String from, @Query("page") int page, @Query("apiKey") String key);

    @GET("/v2/everything")
    Call<News> getFilteredNews(@Query("language") String language, @Query("q") String q, @Query("page") int page, @Query("apiKey") String key);

    @GET("/v2/everything")
    Call<News> getFilteredNews(@Query("language") String language, @Query("q") String q, @Query("to") String to,  @Query("page") int page, @Query("apiKey") String key);

    @GET("/v2/everything")
    Call<News> getFilteredNews(@Query("language") String language, @Query("q") String q, @Query("from") String from, @Query("to") String to,  @Query("page") int page, @Query("apiKey") String key);
}
