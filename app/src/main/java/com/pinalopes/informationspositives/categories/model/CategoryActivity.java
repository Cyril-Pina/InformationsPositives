package com.pinalopes.informationspositives.categories.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.viewmodel.CategoryViewModel;
import com.pinalopes.informationspositives.databinding.CategoryBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.DataLoadingViewModel;
import com.pinalopes.informationspositives.feed.viewmodel.NewsViewModel;
import com.pinalopes.informationspositives.network.model.NetworkErrorFragment;
import com.pinalopes.informationspositives.network.model.NetworkService;
import com.pinalopes.informationspositives.newsapi.NewsRequestsApi;
import com.pinalopes.informationspositives.newsapi.responsebody.Article;
import com.pinalopes.informationspositives.newsapi.responsebody.News;
import com.pinalopes.informationspositives.storage.DataStorageHelper;
import com.pinalopes.informationspositives.utils.AdapterUtils;
import com.pinalopes.informationspositives.utils.DateUtils;
import com.pinalopes.informationspositives.utils.ResourceUtils;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.ADD_NEW_ELEMENT;
import static com.pinalopes.informationspositives.Constants.CURRENT_CATEGORY;
import static com.pinalopes.informationspositives.Constants.DEFAULT_PAGINATION_VALUE;
import static com.pinalopes.informationspositives.Constants.DIRECTION_SCROLL_VERTICALLY;
import static com.pinalopes.informationspositives.Constants.ELEVATION_TEN;
import static com.pinalopes.informationspositives.Constants.FAILURE_ITERATION_INIT_VALUE;
import static com.pinalopes.informationspositives.Constants.FAILURE_VALUE;
import static com.pinalopes.informationspositives.Constants.INITIAL_VALUE_NB_ELEMENTS_ADDED;
import static com.pinalopes.informationspositives.Constants.MAX_FAILURE_ITERATION;
import static com.pinalopes.informationspositives.Constants.MIN_SIZE;
import static com.pinalopes.informationspositives.Constants.NEXT_PAGE;
import static com.pinalopes.informationspositives.Constants.NO_ARTICLE;
import static com.pinalopes.informationspositives.Constants.NO_BACKGROUND_RESOURCE;
import static com.pinalopes.informationspositives.Constants.NO_ELEVATION;

public class CategoryActivity extends AppCompatActivity implements NetworkErrorFragment.OnNetworkErrorEventListener{

    private CategoryBinding binding;
    private Category category;
    private NewsViewModel viewModel;
    private List<ArticleRowViewModel> categoryArticleDataList;

    private int page = DEFAULT_PAGINATION_VALUE;
    private int failureIteration = FAILURE_ITERATION_INIT_VALUE;

    private int currentThemeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        currentThemeId = DataStorageHelper.getUserSettings().getCurrentTheme();
        setTheme(currentThemeId);
        super.onCreate(savedInstanceState);
        binding = CategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Slidr.attach(this);

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        categoryArticleDataList = new ArrayList<>();
        binding.articleCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initCategoryView(savedInstanceState);
        setNestedScrollViewListener();
        setOnClickHeaderButtons();
        setOnFeedListReachesBottomListener();
        updateDataLoadingViewModel(false, true);
        initArticleCategoryRecyclerView(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CURRENT_CATEGORY, new Gson().toJson(category));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefreshButtonPressed() {
        updateDataLoadingViewModel(false, true);
        if (viewModel.getNewsMutableLiveData().hasObservers()) {
            NewsRequestsApi.getInstance().getLatestCategoryNews(
                    viewModel.getNewsMutableLiveData(),
                    getString(R.string.lang_prefix),
                    category.getCategoryName(),
                    DateUtils.getActualDate(),
                    page);
        } else {
            initArticleCategoryRecyclerView(this);
        }
    }

