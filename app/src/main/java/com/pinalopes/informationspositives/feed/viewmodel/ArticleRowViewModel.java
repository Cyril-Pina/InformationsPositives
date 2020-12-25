package com.pinalopes.informationspositives.feed.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.categories.model.Category;

public class ArticleRowViewModel extends ViewModel {

    private String title;
    private String date;
    private String writer;
    private Category category;
    private long nbViews;
    private long nbLikes;
    private boolean isVideo;

    public ArticleRowViewModel(String title, String date, String writer, Category category, long nbViews, long nbLikes, boolean isVideo) {
        this.title = title;
        this.date = date;
        this.writer = writer;
        this.category = category;
        this.nbViews = nbViews;
        this.nbLikes = nbLikes;
        this.isVideo = isVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getNbViews() {
        return nbViews;
    }

    public void setNbViews(long nbViews) {
        this.nbViews = nbViews;
    }

    public long getNbLikes() {
        return nbLikes;
    }

    public void setNbLikes(long nbLikes) {
        this.nbLikes = nbLikes;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"app:srcCompat"})
    public static void setImageViewResourceApp(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"tools:srcCompat"})
    public static void setImageViewResourceTools(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
