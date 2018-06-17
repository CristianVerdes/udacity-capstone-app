package com.example.cristianverdes.mylolhelper.ui.championdetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionRepository;

public class ChampionDetailsViewModelFactory implements ViewModelProvider.Factory {
    private ChampionRepository championRepository;

    public ChampionDetailsViewModelFactory(ChampionRepository championRepository) {
        this.championRepository = championRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChampionDetailsViewModel.class)) {
            return (T) new ChampionDetailsViewModel(championRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
