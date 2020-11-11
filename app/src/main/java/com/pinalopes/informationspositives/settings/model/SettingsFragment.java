package com.pinalopes.informationspositives.settings.model;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pinalopes.informationspositives.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";    

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        SettingsFragmentBinding binding = SettingsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
