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
import com.pinalopes.informationspositives.search.model.SearchActivity;
import com.pinalopes.informationspositives.settings.model.SettingsFragment;
import com.pinalopes.informationspositives.storage.DataStorage;

import static com.pinalopes.informationspositives.Constants.IS_ACTIVITY_RECREATED;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment firstFragment = new FeedFragment();
    private int fragmentId = R.id.action_feed;
    private boolean isRecreated = false;
    private int currentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DataStorage.getUserSettings().getCurrentTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recreateAfterThemeUpdate();
        initBottomNavigationMenu();
        setOnSearchClickListener();
    }

    private void initBottomNavigationMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.activityMainFrameLayout, firstFragment).commit();

        binding.activityMainBottomNavigation.setItemIconTintList(null);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() != currentItemId) {
                currentItemId = item.getItemId();
                return updateMainFragment(item.getItemId());
            }
            return false;
        });
        binding.activityMainBottomNavigation.setSelectedItemId(fragmentId);
    }

    private void recreateAfterThemeUpdate() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            firstFragment = new SettingsFragment();
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
        Fragment newFragment = new CategoriesFragment();
        if (itemId == R.id.action_categories) {
            setTopBarVisibility(View.VISIBLE);
        } else if (itemId == R.id.action_feed) {
            setTopBarVisibility(View.VISIBLE);
            newFragment = new FeedFragment();
        } else if (itemId == R.id.action_settings) {
            setTopBarVisibility(View.GONE);
            newFragment = new SettingsFragment();
            newFragment.setArguments(getSettingsBundle());
            isRecreated = false;
        }
        fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, newFragment).commit();
        return true;
    }

    private void setTopBarVisibility(int visibility) {
        binding.cardViewTopBar.setVisibility(visibility);
    }

    public void rateUsButtonClicked(View rateUsButton) {
        rateUsButton.startAnimation(AnimationUtils.loadAnimation(rateUsButton.getContext(), R.anim.item_pressed_anim));
    }
}