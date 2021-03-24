package com.pinalopes.informationspositives.network.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.NetworkErrorFragmentBinding;
import com.pinalopes.informationspositives.network.viewmodel.NetworkErrorViewModel;

import static com.pinalopes.informationspositives.Constants.ANIM_NO_REPEAT;
import static com.pinalopes.informationspositives.Constants.IS_RELOAD_DATA_ENABLED;
import static com.pinalopes.informationspositives.Constants.NETWORK_ERROR_CAUSE;
import static com.pinalopes.informationspositives.Constants.NETWORK_ERR_ANIM_MAX_FRAME;
import static com.pinalopes.informationspositives.Constants.NETWORK_ERR_ANIM_REVERSE_FRAME;
import static com.pinalopes.informationspositives.Constants.NETWORK_ERR_ANIM_REVERSE_MAX_FRAME;

public class NetworkErrorFragment extends Fragment {

    private NetworkErrorFragmentBinding binding;
    private OnNetworkErrorEventListener listener;

    public interface OnNetworkErrorEventListener {
        void onRefreshButtonPressed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NetworkErrorFragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        binding.setNetworkErrorViewModel(
                new ViewModelProvider((ViewModelStoreOwner) rootView.getContext()).get(NetworkErrorViewModel.class));
        initNetworkErrorViews();
        return rootView;
    }

    public static NetworkErrorFragment newInstance(String cause, boolean isReloadDataEnabled) {
        NetworkErrorFragment networkErrorFragment = new NetworkErrorFragment();
        Bundle args = new Bundle();
        args.putString(NETWORK_ERROR_CAUSE, cause);
        args.putBoolean(IS_RELOAD_DATA_ENABLED, isReloadDataEnabled);
        networkErrorFragment.setArguments(args);
        return networkErrorFragment;
    }

    public void setOnNetworkErrorEventListener(OnNetworkErrorEventListener listener) {
        this.listener = listener;
    }

    private void initNetworkErrorViews() {
        if (getArguments() != null) {
            String cause = getArguments().getString(NETWORK_ERROR_CAUSE, getString(R.string.no_network_connection));
            boolean isReloadDataEnabled = getArguments().getBoolean(IS_RELOAD_DATA_ENABLED, false);
            binding.getNetworkErrorViewModel().setNetworkErrorData(cause, isReloadDataEnabled);
        }
        binding.feedAnimation.setMaxFrame(NETWORK_ERR_ANIM_MAX_FRAME);
        binding.feedAnimation.setRepeatCount(ANIM_NO_REPEAT);
        binding.feedAnimation.playAnimation();
        binding.refreshDataButton.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.item_pressed_anim));
            listener.onRefreshButtonPressed();
            binding.feedAnimation.setMaxFrame(NETWORK_ERR_ANIM_REVERSE_MAX_FRAME);
            binding.feedAnimation.setMinFrame(NETWORK_ERR_ANIM_REVERSE_FRAME);
            binding.feedAnimation.playAnimation();
        });
    }
}
