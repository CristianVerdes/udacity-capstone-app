package com.example.cristianverdes.mylolhelper.ui.allchampions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;

public class AllChampionsViewModelFactory implements ViewModelProvider.Factory {
    private final ChampionsRepository championsRepository;

    public AllChampionsViewModelFactory(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AllChampionsViewModel.class)) {
            return (T) new AllChampionsViewModel(championsRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
