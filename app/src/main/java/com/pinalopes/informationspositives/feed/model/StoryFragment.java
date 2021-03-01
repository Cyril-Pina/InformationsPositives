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

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.databinding.StoryFragmentBinding;
import com.pinalopes.informationspositives.feed.viewmodel.StoryViewModel;

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
        List<StoryViewModel> storiesDataList = new ArrayList<>();

        storiesDataList.add(new StoryViewModel(R.drawable.puppy, "Un chiot sauvé miraculeusement par un jeune homme dans le département de Tarn"));
        storiesDataList.add(new StoryViewModel(R.drawable.homeless, "La mairie de Nice fait signé un contrat de travail à un sans-abri bienfaiteur"));
        storiesDataList.add(new StoryViewModel(R.drawable.plant, "Une technique de l'Antiquité utilisée pour la semis des graines en Gironde"));
        storiesDataList.add(new StoryViewModel(R.drawable.shoes, "Une chaussure à partir de liège, de maïs et de riz a vue le jour"));
        storiesDataList.add(new StoryViewModel(R.drawable.car, "Un moyen trouvé pour recyclé les gaz à effet de serre grâce aux lapins"));
        storiesDataList.add(new StoryViewModel(R.drawable.cat, "L'application pour comprendre votre chat est en cours de développement"));

        storiesRecyclerView.setLayoutManager(layoutManager);
        StoryRecyclerAdapter adapter = new StoryRecyclerAdapter(storiesDataList);
        storiesRecyclerView.setAdapter(adapter);
    }
}
