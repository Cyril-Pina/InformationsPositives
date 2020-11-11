package com.pinalopes.informationspositives.home.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.CategoriesFragment;
import com.pinalopes.informationspositives.databinding.ActivityMainBinding;
import com.pinalopes.informationspositives.feed.model.FeedFragment;
import com.pinalopes.informationspositives.settings.model.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBottomNavigationMenu();
    }

    private void initBottomNavigationMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mapFragment = new FeedFragment();
        fragmentManager.beginTransaction().add(R.id.activityMainFrameLayout, mapFragment).commit();

        binding.activityMainBottomNavigation.setItemIconTintList(null);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
        binding.activityMainBottomNavigation.setSelectedItemId(R.id.action_feed);
    }

    private Boolean updateMainFragment(Integer itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (itemId) {
            case R.id.action_categories:
                setTopBarVisibility(View.VISIBLE);
                Fragment categoriesFragment = new CategoriesFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, categoriesFragment).commit();
                break;
            case R.id.action_feed:
                setTopBarVisibility(View.VISIBLE);
                Fragment mapFragment = new FeedFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, mapFragment).commit();
                break;
            case R.id.action_settings:
                setTopBarVisibility(View.GONE);
                Fragment settingsFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, settingsFragment).commit();
                break;
            default:
                break;
        }
        return true;
    }

    private void setTopBarVisibility(int visibility) {
        binding.cardViewTopBar.setVisibility(visibility);
    }
}