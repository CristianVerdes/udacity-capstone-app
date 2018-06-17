package com.example.cristianverdes.mylolhelper.ui.searchchamp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;

public class SearchChampionViewModelFactory implements ViewModelProvider.Factory {
    private ChampionsRepository championsRepository;

    public SearchChampionViewModelFactory(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchChampionViewModel.class)) {
            return (T) new SearchChampionViewModel(championsRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