    private void initCategoryView(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            category = new Gson().fromJson(extras.getString(CURRENT_CATEGORY), Category.class);
        } else if (savedInstanceState != null) {
            category = new Gson().fromJson(savedInstanceState.getString(CURRENT_CATEGORY), Category.class);
        }
        binding.setCategoryViewModel(new CategoryViewModel(category, category.getDescFromName(this)));
    }

    private void initArticleCategoryRecyclerView(Context context) {
        if (!viewModel.getNewsMutableLiveData().hasObservers()) {
            viewModel.getNewsMutableLiveData().observe((LifecycleOwner) context, news -> {
                if (news != null && news.getTotalResults() > NO_ARTICLE) {
                    int nbElementsAdded = updateCategoryArticleDataList(news);
                    showArticlesLayout();
                    initArticleCategoryAdapter(this.categoryArticleDataList, nbElementsAdded);
                    failureIteration = FAILURE_ITERATION_INIT_VALUE;
                    page += NEXT_PAGE;
                    return;
                } else if (failureIteration < MAX_FAILURE_ITERATION) {
                    NewsRequestsApi.getInstance().getLatestCategoryNews(
                            viewModel.getNewsMutableLiveData(),
                            getString(R.string.lang_prefix),
                            category.getCategoryName(),
                            DateUtils.getPreviousDate(),
                            page);
                } else if (categoryArticleDataList.size() <= MIN_SIZE) {
                    showNetworkErrorFragment(NetworkService.isNetworkOn(context) ?
                            getString(R.string.article_not_found) : getString(R.string.no_network_connection));
                } else {
                    updateDataLoadingViewModel(true, true);
                }
                failureIteration += FAILURE_VALUE;
            });
        }
        NewsRequestsApi.getInstance().getLatestCategoryNews(
                viewModel.getNewsMutableLiveData(),
                getString(R.string.lang_prefix),
                category.getCategoryName(),
                DateUtils.getActualDate(),
                page);
    }

    private int updateCategoryArticleDataList(News news) {
        int nbElementsAdded = INITIAL_VALUE_NB_ELEMENTS_ADDED;
        for (Article article : news.getArticles()) {
            if (AdapterUtils.isArticleValid(article)
                    && AdapterUtils.isArticleNonDuplicate(categoryArticleDataList, article.getTitle())) {
                ArticleRowViewModel articleCategoryViewModel = new ArticleRowViewModel();
                articleCategoryViewModel.setTitle(article.getTitle());
                articleCategoryViewModel.setImageUrl(article.getUrlToImage());
                articleCategoryViewModel.setWriter(AdapterUtils.getArticleWriter(article));
                articleCategoryViewModel.setDate(DateUtils.formatArticlePublishedDate(article.getPublishedAt()));
                articleCategoryViewModel.setText(article.getContent());
                articleCategoryViewModel.setDescription(article.getDescription());
                articleCategoryViewModel.setLinkToArticle(article.getUrl());
                articleCategoryViewModel.setCategory(AdapterUtils.getFeedGeneralCategory(binding.getRoot().getContext(),
                        R.style.AppTheme_Dark_NoActionBar));
                categoryArticleDataList.add(articleCategoryViewModel);
                nbElementsAdded += ADD_NEW_ELEMENT;
            }
        }
        return nbElementsAdded;
    }

    private void setNestedScrollViewListener() {
        final double boundaryChangeColor = getResources().getDimension(R.dimen.height_category_header) * 0.7;
        final int colorBlack = ContextCompat.getColor(this, android.R.color.black);
        final int colorWhite = ContextCompat.getColor(this, android.R.color.white);
        final Animation fadeInAnimation  = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        final Bitmap iconCategoryWhite = ResourceUtils.imgResToBitmap(this, category.getCategoryIcon());
        final Bitmap iconCategoryBlack = ResourceUtils.imgResToBitmap(this,
                ResourceUtils.getCategoryBlackIcon(this, category.getCategoryIcon()));
        final int leftArrowWhiteRes = R.drawable.left_arrow_white_selector;
        final int leftArrowBlackRes = R.drawable.left_arrow_selector;

        binding.articleCategoryScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY <= boundaryChangeColor && oldScrollY > boundaryChangeColor) {
                if (currentThemeId == R.style.AppTheme_NoActionBar) {
                    animateTextColorChanges(colorWhite, fadeInAnimation);
                    animateImageSrcChanges(iconCategoryWhite, fadeInAnimation);
                    animateImageButtonChanges(leftArrowWhiteRes, fadeInAnimation);
                }
                updateHeaderBackground(NO_ELEVATION, NO_BACKGROUND_RESOURCE);
            } else if (scrollY > boundaryChangeColor && oldScrollY <= boundaryChangeColor) {
                if (currentThemeId == R.style.AppTheme_NoActionBar) {
                    animateTextColorChanges(colorBlack, fadeInAnimation);
                    updateHeaderBackground(ELEVATION_TEN, Color.WHITE);
                    animateImageSrcChanges(iconCategoryBlack, fadeInAnimation);
                    animateImageButtonChanges(leftArrowBlackRes, fadeInAnimation);
                } else {
                    updateHeaderBackground(ELEVATION_TEN, Color.BLACK);
                }
            }
        });
    }

    private void setOnFeedListReachesBottomListener() {
        binding.articleCategoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(DIRECTION_SCROLL_VERTICALLY) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    initArticleCategoryRecyclerView(CategoryActivity.this);
                }
            }
        });
    }

    private void setOnClickHeaderButtons() {
        binding.leftArrowButton.setOnClickListener(v -> finish());
    }

    private void initArticleCategoryAdapter(List<ArticleRowViewModel> categoryArticleDataList, int nbElementsAdded) {
        if (binding.articleCategoryRecyclerView.getAdapter() != null
                && categoryArticleDataListIsAlreadyFilled(categoryArticleDataList, nbElementsAdded)) {
            int startIndex = categoryArticleDataList.size() - nbElementsAdded;
            notifyItemsAddedInCategoryFeed(startIndex, nbElementsAdded);
        } else {
            ArticleCategoryRecyclerAdapter adapter = new ArticleCategoryRecyclerAdapter(categoryArticleDataList);
            binding.articleCategoryRecyclerView.setAdapter(adapter);
            binding.articleCategoryRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void notifyItemsAddedInCategoryFeed(int startIndex, int size) {
        if (binding.articleCategoryRecyclerView.getAdapter() != null) {
            binding.articleCategoryRecyclerView.getAdapter().notifyItemRangeInserted(startIndex, size);
        }
    }

    private boolean categoryArticleDataListIsAlreadyFilled(List<ArticleRowViewModel> categoryArticleDataList, int nbElementsAdded) {
        return categoryArticleDataList.size() > nbElementsAdded;
    }

    private void updateDataLoadingViewModel(boolean isDataLoaded, boolean isNetworkOn) {
        if (binding.getDataLoadingViewModel() != null) {
            binding.getDataLoadingViewModel().setDataLoaded(isDataLoaded);
            binding.getDataLoadingViewModel().setNetworkOn(isNetworkOn);
        } else {
            binding.setDataLoadingViewModel(new DataLoadingViewModel(isDataLoaded, isNetworkOn));
        }
        binding.invalidateAll();
    }

    private void showArticlesLayout() {
        if (!isDataVisible()) {
            updateDataLoadingViewModel(true, true);
        }
    }

    private boolean isDataVisible() {
        return binding.getDataLoadingViewModel() != null && binding.getDataLoadingViewModel().isDataLoaded();
    }

    private void showNetworkErrorFragment(String cause) {
        updateDataLoadingViewModel(true, false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        NetworkErrorFragment networkErrorFragment = NetworkErrorFragment.newInstance(cause, true);
        networkErrorFragment.setOnNetworkErrorEventListener(this);
        fragmentManager.beginTransaction().replace(R.id.networkErrorFragment, networkErrorFragment).commitAllowingStateLoss();
    }

    private void animateTextColorChanges(int newColor, Animation textColorInAnim) {
        binding.categoryTitle.setTextColor(newColor);
        binding.categoryTitle.startAnimation(textColorInAnim);
    }

    private void animateImageSrcChanges(Bitmap newImage, Animation imageBitmapInAnim) {
        binding.categoryIcon.setImageBitmap(newImage);
        binding.categoryIcon.startAnimation(imageBitmapInAnim);
    }

    private void animateImageButtonChanges(int newImageRes,  Animation imageResInAnim) {
        binding.leftArrowButton.setImageResource(newImageRes);
        binding.leftArrowButton.startAnimation(imageResInAnim);
    }

    private void updateHeaderBackground(float elevation, int color) {
        binding.categoryTitleLayout.setElevation(elevation);
        if (color == NO_BACKGROUND_RESOURCE) {
            binding.categoryTitleLayout.setBackgroundResource(NO_BACKGROUND_RESOURCE);
        } else {
            binding.categoryTitleLayout.setBackgroundColor(color);
        }
    }
}
