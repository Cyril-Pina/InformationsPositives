package com.pinalopes.informationspositives.settings.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pinalopes.informationspositives.databinding.AboutUsDialogFragmentBinding;
import com.pinalopes.informationspositives.settings.viewmodel.AboutUsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.sentry.Sentry;

import static com.pinalopes.informationspositives.Constants.DRAWABLE;
import static com.pinalopes.informationspositives.Constants.PACKAGE_NAME;
import static com.pinalopes.informationspositives.Constants.PREFIX_HEADER_IMAGE;
import static com.pinalopes.informationspositives.Constants.TOTAL_IMAGE_HEADER_ABOUT_US;
import static com.pinalopes.informationspositives.application.IPApplication.rand;

public class AboutUsDialogFragment extends DialogFragment {

    private static final String TAG = "AboutUsDialogFragment";

    private AboutUsDialogFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AboutUsDialogFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        initHeaderAboutUsGridView();
        setAppVersion(rootView.getContext());
        return rootView;
    }

    private void initHeaderAboutUsGridView() {
        List<Integer> headerImages = new ArrayList<>();

        while (headerImages.size() < TOTAL_IMAGE_HEADER_ABOUT_US) {
            int imageIndex = rand.nextInt(TOTAL_IMAGE_HEADER_ABOUT_US);
            String imageFileName = String.format(Locale.getDefault(), "%s%d", PREFIX_HEADER_IMAGE, imageIndex);
            int imageRes =  getResources().getIdentifier(imageFileName, DRAWABLE, PACKAGE_NAME);
            if (!headerImages.contains(imageRes)) {
                headerImages.add(imageRes);
            }
        }
        HeaderAboutUsAdapter adapter = new HeaderAboutUsAdapter(getContext(), headerImages);
        binding.headerAboutUsGridView.setAdapter(adapter);
    }

    private void setAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String appVersion;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                appVersion = String.format("v%s (%s)", packageInfo.versionName, packageInfo.getLongVersionCode());
            } else {
                appVersion = String.format("v%s (%s)", packageInfo.versionName, packageInfo.versionCode);
            }
            binding.setAboutUseViewModel(new AboutUsViewModel(appVersion));
        } catch (PackageManager.NameNotFoundException nnfe) {
            Log.e(TAG, nnfe.toString());
            Sentry.captureException(nnfe);
        }
    }
}
