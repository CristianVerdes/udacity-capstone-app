package com.example.cristianverdes.mylolhelper.ui.favchampions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FavChampionsViewModel extends ViewModel {
    private static final String TAG = FavChampionsViewModel.class.getSimpleName();
    private ChampionsRepository championsRepository;
    private Disposable championsDisposable;

    private MutableLiveData<DomainChampions> championsMutableLiveData;

    public FavChampionsViewModel(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    private void loadChampionsFromDb() {
        championsDisposable = championsRepository.getFavoriteChampions()
                .subscribe(new Consumer<DomainChampions>() {
                    @Override
                    public void accept(DomainChampions domainChampions) {
                        championsMutableLiveData.setValue(domainChampions);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, "List of Favorite Champions is empty");
                    }
                });
    }

    public void disposeObservables() {
        if (championsDisposable != null) {
            championsDisposable.dispose();
        }
    }

    public MutableLiveData<DomainChampions> getChampionsMutableLiveData() {
        if (championsMutableLiveData == null) {
            championsMutableLiveData = new MutableLiveData<>();
        }

        // Load data
        loadChampionsFromDb();
        return championsMutableLiveData;
    }

    public void deleteFavoriteChampion(ChampionListItem championListItem) {
        championsRepository.deleteFavoriteChampion(championListItem);
    }

}
