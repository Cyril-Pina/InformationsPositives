package com.pinalopes.informationspositives.settings.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

public class SettingsMenuItemViewModel extends ViewModel {

    private String itemName;
    private int itemIcon;
    private boolean isSwitchable;
    private boolean isChecked;

    public SettingsMenuItemViewModel(String itemName, int itemIcon, boolean isSwitchable, boolean isChecked) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
        this.isSwitchable = isSwitchable;
        this.isChecked = isChecked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public boolean isSwitchable() {
        return isSwitchable;
    }

    public void setSwitchable(boolean switchable) {
        isSwitchable = switchable;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
