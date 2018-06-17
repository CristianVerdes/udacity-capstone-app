package com.example.cristianverdes.mylolhelper.ui.allchampions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;
import com.example.cristianverdes.mylolhelper.utils.Injection;

public class AllChampionsFragment extends Fragment {
    private View rootView;
    private RecyclerView championsList;
    private ChampionsAdapter championsAdapter;

    private AllChampionsViewModel allChampionsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_champions, container, false);
        championsList = rootView.findViewById(R.id.rv_all_champions);

        // Create Champions Adapter
        championsAdapter = new ChampionsAdapter(true);
        championsList.setAdapter(championsAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        championsList.setLayoutManager(gridLayoutManager);

        createViewModel();

        subscribeToDataStream();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allChampionsViewModel.disposeObservables();
    }

    private void createViewModel() {
        // Create the Factory to be passed to the ViewModel.
        AllChampionsViewModelFactory allChampionsViewModelFactory = new AllChampionsViewModelFactory(Injection.provideChampionsRepository(getActivity()));

        // Create ViewModel
        allChampionsViewModel = ViewModelProviders.of(this, allChampionsViewModelFactory).get(AllChampionsViewModel.class);
    }

    private void subscribeToDataStream() {
        // Create the observer which updates the UI
        final Observer<DomainChampions> championsObserver = new Observer<DomainChampions>() {
            @Override
            public void onChanged(@Nullable DomainChampions champions) {
                hideProgressBar();
                championsAdapter.setChampionListItems(champions);
            }
        };

        // Observe the LiveData, pass the Fragment as the LifecycleOwner and the observer.
        allChampionsViewModel.getChampionsMutableLiveData().observe(this, championsObserver);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_all_champions);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
