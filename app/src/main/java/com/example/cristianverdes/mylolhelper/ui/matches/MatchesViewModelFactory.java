package com.example.cristianverdes.mylolhelper.ui.matches;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsViewModel;

public class MatchesViewModelFactory implements ViewModelProvider.Factory {
    private MatchesRepository matchesRepository;

    public MatchesViewModelFactory(MatchesRepository matchesRepository) {
        this.matchesRepository = matchesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MatchesViewModel.class)) {
            return (T) new MatchesViewModel(matchesRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
