package com.pinalopes.informationspositives.home.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.Article;
import com.pinalopes.informationspositives.articles.ArticleComponent;
import com.pinalopes.informationspositives.articles.DaggerArticleComponent;
import com.pinalopes.informationspositives.databinding.ActivityMainBinding;
import com.pinalopes.informationspositives.editor.EditorModule;
import com.pinalopes.informationspositives.editor.SocialNetworks;

import javax.inject.Inject;

import io.sentry.Sentry;

public class MainActivity extends AppCompatActivity {

    @Inject
    Article article;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Sentry.captureMessage("testing SDK setup");

        ArticleComponent articleComponent = DaggerArticleComponent.builder().editorModule(new EditorModule(
               "NewMan",
               "Nicolas",
               "Nick",
               new SocialNetworks(
                       "nick@gmail.com",
                       "Nicolas Newman",
                       "n.newman",
                       "Nick Newman"
               ))).build();
        articleComponent.inject(this);
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

    private Boolean updateMainFragment(Integer integer) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (integer) {
            case R.id.action_categories:
                Fragment categoriesFragment = new CategoriesFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, categoriesFragment).commit();
                break;
            case R.id.action_feed:
                Fragment mapFragment = new FeedFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, mapFragment).commit();
                break;
            case R.id.action_settings:
                Fragment settingsFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.activityMainFrameLayout, settingsFragment).commit();
                break;
            default:
                break;
        }
        return true;
    }
}