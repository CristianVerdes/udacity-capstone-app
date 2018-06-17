package com.example.cristianverdes.mylolhelper.ui.favmatches;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;

public class FavMatchesViewModelFactory implements ViewModelProvider.Factory {
    private MatchesRepository matchesRepository;

    public FavMatchesViewModelFactory(MatchesRepository matchesRepository) {
        this.matchesRepository = matchesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavMatchesViewModel.class)) {
            return (T) new FavMatchesViewModel(matchesRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
