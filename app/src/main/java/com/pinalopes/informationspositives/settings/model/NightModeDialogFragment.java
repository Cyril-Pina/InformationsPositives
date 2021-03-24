package com.pinalopes.informationspositives.settings.model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.NightModeAlertFragmentBinding;
import com.pinalopes.informationspositives.storage.DataStorageHelper;

import static com.pinalopes.informationspositives.Constants.DAY_MODE;
import static com.pinalopes.informationspositives.Constants.DAY_MODE_INITIAL_FRAME;
import static com.pinalopes.informationspositives.Constants.DAY_MODE_MAX_FRAME;
import static com.pinalopes.informationspositives.Constants.NIGHT_MODE;
import static com.pinalopes.informationspositives.Constants.NIGHT_MODE_INITIAL_FRAME;
import static com.pinalopes.informationspositives.Constants.NIGHT_MODE_MAX_FRAME;
import static com.pinalopes.informationspositives.Constants.UPDATE_THEME_EXTRA;

public class NightModeDialogFragment extends DialogFragment {

    private NightModeAlertFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NightModeAlertFragmentBinding.inflate(inflater, container, false);
        setBackgroundTransparent();
        initNightModeIconSwitch();
        initNightModeAnimFrame();
        setOnClosePopUpClickListener();
        return binding.getRoot();
    }

    private void setBackgroundTransparent() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void initNightModeIconSwitch() {
        int currentThemeId = DataStorageHelper.getUserSettings().getCurrentTheme();
        if (currentThemeId == R.style.AppTheme_Dark_NoActionBar) {
            binding.nightModeSwitch.setChecked(NIGHT_MODE);
            binding.nightModeAnimation.setFrame(NIGHT_MODE_MAX_FRAME);
        }
    }

    private void initNightModeAnimFrame() {
        binding.nightModeSwitch.setCheckedChangeListener(current -> {
            if (current == NIGHT_MODE) {
                binding.nightModeAnimation.setFrame(NIGHT_MODE_INITIAL_FRAME);
                binding.nightModeAnimation.setMinFrame(NIGHT_MODE_INITIAL_FRAME);
                binding.nightModeAnimation.setMaxFrame(NIGHT_MODE_MAX_FRAME);
                binding.nightModeAnimation.playAnimation();
            } else if (current == DAY_MODE) {
                binding.nightModeAnimation.setFrame(DAY_MODE_INITIAL_FRAME);
                binding.nightModeAnimation.setMaxFrame(DAY_MODE_MAX_FRAME);
                binding.nightModeAnimation.setMinFrame(DAY_MODE_INITIAL_FRAME);
                binding.nightModeAnimation.playAnimation();
            }
            DataStorageHelper.updateCurrentTheme(binding.getRoot().getContext());
            requireActivity().getIntent().putExtra(UPDATE_THEME_EXTRA, "");
            requireActivity().recreate();
        });
    }

    private void setOnClosePopUpClickListener() {
        binding.closePopUpButton.setOnClickListener(v -> dismiss());
        binding.nightModeBackgroundContainer.setOnClickListener(v -> dismiss());
    }
}
