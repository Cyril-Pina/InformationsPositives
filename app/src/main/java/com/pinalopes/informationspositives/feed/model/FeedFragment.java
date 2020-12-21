package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.LoadingService;
import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.categories.model.Category;
import com.pinalopes.informationspositives.databinding.FeedFragmentBinding;
import com.pinalopes.informationspositives.feed.dagger.DaggerFeedComponent;
import com.pinalopes.informationspositives.feed.dagger.LoadingModule;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FeedFragment extends Fragment {

    @Inject LoadingService loadingService;
    private FeedFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FeedFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        DaggerFeedComponent
                .builder()
                .loadingModule(new LoadingModule(rootView.getContext(), binding.loadingMainLayout, binding.loadingImageView))
                .build()
                .inject(this);
        initStoriesFragment((FragmentActivity) rootView.getContext());
        initFeedRecyclerView(rootView.getContext());
        return rootView;
    }

    private void initStoriesFragment(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment mapFragment = new StoryFragment();
        fragmentManager.beginTransaction().add(R.id.storiesFrameLayout, mapFragment).commit();
    }

    private void initFeedRecyclerView(Context context) {
        RecyclerView feedRecyclerView = binding.feedRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        List<ArticleRowViewModel> test = new ArrayList<>();

        Category category = new Category("Nature", 0, R.drawable.ic_nature_black);
        Category categoryFauna = new Category("Nature", 0, R.drawable.ic_fauna_black);
        Category categoryFood = new Category("Nature", 0, R.drawable.ic_food_black);

        test.add(new ArticleRowViewModel("Un chiot est sauver par Gaston du PMU", "18:25-12/12/2020", "Michel Jaqueson", category, 1802, 235, true));
        test.add(new ArticleRowViewModel("Sangoku a encore sauvé la terre top", "16:10-12/12/2020", "Miky Mike", category, 36820, 13, true));
        test.add(new ArticleRowViewModel("Macron donne 1million d'euros à un jeune sans abri", "08:02-12/12/2020", "Michel Jaqueson", categoryFauna, 36974, 13, false));
        test.add(new ArticleRowViewModel("Oui, la news plus haute est vraie", "08:01-12/12/2020", "Brigitte Bardot", categoryFood, 1859265, 483, false));

        feedRecyclerView.setLayoutManager(layoutManager);
        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter(test);
        feedRecyclerView.setAdapter(adapter);
    }
}