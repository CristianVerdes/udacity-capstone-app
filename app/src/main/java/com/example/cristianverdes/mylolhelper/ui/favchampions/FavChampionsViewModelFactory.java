package com.example.cristianverdes.mylolhelper.ui.favchampions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;

public class FavChampionsViewModelFactory implements ViewModelProvider.Factory {
    private ChampionsRepository championsRepository;

    public FavChampionsViewModelFactory(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavChampionsViewModel.class)) {
            return (T) new FavChampionsViewModel(championsRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
