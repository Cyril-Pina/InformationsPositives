package com.pinalopes.informationspositives.search.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinalopes.informationspositives.search.model.Filters;

import java.util.Date;

public class SearchActivityViewModel extends ViewModel {

    private boolean isFiltersDisplayed;
    private String beginningDate;
    private String endingDate;
    private MutableLiveData<Date> beginningDateMutable;
    private MutableLiveData<Date> endingDateMutable;
    private MutableLiveData<Filters> filtersMutable;
    private MutableLiveData<Integer> clickOnCategoryMutable;

    public boolean isFiltersDisplayed() {
        return isFiltersDisplayed;
    }

    public void setFiltersDisplayed(boolean filtersDisplayed) {
        isFiltersDisplayed = filtersDisplayed;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public MutableLiveData<Date> getBeginningDateMutable() {
        if (beginningDateMutable == null) {
            beginningDateMutable = new MutableLiveData<>();
        }
        return beginningDateMutable;
    }

    public MutableLiveData<Date> getEndingDateMutable() {
        if (endingDateMutable == null) {
            endingDateMutable = new MutableLiveData<>();
        }
        return endingDateMutable;
    }

    public MutableLiveData<Filters> getFiltersMutable() {
        if (filtersMutable == null) {
            filtersMutable = new MutableLiveData<>();
        }
        return filtersMutable;
    }

    public MutableLiveData<Integer> getClickOnCategoryMutable() {
        if (clickOnCategoryMutable == null) {
            clickOnCategoryMutable = new MutableLiveData<>();
        }
        return clickOnCategoryMutable;
    }
}
