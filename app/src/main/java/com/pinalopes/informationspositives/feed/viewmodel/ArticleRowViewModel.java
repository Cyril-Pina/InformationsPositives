package com.pinalopes.informationspositives.feed.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.categories.model.Category;
import com.squareup.picasso.Picasso;

import static com.pinalopes.informationspositives.Constants.TARGET_HEIGHT;
import static com.pinalopes.informationspositives.Constants.TARGET_WIDTH;

public class ArticleRowViewModel extends ViewModel {

    protected String title;
    protected String date;
    protected String writer;
    protected String text;
    protected String description;
    protected String linkToArticle;
    protected Category category;
    protected long nbViews;
    protected long nbLikes;
    protected int imageRes;
    protected String imageUrl;
    protected boolean isVideo;

    public ArticleRowViewModel() {}

    public ArticleRowViewModel(ArticleRowViewModel articleRowViewModel) {
        this.title = articleRowViewModel.title;
        this.date = articleRowViewModel.date;
        this.writer = articleRowViewModel.writer;
        this.text = articleRowViewModel.text;
        this.description = articleRowViewModel.description;
        this.linkToArticle = articleRowViewModel.linkToArticle;
        this.category = articleRowViewModel.category;
        this.nbViews = articleRowViewModel.nbViews;
        this.nbLikes = articleRowViewModel.nbLikes;
        this.imageRes = articleRowViewModel.imageRes;
        this.imageUrl = articleRowViewModel.imageUrl;
        this.imageUrl = articleRowViewModel.imageUrl;
        this.isVideo = articleRowViewModel.isVideo;
    }

    public ArticleRowViewModel(String title, String date, String writer, String text, String description, Category category, long nbViews, long nbLikes, int imageRes, String imageUrl, boolean isVideo) {
        this.title = title;
        this.date = date;
        this.writer = writer;
        this.text = text;
        this.description = description;
        this.category = category;
        this.nbViews = nbViews;
        this.nbLikes = nbLikes;
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkToArticle() {
        return linkToArticle;
    }

    public void setLinkToArticle(String linkToArticle) {
        this.linkToArticle = linkToArticle;
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

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String imageUrl){
        Picasso.get().load(imageUrl).resize(TARGET_WIDTH, TARGET_HEIGHT).onlyScaleDown().into(view);
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
