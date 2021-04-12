package com.pinalopes.informationspositives.articles.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.storage.LikeModel;

public class ArticleViewModel extends ArticleRowViewModel {

    private MutableLiveData<LikeModel> likeModelMutableLiveData;

    private boolean isLiked;
    private boolean isHeaderVisible;

    public ArticleViewModel() {}

    public ArticleViewModel(ArticleRowViewModel articleRowViewModel) {
        super(articleRowViewModel);
    }

    public MutableLiveData<LikeModel> getLikeModelMutableLiveData() {
        if (likeModelMutableLiveData == null) {
            likeModelMutableLiveData = new MutableLiveData<>();
        }
        return likeModelMutableLiveData;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isHeaderVisible() {
        return isHeaderVisible;
    }

    public void setHeaderVisible(boolean headerVisible) {
        isHeaderVisible = headerVisible;
    }
}
