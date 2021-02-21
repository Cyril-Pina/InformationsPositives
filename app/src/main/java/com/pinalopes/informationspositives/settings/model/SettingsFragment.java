package com.pinalopes.informationspositives.settings.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.SettingsFragmentBinding;
import com.pinalopes.informationspositives.settings.viewmodel.SettingsMenuItemViewModel;
import com.pinalopes.informationspositives.settings.viewmodel.TopCategoriesViewModel;
import com.pinalopes.informationspositives.storage.DataStorage;
import com.pinalopes.informationspositives.utils.AdapterUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pinalopes.informationspositives.Constants.ABOUT_US_ALERT_DIALOG;
import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.IS_ACTIVITY_RECREATED;
import static com.pinalopes.informationspositives.Constants.ITEM_ABOUT_US_INDEX;
import static com.pinalopes.informationspositives.Constants.ITEM_CONTACT_US_INDEX;
import static com.pinalopes.informationspositives.Constants.ITEM_NIGHT_MODE_INDEX;
import static com.pinalopes.informationspositives.Constants.ITEM_NOTIFICATIONS_INDEX;
import static com.pinalopes.informationspositives.Constants.ITEM_OTHER_APPS_INDEX;
import static com.pinalopes.informationspositives.Constants.MAX_CATEGORIES_SELECTED;
import static com.pinalopes.informationspositives.Constants.NIGHT_MODE_ALERT_DIALOG;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;
import static com.pinalopes.informationspositives.Constants.PLAIN_TEXT;
import static com.pinalopes.informationspositives.application.IPApplication.notificationsBroadcastReceiver;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";    

    private SettingsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        showNightModePopUpAfterRecreate();
        initTopCategoriesFilterAdapter(rootView);
        initSettingsMenu(rootView.getContext());
        return rootView;
    }

    private void showNightModePopUpAfterRecreate() {
        if (getArguments() != null && getArguments().getBoolean(IS_ACTIVITY_RECREATED)) {
            showNightModePopUp();
        }
    }

    private void initTopCategoriesFilterAdapter(View view) {
        TopCategoriesAdapter adapter = new TopCategoriesAdapter(view.getContext(),
                getSavedSelectedCategories(), AdapterUtils.getFilterCategories(view.getContext()),
                getOnCategoriesSelectedListener(view.getContext(), getSavedSelectedCategories().size()));
        binding.topCategoriesGridView.setAdapter(adapter);
    }

    private void initSettingsMenu(Context context) {
        RecyclerView settingsRecyclerView = binding.settingsRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        String[] settingsMenuItems = getResources().getStringArray(R.array.settings_menu_items);
        String[] settingsMenuIcons = getResources().getStringArray(R.array.settings_menu_icons);
        List<SettingsMenuItemViewModel> menuItems = new ArrayList<>();

        for (int i = 0 ; i != settingsMenuItems.length ; i++) {
            String settingsMenuItem = settingsMenuItems[i];
            int settingsMenuIcon =  getResources().getIdentifier(settingsMenuIcons[i], DRAWABLE, PACKAGE_NAME);
            boolean isSwitchable = settingsMenuItem.equals(getString(R.string.notifications_item));
            boolean isChecked = isSavedNotificationsEnabled();
            menuItems.add(new SettingsMenuItemViewModel(settingsMenuItem, settingsMenuIcon, isSwitchable, isChecked));
        }

        settingsRecyclerView.setLayoutManager(layoutManager);
        SettingsMenuAdapter adapter = new SettingsMenuAdapter(menuItems,
                getOnSettingsMenuClickListener(context), getOnCheckedChangeListener(context));
        settingsRecyclerView.setAdapter(adapter);
        binding.settingsRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    private OnCategoriesClickListener getOnCategoriesSelectedListener(Context context, int savedTotalCategoriesSelected) {
        binding.setTopCategoriesViewModel(getTopCategoriesViewModelInstance(savedTotalCategoriesSelected));
        return (idCategory, isSelected, totalCategoriesSelected) -> {
            DataStorage.updateSelectedCategories(context, idCategory, isSelected);
            binding.setTopCategoriesViewModel(getTopCategoriesViewModelInstance(totalCategoriesSelected));
        };
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(Context context) {
        return (notificationSwitch, isChecked) -> {
            notificationsManager(context, (SwitchCompat) notificationSwitch, isChecked);
            DataStorage.updateNotificationsState(context);
        };
    }

    private OnSettingsMenuClickListener getOnSettingsMenuClickListener(Context context) {
        return (position, notificationSwitch) -> {
            switch (position) {
                case ITEM_NOTIFICATIONS_INDEX:
                    boolean isNotificationEnabled = !notificationSwitch.isChecked();
                    notificationsManager(context, notificationSwitch, isNotificationEnabled);
                    DataStorage.updateNotificationsState(context);
                    break;
                case ITEM_NIGHT_MODE_INDEX:
                    showNightModePopUp();
                    break;
                case ITEM_CONTACT_US_INDEX:
                    prepareForEmail();
                    break;
                case ITEM_OTHER_APPS_INDEX:
                    Log.d("Item other apps", "clicked");
                    break;
                case ITEM_ABOUT_US_INDEX:
                    showAboutUsPopUp();
                    break;
                default:
                    break;
            }
        };
    }

    private List<Integer> getSavedSelectedCategories() {
        return DataStorage.getUserSettings().getCategoriesSelected();
    }

    private boolean isSavedNotificationsEnabled() {
        return DataStorage.getUserSettings().isNotificationsEnabled();
    }

    private void notificationsManager(Context context, SwitchCompat notificationSwitch, boolean isNotificationEnable) {
        if (isNotificationEnable) {
            notificationSwitch.setChecked(true);
            notificationsBroadcastReceiver.setNotificationsAlarm(context);
        } else {
            notificationSwitch.setChecked(false);
            notificationsBroadcastReceiver.cancelNotificationsAlarm(context);
        }
    }

    private void prepareForEmail() {
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setType(PLAIN_TEXT);
        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.email_ip) });
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email));
        startActivity(Intent.createChooser(intentEmail, getString(R.string.chooser_title_email)));
    }

    private void showNightModePopUp() {
        FragmentManager fragmentManager = getChildFragmentManager();
        NightModeDialogFragment dialogFragment = new NightModeDialogFragment();
        dialogFragment.show(fragmentManager, NIGHT_MODE_ALERT_DIALOG);
    }

    private void showAboutUsPopUp() {
        FragmentManager fragmentManager = getChildFragmentManager();
        AboutUsDialogFragment dialogFragment = new AboutUsDialogFragment();
        dialogFragment.show(fragmentManager, ABOUT_US_ALERT_DIALOG);
    }

    private TopCategoriesViewModel getTopCategoriesViewModelInstance(int totalCategoriesSelected) {
        String totalTopCategoriesSelected = getResources().getQuantityString(R.plurals.number_top_categories_selected, totalCategoriesSelected, totalCategoriesSelected);
        int color = getColorTopCategoriesFromSelected(totalCategoriesSelected);
        int style = getStyleTopCategoriesFromSelected(totalCategoriesSelected);
        return new TopCategoriesViewModel(totalTopCategoriesSelected, ContextCompat.getColor(binding.getRoot().getContext(), color), style);
    }

    private int getColorTopCategoriesFromSelected(int totalCategoriesSelected) {
        return totalCategoriesSelected >= MAX_CATEGORIES_SELECTED ? R.color.colorPrimary : android.R.color.darker_gray;
    }

    private int getStyleTopCategoriesFromSelected(int totalCategoriesSelected) {
        return totalCategoriesSelected >= MAX_CATEGORIES_SELECTED ? Typeface.BOLD : Typeface.NORMAL;
    }
}
