package com.pinalopes.informationspositives.settings.viewmodel;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class TopCategoriesViewModel extends ViewModel {

    public final ObservableField<String> totalTopCategoriesSelected = new ObservableField<>();
    public final ObservableField<Integer> textColor = new ObservableField<>();
    public final ObservableField<Integer> textStyle = new ObservableField<>();

    public TopCategoriesViewModel(String totalTopCategoriesSelected, int textColor, int textStyle) {
        this.totalTopCategoriesSelected.set(totalTopCategoriesSelected);
        this.textColor.set(textColor);
        this.textStyle.set(textStyle);
    }

    @BindingAdapter({"bind:color"})
    public static void setTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    @BindingAdapter({"android:textStyle"})
    public static void setTextStyle(TextView textView, int style) {
        textView.setTypeface(textView.getTypeface(), style);
    }
}
