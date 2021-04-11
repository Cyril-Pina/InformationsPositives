package com.pinalopes.informationspositives;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.pinalopes.informationspositives.categories.model.CategoriesFragment;
import com.pinalopes.informationspositives.databinding.ActivityMainBinding;
import com.pinalopes.informationspositives.feed.model.FeedFragment;
import com.pinalopes.informationspositives.feed.model.OnArticleEventListener;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;
import com.pinalopes.informationspositives.search.model.SearchActivity;
import com.pinalopes.informationspositives.settings.model.SettingsFragment;
import com.pinalopes.informationspositives.storage.DataStorageHelper;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.DEFAULT_PAGINATION_VALUE;
import static com.pinalopes.informationspositives.Constants.IS_ACTIVITY_RECREATED;

public class MainActivity extends AppCompatActivity implements OnArticleEventListener {

    private ActivityMainBinding binding;
    private Fragment currentFragment;
    private int fragmentId = R.id.action_feed;
    private boolean isRecreated = false;
    private int currentItemId;

    private List<ArticleRowViewModel> feedArticleDataList;
    private int feedPage = DEFAULT_PAGINATION_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DataStorageHelper.getUserSettings().getCurrentTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recreateAfterThemeUpdate();
        initBottomNavigationMenu();
        setOnSearchClickListener();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initBottomNavigationMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = new FeedFragment();
        ((FeedFragment) currentFragment).setOnArticleEventListener(this, feedArticleDataList, feedPage);
        fragmentManager.beginTransaction().add(R.id.activityMainFrameLayout, currentFragment).addToBackStack(null).commit();

        binding.activityMainBottomNavigation.setItemIconTintList(null);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() != currentItemId) {
                currentItemId = item.getItemId();
                return updateMainFragment(item.getItemId());
            } else if (item.getItemId() == currentItemId && currentFragment instanceof FeedFragment) {
                ((FeedFragment)currentFragment).scrollUpToTopOfList();
            }
            return false;
        });
        binding.activityMainBottomNavigation.setSelectedItemId(fragmentId);
    }

    private void recreateAfterThemeUpdate() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            currentFragment = new SettingsFragment();
            fragmentId = R.id.action_settings;
            isRecreated = true;
        }
    }

    private Bundle getSettingsBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_ACTIVITY_RECREATED, isRecreated);
        return bundle;
    }

    private void setOnSearchClickListener() {
        binding.searchImageButton.setOnClickListener(view -> {
            Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intentSearch);
            overridePendingTransition(
                    getResources().getInteger(R.integer.no_animation),
                    getResources().getInteger(R.integer.no_animation));
        });
    }

    private Boolean updateMainFragment(Integer itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (itemId == R.id.action_categories) {
            currentFragment = new CategoriesFragment();
            setTopBarVisibility(View.VISIBLE);
        } else if (itemId == R.id.action_feed) {
            setTopBarVisibility(View.VISIBLE);
            currentFragment = new FeedFragment();
            ((FeedFragment) currentFragment).setOnArticleEventListener(this, this.feedArticleDataList, feedPage);
        } else if (itemId == R.id.action_settings) {
            setTopBarVisibility(View.GONE);
            currentFragment = new SettingsFragment();
            currentFragment.setArguments(getSettingsBundle());
            isRecreated = false;
        }
        fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, currentFragment).addToBackStack(null).commit();
        return true;
    }

    private void setTopBarVisibility(int visibility) {
        binding.cardViewTopBar.setVisibility(visibility);
    }

    public void rateUsButtonClicked(View rateUsButton) {
        rateUsButton.startAnimation(AnimationUtils.loadAnimation(rateUsButton.getContext(), R.anim.item_pressed_anim));
    }

    @Override
    public void onArticleUpdated(List<ArticleRowViewModel> feedArticleDataList, int page) {
        this.feedArticleDataList = feedArticleDataList;
        this.feedPage = page;
    }
}