package com.example.cristianverdes.mylolhelper.ui.allchampions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AllChampionsViewModel extends ViewModel {
    private static final String TAG = AllChampionsViewModel.class.getSimpleName();

    private ChampionsRepository championsRepository;
    private MutableLiveData<DomainChampions> championsMutableLiveData;
    private Disposable allChampionsDisposable;

    public AllChampionsViewModel(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    // Get data from Repository
    public void loadAllChampions() {
        allChampionsDisposable = championsRepository.getChampions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DomainChampions>() {
                    @Override
                    public void accept(DomainChampions champions) throws Exception {
                        championsMutableLiveData.setValue(champions);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
    }


    // Dispose Observable
    public void disposeObservables() {
        if (allChampionsDisposable != null) {
            allChampionsDisposable.dispose();
        }
    }

    // Getters
    public MutableLiveData<DomainChampions> getChampionsMutableLiveData() {
        if (championsMutableLiveData == null) {
            championsMutableLiveData = new MutableLiveData<DomainChampions>();
        }

        // Load Data
        loadAllChampions();
        return championsMutableLiveData;
    }


}
