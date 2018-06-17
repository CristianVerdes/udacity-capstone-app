package com.example.cristianverdes.mylolhelper.ui.searchchamp;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchChampionViewModel extends ViewModel {
    private static final String TAG = SearchChampionViewModel.class.getSimpleName();

    private ChampionsRepository championsRepository;
    private MutableLiveData<ChampionListItem> championMutableLiveData;
    private Disposable allChampionsDisposable;
    private boolean championFound = false;

    public SearchChampionViewModel(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    // Dispose Observable
    public void disposeObservables() {
        if (allChampionsDisposable != null) {
            allChampionsDisposable.dispose();
        }
    }

    // Getters
    public MutableLiveData<ChampionListItem> getChampionMutableLiveData(String searchedChampionName) {
        if (championMutableLiveData == null) {
            championMutableLiveData = new MutableLiveData<>();
        }
        // Load Data
        getSearchedChampion(searchedChampionName);
        return championMutableLiveData;
    }

    private void getSearchedChampion(final String searchedChampionName) {
        allChampionsDisposable = championsRepository.getChampions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DomainChampions>() {
                    @Override
                    public void accept(DomainChampions domainChampions) {
                        for (ChampionListItem championListItem: domainChampions.getChampionsArrayList()) {
                            if (championListItem.getName().toLowerCase().equals(searchedChampionName.toLowerCase())) {
                                championMutableLiveData.setValue(championListItem);
                                championFound = true;
                            }
                        }

                        if (!championFound) {
                            championMutableLiveData.setValue(ChampionListItem.nullValue);
                        }

                        championFound = false;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, "Getting champion list error. ");
                    }
                });
    }
}
