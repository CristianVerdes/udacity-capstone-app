package com.example.cristianverdes.mylolhelper.ui.findsummoner;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.repositories.SummonerRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainSummoner;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindSummonerViewModel extends ViewModel {
    private static final String TAG = FindSummonerViewModel.class.getSimpleName();
    private SummonerRepository summonerRepository;
    private Disposable summonerDisposable;
    private MutableLiveData<DomainSummoner> summonerMutableLiveData;

    public FindSummonerViewModel(SummonerRepository summonerRepository) {
        this.summonerRepository = summonerRepository;
    }

    public void disposeObservables() {
        if (summonerDisposable != null) {
            summonerDisposable.dispose();
        }
    }

    private void loadData(String name) {
        summonerDisposable = summonerRepository.getSummoner(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DomainSummoner>() {
                    @Override
                    public void accept(DomainSummoner domainSummoner) {
                        summonerMutableLiveData.setValue(domainSummoner);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        summonerMutableLiveData.setValue(null);
                        Log.e(TAG, throwable.toString());
                    }
                });
    }

    public MutableLiveData<DomainSummoner> searchSummoner(String name) {
        if (summonerMutableLiveData == null) {
            summonerMutableLiveData = new MutableLiveData<>();
        }

        loadData(name);
        return summonerMutableLiveData;
    }
}