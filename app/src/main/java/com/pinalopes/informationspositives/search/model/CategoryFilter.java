package com.pinalopes.informationspositives.search.model;

public class CategoryFilter {

    private String name;
    private String pictureRes;
    private boolean isSelected;

    public CategoryFilter(String name, String pictureRes) {
        this.name = name;
        this.pictureRes = pictureRes;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getPictureRes() {
        return pictureRes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void changeSelectionState() {
        isSelected = !isSelected;
    }
}
