package com.pinalopes.informationspositives.settings.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

public class HeaderAboutUsViewModel extends ViewModel {

    private int imageRes;

    public HeaderAboutUsViewModel(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
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
