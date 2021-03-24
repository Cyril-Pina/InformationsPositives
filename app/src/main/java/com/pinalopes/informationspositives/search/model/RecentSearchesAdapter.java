package com.pinalopes.informationspositives.search.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.databinding.RecentSearchRowBinding;
import com.pinalopes.informationspositives.search.viewmodel.RecentSearchViewModel;
import com.pinalopes.informationspositives.storage.RecentSearch;

import java.util.List;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchesAdapter.RecentSearchesViewHolder>{

    private final List<RecentSearch> recentSearches;
    private final OnRecentSearchClickListener listener;

    public static class RecentSearchesViewHolder extends RecyclerView.ViewHolder {

        RecentSearchRowBinding binding;

        public RecentSearchesViewHolder(RecentSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RecentSearchViewModel recentSearchViewModel) {
            binding.setRecentSearchViewModel(recentSearchViewModel);
        }
    }

    public RecentSearchesAdapter(List<RecentSearch> recentSearches, OnRecentSearchClickListener listener) {
        this.recentSearches = recentSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentSearchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RecentSearchRowBinding binding = RecentSearchRowBinding.inflate(layoutInflater);
        RecentSearchesViewHolder holder = new RecentSearchesViewHolder(binding);
        binding.getRoot().setOnClickListener(v ->
                listener.setOnRecentSearchClick(recentSearches.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchesViewHolder holder, int position) {
        String articleSearched = recentSearches.get(position).articleSearched;
        RecentSearchViewModel recentSearchViewModel = new RecentSearchViewModel(articleSearched);
        holder.binding.deleteRecentSearchIcon.setOnClickListener(v ->
                listener.setOnDeleteRecentSearchClick(recentSearches.get(position), position));
        holder.bind(recentSearchViewModel);
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    public List<RecentSearch> getRecentSearches() {
        return recentSearches;
    }
}