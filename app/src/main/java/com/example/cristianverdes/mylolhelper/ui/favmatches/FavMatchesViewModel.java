package com.example.cristianverdes.mylolhelper.ui.favmatches;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.model.matches.Match;
import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavMatchesViewModel extends ViewModel {
    private static final String TAG = FavMatchesViewModel.class.getSimpleName();
    private MatchesRepository matchesRepository;
    private Disposable matchesDisposable;

    private MutableLiveData<List<DomainMatch>> matchesMutableLiveData;

    public FavMatchesViewModel(MatchesRepository matchesRepository) {
        this.matchesRepository = matchesRepository;
    }

    private void loadMatchesFromDb() {
        matchesDisposable = matchesRepository.getFavoriteMatches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DomainMatch>>() {
                    @Override
                    public void accept(List<DomainMatch> domainMatches) throws Exception {
                        matchesMutableLiveData.setValue(domainMatches);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "List of matches id empty");
                    }
                });
    }

    public void disposeObservables() {
        if (matchesDisposable != null) {
            matchesDisposable.dispose();
        }
    }

    // Getters
    public MutableLiveData<List<DomainMatch>> getMatchesMutableLiveData() {
        if (matchesMutableLiveData == null) {
            matchesMutableLiveData = new MutableLiveData<>();
        }

        // Load Data
        loadMatchesFromDb();
        return matchesMutableLiveData;
    }

    public void deleteFavoriteMatch(DomainMatch domainMatch) {
        matchesRepository.deleteFavoriteMatch(domainMatch);
    }
}
