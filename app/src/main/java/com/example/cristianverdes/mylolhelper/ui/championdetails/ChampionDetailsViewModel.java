package com.example.cristianverdes.mylolhelper.ui.championdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChampionDetailsViewModel extends ViewModel {
    private static final String TAG = ChampionDetailsViewModel.class.getSimpleName();
    private ChampionRepository championRepository;
    private Disposable championDisposable;

    private MutableLiveData<DomainChampion> championMutableLiveData;

    public ChampionDetailsViewModel(ChampionRepository championRepository) {
        this.championRepository = championRepository;
    }

    public void loadChampion(int id) {
        championDisposable = championRepository.getChampion(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DomainChampion>() {
                    @Override
                    public void accept(DomainChampion champion) throws Exception {
                        championMutableLiveData.setValue(champion);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
    }

    public void disposeObservables() {
        if (championDisposable != null) {
            championDisposable.dispose();
        }

    }

    //Getters
    public MutableLiveData<DomainChampion> getChampionMutableLiveData(int id) {
        if (championMutableLiveData == null) {
            championMutableLiveData = new MutableLiveData<DomainChampion>();
        }

        // Load Data
        loadChampion(id);
        return championMutableLiveData;
    }
}
