package com.pinalopes.informationspositives.feed.model;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pinalopes.informationspositives.LoadingService;
import com.pinalopes.informationspositives.databinding.FeedFragmentBinding;
import com.pinalopes.informationspositives.feed.dagger.DaggerFeedComponent;
import com.pinalopes.informationspositives.feed.dagger.LoadingModule;

import javax.inject.Inject;

public class FeedFragment extends Fragment {

    private static final String TAG = "FeedFragment";

    @Inject LoadingService loadingService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        FeedFragmentBinding binding = FeedFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        DaggerFeedComponent
                .builder()
                .loadingModule(new LoadingModule(rootView.getContext(), binding.loadingMainLayout, binding.loadingImageView))
                .build()
                .inject(this);
        return rootView;
    }
}