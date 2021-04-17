package com.pinalopes.informationspositives.storage;

import com.pinalopes.informationspositives.R;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.INDEX_OF_FAILURE;

public class UserSettings {

    private final List<Integer> categoriesSelected;
    private boolean isNotificationsEnabled;
    private int currentTheme;

    public UserSettings(Builder builder) {
        if (builder.categoriesSelected != null) {
            this.categoriesSelected = builder.categoriesSelected;
        } else {
            this.categoriesSelected = new ArrayList<>();
        }
        this.isNotificationsEnabled = builder.isNotificationsEnabled;
        this.currentTheme = builder.currentTheme;
    }

    public void updateSelectedCategories(int idCategory, boolean isSelected) {
        if (isSelected && !this.categoriesSelected.contains(idCategory)) {
            this.categoriesSelected.add(idCategory);
        } else if (!isSelected && this.categoriesSelected.contains(idCategory)) {
            int index = this.categoriesSelected.indexOf(idCategory);
            if (index != INDEX_OF_FAILURE) {
                this.categoriesSelected.remove(index);
            }
        }
    }

    public List<Integer> getCategoriesSelected() {
        return categoriesSelected;
    }

    public boolean isNotificationsEnabled() {
        return isNotificationsEnabled;
    }

    public void modifyNotificationsState(boolean isChecked) {
        this.isNotificationsEnabled = isChecked;
    }

    public int getCurrentTheme() {
        return currentTheme;
    }

    public void modifyCurrentTheme() {
        if (currentTheme == R.style.AppTheme_NoActionBar) {
            currentTheme = R.style.AppTheme_Dark_NoActionBar;
        } else {
            currentTheme = R.style.AppTheme_NoActionBar;
        }
    }

    static class Builder {
        private List<Integer> categoriesSelected = new ArrayList<>();
        private boolean isNotificationsEnabled;
        private int currentTheme;

        public Builder setCategoriesSelected(final List<Integer> categoriesSelected) {
            this.categoriesSelected = categoriesSelected;
            return this;
        }

        public Builder setNotificationsActivated(boolean notificationsActivated) {
            this.isNotificationsEnabled = notificationsActivated;
            return this;
        }

        public Builder setCurrentTheme(int currentTheme) {
            this.currentTheme = currentTheme;
            return this;
        }

        public UserSettings build() {
            return new UserSettings(this);
        }
    }
}
