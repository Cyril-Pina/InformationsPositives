package com.pinalopes.informationspositives.search.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.TransitionService;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.databinding.SearchActivityBinding;
import com.pinalopes.informationspositives.search.viewmodel.FilterCategoriesViewModel;
import com.pinalopes.informationspositives.search.viewmodel.SearchActivityViewModel;
import com.pinalopes.informationspositives.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;

public class SearchActivity extends AppCompatActivity {

    private static final long DELAY_POST_KEYBOARD = 250;
    private static final long START_DELAY_TRANSITION = 0;
    private static final long DURATION_TRANSITION_FILTER = 200;
    private static final long DURATION_TRANSITION = 300;
    private static final long DURATION_TRANSITION_ARROW = 1000;
    private static final float VISIBLE_ALPHA_VALUE = 1.0f;
    private static final int DEF_HEIGHT_DATA_FILTER_LAYOUT = 1;

    private SearchActivityBinding binding;
    private boolean isFiltersDisplayed = false;
    private Filters filters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnReturnButtonClickListener();
        setOnFiltersLayoutClickListener();
        initSearchResultsFragment();
        initCategoriesFilterAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.cardViewTopBar.post(this::startTopBarViewsAnimation);
        binding.searchEditText.postDelayed(this::focusSearchEditText, DELAY_POST_KEYBOARD);
    }

    @Override
    public void onBackPressed() {
        reverseTopBarViewsAnimation();
    }

    private void setOnReturnButtonClickListener() {
        binding.leftArrowButton.setOnClickListener(view -> reverseTopBarViewsAnimation());
    }

    private void setOnFiltersLayoutClickListener() {
        binding.filtersLayout.setVisibility(View.GONE);
        initDateFiltersMutable();
        binding.titleFiltersLayout.setOnClickListener(view -> {
            if (!isFiltersDisplayed) {
                startFiltersLayoutTransition(ViewGroup.LayoutParams.WRAP_CONTENT);
                showDatePickerSpinnerOnClick(binding.beginningDateTextView, getOnBeginningDateSetListener());
                showDatePickerSpinnerOnClick(binding.endingDateTextView, getOnEndingDateSetListener());
            }
        });
        binding.validateImageButton.setOnClickListener(view -> startFiltersLayoutTransition(DEF_HEIGHT_DATA_FILTER_LAYOUT));
    }

    private void startFiltersLayoutTransition(int newLayoutHeight) {
        isFiltersDisplayed = !isFiltersDisplayed;
        binding.getSearchActivityViewModel().setFiltersDisplayed(isFiltersDisplayed);
        binding.invalidateAll();
        final ConstraintLayout.LayoutParams layoutParamsFilters = (ConstraintLayout.LayoutParams) binding.dataFiltersLayout.getLayoutParams();

        TransitionService.setLayoutTransition(
                binding.filtersLayout,
                binding.dataFiltersLayout,
                binding.filtersLayout,
                START_DELAY_TRANSITION,
                DURATION_TRANSITION_FILTER);

        layoutParamsFilters.height = newLayoutHeight;
        binding.dataFiltersLayout.setLayoutParams(layoutParamsFilters);
    }

    private void initCategoriesFilterAdapter() {
        List<FilterCategoriesViewModel> categories = new ArrayList<>();
        String[] categoriesName = getResources().getStringArray(R.array.categories_name);
        String[] categoriesRes = getResources().getStringArray(R.array.categories_res);
        String[] categoriesIcon = getResources().getStringArray(R.array.categories_icon);

        for (int i = 0; i != categoriesName.length ; i ++) {
            String categoryName = categoriesName[i];
            int categoryRes =  getResources().getIdentifier(categoriesRes[i], DRAWABLE, PACKAGE_NAME);
            int categoryIcon =  getResources().getIdentifier(categoriesIcon[i], DRAWABLE, PACKAGE_NAME);

            FilterCategoriesViewModel filterCategoriesViewModel = new FilterCategoriesViewModel(
                    new Category(categoryName,
                            categoryRes,
                            categoryIcon),
                    false
            );
            categories.add(filterCategoriesViewModel);
        }

        FilterCategoriesAdapter adapter = new FilterCategoriesAdapter(this, categories,
                binding.getSearchActivityViewModel().getClickOnCategoryMutable());
        binding.filterCategoriesGridView.setAdapter(adapter);
    }

    private void initSearchResultsFragment() {
        filters = new Filters();
        binding.getSearchActivityViewModel().getClickOnCategoryMutable().observe(this, position -> {
            filters.getCategories()[position] = !filters.getCategories()[position];
            binding.getSearchActivityViewModel().getFiltersMutable().setValue(filters);
        });
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // beforeTextChanged() ignored
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // onTextChanged() ignored
            }
            @Override
            public void afterTextChanged(Editable keyWordEditable) {
                openSearchFragment(keyWordEditable.toString(), filters);
            }
        });
        binding.getSearchActivityViewModel().getFiltersMutable().observe(this, currentFilters -> {
            String currentKeyWordSearch = binding.searchEditText.getText().toString();
            openSearchFragment(currentKeyWordSearch, currentFilters);
        });
    }

    private void openSearchFragment(String keyWordSearch, Filters filters) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SearchResultsFragment searchResultsFragment = SearchResultsFragment.newInstance(keyWordSearch, filters);
        fragmentTransaction.replace(R.id.searchResultsFrameLayout, searchResultsFragment);
        fragmentTransaction.commit();
    }

    private void initDateFiltersMutable() {
        binding.setSearchActivityViewModel(new SearchActivityViewModel());
        binding.getSearchActivityViewModel().getBeginningDateMutable().observe(this, date -> {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.getSearchActivityViewModel().setBeginningDate(format.format(date));
            binding.invalidateAll();

            Date endingDate = binding.getSearchActivityViewModel().getEndingDateMutable().getValue();
            if (endingDate != null
                && DateUtils.dateIsGreater(date, endingDate)) {
                binding.getSearchActivityViewModel().getEndingDateMutable().setValue(date);
            } else {
                filters.setBeginningDate(format.format(date));
                binding.getSearchActivityViewModel().getFiltersMutable().setValue(filters);
            }
        });

        binding.getSearchActivityViewModel().getEndingDateMutable().observe(this, date -> {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.getSearchActivityViewModel().setEndingDate(format.format(date));
            binding.invalidateAll();

            Date beginningDate = binding.getSearchActivityViewModel().getBeginningDateMutable().getValue();
            if (beginningDate != null
                    && DateUtils.dateIsLower(date, beginningDate)) {
                binding.getSearchActivityViewModel().getBeginningDateMutable().setValue(date);
            } else {
                filters.setEndingDate(format.format(date));
                binding.getSearchActivityViewModel().getFiltersMutable().setValue(filters);
            }
        });
    }

    private void showDatePickerSpinnerOnClick(TextView dateTextView, DatePickerDialog.OnDateSetListener onDateSetListener) {
        dateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(SearchActivity.this,
                    R.style.DateFilterSpinnerStyle,
                    onDateSetListener,
                    year,
                    month,
                    day);
            picker.show();
        });
    }

    private DatePickerDialog.OnDateSetListener getOnBeginningDateSetListener() {
        return (datePicker, year, monthOfYear, dayOfMonth) -> {
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            Date beginningDate = DateUtils.getDateFromString(year, monthOfYear, dayOfMonth, format);
            binding.getSearchActivityViewModel().getBeginningDateMutable().setValue(beginningDate);
        };
    }

    private DatePickerDialog.OnDateSetListener getOnEndingDateSetListener() {
        return (datePicker, year, monthOfYear, dayOfMonth) -> {
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            Date endingDate = DateUtils.getDateFromString(year, monthOfYear, dayOfMonth, format);
            binding.getSearchActivityViewModel().getEndingDateMutable().setValue(endingDate);
        };
    }

    private void focusSearchEditText() {
        binding.searchEditText.setFocusableInTouchMode(true);
        binding.searchEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startTopBarViewsAnimation() {
        final RelativeLayout.LayoutParams layoutParamsLogo = (RelativeLayout.LayoutParams) binding.logoImageView.getLayoutParams();
        final RelativeLayout.LayoutParams layoutParamsSearch = (RelativeLayout.LayoutParams) binding.searchEditText.getLayoutParams();

        TransitionService.setLayoutTransition(
                getTransitionListenerStart(),
                binding.cardViewTopBar,
                binding.logoImageView,
                binding.searchEditText,
                START_DELAY_TRANSITION,
                DURATION_TRANSITION);

        layoutParamsLogo.removeRule(RelativeLayout.CENTER_IN_PARENT);
        binding.logoImageView.setLayoutParams(layoutParamsLogo);

        layoutParamsSearch.addRule(RelativeLayout.END_OF, R.id.leftArrowButton);
        binding.searchEditText.setLayoutParams(layoutParamsSearch);

        binding.leftArrowButton.animate().alpha(VISIBLE_ALPHA_VALUE).setDuration(DURATION_TRANSITION_ARROW);
    }

    private void reverseTopBarViewsAnimation() {
        final RelativeLayout.LayoutParams layoutParamsLogo = (RelativeLayout.LayoutParams) binding.logoImageView.getLayoutParams();
        final RelativeLayout.LayoutParams layoutParamsSearch = (RelativeLayout.LayoutParams) binding.searchEditText.getLayoutParams();

        TransitionService.setLayoutTransition(
                getTransitionListenerReverse(),
                binding.cardViewTopBar,
                binding.logoImageView,
                binding.searchEditText,
                START_DELAY_TRANSITION,
                DURATION_TRANSITION);

        layoutParamsLogo.addRule(RelativeLayout.CENTER_IN_PARENT);
        binding.logoImageView.setLayoutParams(layoutParamsLogo);

        layoutParamsSearch.removeRule(RelativeLayout.END_OF);
        binding.searchEditText.setLayoutParams(layoutParamsSearch);

        binding.leftArrowButton.setVisibility(View.GONE);
    }

    private Transition.TransitionListener getTransitionListenerStart() {
        return new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                // onTransitionStart() ignored
            }
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                binding.filtersLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
                // onTransitionCancel() ignored
            }
            @Override
            public void onTransitionPause(@NonNull Transition transition) {
                // onTransitionPause() ignored
            }
            @Override
            public void onTransitionResume(@NonNull Transition transition) {
                // onTransitionResume() ignored
            }
        };
    }

    private Transition.TransitionListener getTransitionListenerReverse() {
        return new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                binding.filtersLayout.setVisibility(View.GONE);
            }
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                finish();
                overridePendingTransition(
                        getResources().getInteger(R.integer.no_animation),
                        getResources().getInteger(R.integer.no_animation));
            }
            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
                // onTransitionCancel() ignored
            }
            @Override
            public void onTransitionPause(@NonNull Transition transition) {
                // onTransitionPause() ignored
            }
            @Override
            public void onTransitionResume(@NonNull Transition transition) {
                // onTransitionResume() ignored
            }
        };
    }
}