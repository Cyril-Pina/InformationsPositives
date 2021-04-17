package com.pinalopes.informationspositives.search.model;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Transition;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.TransitionService;
import com.pinalopes.informationspositives.databinding.SearchActivityBinding;
import com.pinalopes.informationspositives.feed.model.OnArticleEventListener;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.search.viewmodel.SearchActivityViewModel;
import com.pinalopes.informationspositives.storage.DataStorageHelper;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.DateUtils;
import com.pinalopes.informationspositives.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pinalopes.informationspositives.Constants.DATE_FORMAT_FILTER;
import static com.pinalopes.informationspositives.Constants.DEFAULT_PAGINATION_VALUE;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;

public class SearchActivity extends AppCompatActivity implements OnArticleEventListener {

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
    private int currentThemeId;

    private List<ArticleRowViewModel> feedArticleDataList;
    private int feedPage = DEFAULT_PAGINATION_VALUE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(DataStorageHelper.getUserSettings().getCurrentTheme());
        currentThemeId = DataStorageHelper.getUserSettings().getCurrentTheme();
        super.onCreate(savedInstanceState);
        binding = SearchActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setSearchActivityViewModel(new ViewModelProvider(this).get(SearchActivityViewModel.class));

        setOnReturnButtonClickListener();
        setOnKeyboardActionButtonClickListener();
        setOnFiltersLayoutClickListener();
        setOnRecentSearchClickListener();
        initSearchResultsFragment();
        initCategoriesFilterAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.cardViewTopBar.post(this::startTopBarViewsAnimation);
        binding.searchEditText.postDelayed(this::focusSearchEditText, DELAY_POST_KEYBOARD);
        openSearchFragmentWithSearchResults(binding.searchEditText.getText().toString(), filters);
    }

    @Override
    public void onBackPressed() {
        reverseTopBarViewsAnimation();
    }

    private void setOnReturnButtonClickListener() {
        binding.leftArrowButton.setOnClickListener(view -> reverseTopBarViewsAnimation());
    }

    private void setOnKeyboardActionButtonClickListener() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                ViewUtils.hideSoftKeyboard((Activity) v.getContext());
                String recentArticleSearched = v.getText().toString();
                if (!recentArticleSearched.isEmpty()) {
                    DataStorageHelper.saveRecentSearchInLocalDB(recentArticleSearched);
                }
                return true;
            }
            return false;
        });
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

    private void setOnRecentSearchClickListener() {
        binding.getSearchActivityViewModel().getClickOnRecentSearchMutable().observe(this, recentSearch -> {
            binding.searchEditText.setText(recentSearch);
            binding.searchEditText.setSelection(recentSearch.length());
            ViewUtils.hideSoftKeyboard(this);
            openSearchFragment(recentSearch, filters);
        });
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
        FilterCategoriesAdapter adapter = new FilterCategoriesAdapter(this, AdapterUtils.getFilterCategories(this),
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
        searchResultsFragment.setOnArticleEventListener(this, new ArrayList<>(), DEFAULT_PAGINATION_VALUE);
        fragmentTransaction.replace(R.id.searchResultsFrameLayout, searchResultsFragment);
        fragmentTransaction.commit();
    }

    private void openSearchFragmentWithSearchResults(String keyWordSearch, Filters filters) {
        if (feedArticleDataList != null && feedArticleDataList.size() > MIN_SIZE) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
            searchResultsFragment.setOnArticleEventListener(this, this.feedArticleDataList, feedPage);
            fragmentTransaction.replace(R.id.searchResultsFrameLayout, searchResultsFragment);
            fragmentTransaction.commit();
        } else {
            openSearchFragment(keyWordSearch, filters);
        }
    }

    private void initDateFiltersMutable() {
        binding.getSearchActivityViewModel().getBeginningDateMutable().observe(this, date -> {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FILTER, Locale.getDefault());
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
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FILTER, Locale.getDefault());
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

            int alertDialogStyle = (currentThemeId == R.style.AppTheme_NoActionBar) ?
                    R.style.DateFilterSpinnerStyle : R.style.DateFilterSpinnerDarkStyle;

            DatePickerDialog picker = new DatePickerDialog(SearchActivity.this,
                    alertDialogStyle,
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

    public void clearSearchEditText(View view) {
        if (binding.searchEditText.getText().length() > MIN_SIZE) {
            binding.searchEditText.getText().clear();
        }
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

    @Override
    public void onArticleUpdated(List<ArticleRowViewModel> feedArticleDataList, int page) {
        this.feedArticleDataList = feedArticleDataList;
        this.feedPage = page;
    }
}