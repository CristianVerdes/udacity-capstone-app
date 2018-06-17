package com.example.cristianverdes.mylolhelper.ui.favmatches;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.example.cristianverdes.mylolhelper.ui.matches.MatchesAdapter;
import com.example.cristianverdes.mylolhelper.ui.matches.MatchesViewModel;
import com.example.cristianverdes.mylolhelper.ui.matches.MatchesViewModelFactory;
import com.example.cristianverdes.mylolhelper.utils.Injection;

import java.util.List;

public class FavMatchesFragment extends Fragment {
    private static final String TAG = FavMatchesViewModel.class.getSimpleName();
    private View rootView;
    private RecyclerView matchesList;
    private MatchesAdapter matchesAdapter;
    private FavMatchesViewModel favMatchesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fav_matches, container, false);
        matchesList = rootView.findViewById(R.id.rv_matches);

        // Create Matches Adapter
        matchesAdapter = new MatchesAdapter(false);
        matchesList.setAdapter(matchesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        matchesList.setLayoutManager(linearLayoutManager);

        createViewModel();

        subscribeToDataModel();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favMatchesViewModel.disposeObservables();
    }

    private void createViewModel() {
        // Create Factory to be passed to the ViewModel
        FavMatchesViewModelFactory matchesViewModelFactory =
                new FavMatchesViewModelFactory(Injection.provideMatchesRepository(getActivity()));

        // Create ViewModel
        favMatchesViewModel = ViewModelProviders
                .of(this, matchesViewModelFactory)
                .get(FavMatchesViewModel.class);

        // Pass VM to Adapter
        matchesAdapter.setFavMatchesViewModel(favMatchesViewModel);
    }

    private void subscribeToDataModel() {
        // Create observer which updates the UI
        final Observer<List<DomainMatch>> matchesObserver = new Observer<List<DomainMatch>>() {
            @Override
            public void onChanged(@Nullable List<DomainMatch> domainMatches) {
                if (domainMatches != null) {
                    // Set Data to Adapter Here
                    hideProgressBar();
                    matchesAdapter.setMatches(domainMatches);
                } else {
                    Log.e(TAG, "List of matches is empty.");
                }
            }
        };

        // Observe the LiveData, pass the Activity as the LifecycleOwner and the observer
        favMatchesViewModel.getMatchesMutableLiveData().observe(this, matchesObserver);

    }

    private void hideProgressBar() {
        ProgressBar progressBar = rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_matches);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
