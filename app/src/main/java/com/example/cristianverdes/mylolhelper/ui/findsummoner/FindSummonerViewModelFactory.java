package com.example.cristianverdes.mylolhelper.ui.findsummoner;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.cristianverdes.mylolhelper.data.repositories.SummonerRepository;

public class FindSummonerViewModelFactory implements ViewModelProvider.Factory {
    private final SummonerRepository summonerRepository;

    public FindSummonerViewModelFactory(SummonerRepository summonerRepository) {
        this.summonerRepository = summonerRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(FindSummonerViewModel.class)) {
            return (T) new FindSummonerViewModel(summonerRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
