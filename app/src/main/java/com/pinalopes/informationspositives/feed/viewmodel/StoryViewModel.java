package com.pinalopes.informationspositives.feed.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.squareup.picasso.Picasso;

import static com.pinalopes.informationspositives.Constants.TARGET_HEIGHT;
import static com.pinalopes.informationspositives.Constants.TARGET_WIDTH;

public class StoryViewModel extends ViewModel {

    private String title;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String imageUrl){
        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.get().load(imageUrl).resize(TARGET_WIDTH, TARGET_HEIGHT).onlyScaleDown().into(view);
        }
    }
}
