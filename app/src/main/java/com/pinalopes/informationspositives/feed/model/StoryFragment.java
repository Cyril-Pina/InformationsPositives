package com.pinalopes.informationspositives.feed.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.databinding.StoryFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class StoryFragment extends Fragment {

    private StoryFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StoryFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        initStoriesRecyclerView(rootView.getContext());
        return rootView;
    }

    private void initStoriesRecyclerView(Context context) {
        RecyclerView storiesRecyclerView = binding.storiesRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        List<String> test = new ArrayList<>();

        test.add("deecz");
        test.add("suuu");
        test.add("dedecdz");
        test.add("deezcdz");
        test.add("deecxadz");
        test.add("deecdxz");
        test.add("deecdfz");
        test.add("deecdlz");
        test.add("deecdpz");
        test.add("deecdzo");

        storiesRecyclerView.setLayoutManager(layoutManager);
        StoryRecyclerAdapter adapter = new StoryRecyclerAdapter(test);
        storiesRecyclerView.setAdapter(adapter);
    }
}
