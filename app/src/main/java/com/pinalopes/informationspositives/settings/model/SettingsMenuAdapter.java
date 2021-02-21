package com.pinalopes.informationspositives.settings.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.databinding.SettingsMenuEmptyItemBinding;
import com.pinalopes.informationspositives.databinding.SettingsMenuItemBinding;
import com.pinalopes.informationspositives.settings.viewmodel.SettingsMenuItemViewModel;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.FIRST_INDEX;
import static com.pinalopes.informationspositives.Constants.TYPE_HEADER;
import static com.pinalopes.informationspositives.Constants.TYPE_ITEM;

public class SettingsMenuAdapter extends RecyclerView.Adapter<SettingsMenuAdapter.SettingsMenuViewHolder> {

    private final List<SettingsMenuItemViewModel> settingsMenuItems;
    private final OnSettingsMenuClickListener listener;
    private final CompoundButton.OnCheckedChangeListener switchListener;

    public static class SettingsMenuViewHolder extends RecyclerView.ViewHolder {

        SettingsMenuItemBinding binding;
        SettingsMenuEmptyItemBinding emptyBinding;

        public SettingsMenuViewHolder(SettingsMenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public SettingsMenuViewHolder(SettingsMenuEmptyItemBinding emptyBinding) {
            super(emptyBinding.getRoot());
            this.emptyBinding = emptyBinding;
        }

        public void bind(SettingsMenuItemViewModel settingsMenuItemViewModel) {
            binding.setSettingsMenuItem(settingsMenuItemViewModel);
        }
    }

    public SettingsMenuAdapter(List<SettingsMenuItemViewModel> settingsMenuItems, OnSettingsMenuClickListener listener, CompoundButton.OnCheckedChangeListener switchListener) {
        this.settingsMenuItems = settingsMenuItems;
        this.listener = listener;
        this.switchListener = switchListener;
    }

    @NonNull
    @Override
    public SettingsMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            SettingsMenuEmptyItemBinding binding = SettingsMenuEmptyItemBinding.inflate(layoutInflater, parent, false);
            return new SettingsMenuViewHolder(binding);
        } else {
            SettingsMenuItemBinding binding = SettingsMenuItemBinding.inflate(layoutInflater, parent, false);
            SettingsMenuViewHolder holder = new SettingsMenuViewHolder(binding);
            holder.binding.notificationsSwitch.setOnCheckedChangeListener(switchListener);
            binding.getRoot().setOnClickListener(v -> listener.onMenuItemSelected(holder.getAdapterPosition(), binding.notificationsSwitch));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsMenuViewHolder holder, int position) {
        if (position != FIRST_INDEX) {
            holder.bind(settingsMenuItems.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == FIRST_INDEX) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return settingsMenuItems.size();
    }

}
