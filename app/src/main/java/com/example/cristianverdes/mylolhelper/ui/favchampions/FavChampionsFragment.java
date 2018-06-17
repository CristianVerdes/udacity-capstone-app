package com.example.cristianverdes.mylolhelper.ui.favchampions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;
import com.example.cristianverdes.mylolhelper.ui.allchampions.ChampionsAdapter;
import com.example.cristianverdes.mylolhelper.utils.Injection;

import java.util.List;

public class FavChampionsFragment extends Fragment {
    private View rootView;
    private RecyclerView championsList;
    private ChampionsAdapter championsAdapter;
    private FavChampionsViewModel favChampionsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fav_champions, container, false);
        championsList = rootView.findViewById(R.id.rv_fav_champions);

        // Create Champion Adapter
        championsAdapter = new ChampionsAdapter(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        championsList.setAdapter(championsAdapter);
        championsList.setLayoutManager(gridLayoutManager);

        createViewModel();

        subscribeToDataStream();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favChampionsViewModel.disposeObservables();
    }

    private void createViewModel() {
        // Create Factory to be passed to the ViewModel
        FavChampionsViewModelFactory favChampionsViewModelFactory =
                new FavChampionsViewModelFactory(Injection.provideChampionsRepository(getActivity()));

        // Create ViewModel
        favChampionsViewModel = ViewModelProviders
                .of(this, favChampionsViewModelFactory)
                .get(FavChampionsViewModel.class);

        championsAdapter.setFavChampionsViewModel(favChampionsViewModel);
    }


    private void subscribeToDataStream() {
        // Create observer which updates the UI
        final Observer<DomainChampions> championsObserver = new Observer<DomainChampions>() {
            @Override
            public void onChanged(@Nullable DomainChampions champions) {
                hideProgressBar();
                championsAdapter.setChampionListItems(champions);
            }
        };

        favChampionsViewModel.getChampionsMutableLiveData().observe(this, championsObserver);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_fav_champions);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
