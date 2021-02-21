package com.pinalopes.informationspositives.settings.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pinalopes.informationspositives.databinding.HeaderAboutUsRowBinding;
import com.pinalopes.informationspositives.settings.viewmodel.HeaderAboutUsViewModel;

import java.util.List;

public class HeaderAboutUsAdapter extends BaseAdapter {

    private final Context context;
    private final List<Integer> headerImages;

    public HeaderAboutUsAdapter(Context context, List<Integer> headerImages) {
        this.context = context;
        this.headerImages = headerImages;
    }

    @Override
    public int getCount() {
        return headerImages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        HeaderAboutUsRowBinding binding = HeaderAboutUsRowBinding.inflate(((Activity) context).getLayoutInflater());
        binding.setHeaderAboutUsViewModel(new HeaderAboutUsViewModel(headerImages.get(position)));
        return binding.getRoot();
    }
}
