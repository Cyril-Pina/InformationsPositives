package com.pinalopes.informationspositives.home.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.CategoriesFragment;
import com.pinalopes.informationspositives.databinding.ActivityMainBinding;
import com.pinalopes.informationspositives.feed.model.FeedFragment;
import com.pinalopes.informationspositives.search.model.SearchActivity;
import com.pinalopes.informationspositives.settings.model.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBottomNavigationMenu();
        setOnSearchClickListener();
    }

    private void initBottomNavigationMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment feedFragment = new FeedFragment();
        fragmentManager.beginTransaction().add(R.id.activityMainFrameLayout, feedFragment).commit();

        binding.activityMainBottomNavigation.setItemIconTintList(null);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
        binding.activityMainBottomNavigation.setSelectedItemId(R.id.action_feed);
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
        }
        fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, newFragment).commit();
        return true;
    }

    private void setTopBarVisibility(int visibility) {
        binding.cardViewTopBar.setVisibility(visibility);
    }
}