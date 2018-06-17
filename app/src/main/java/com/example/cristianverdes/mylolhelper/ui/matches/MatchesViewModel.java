package com.example.cristianverdes.mylolhelper.ui.matches;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.ScrollView;

import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MatchesViewModel extends ViewModel {
    private static final String TAG = MatchesViewModel.class.getSimpleName();
    private MatchesRepository matchesRepository;
    private Disposable matchesDisposable;

    private MutableLiveData<List<DomainMatch>> matchesMutableLiveData;

    public MatchesViewModel(MatchesRepository matchesRepository) {
        this.matchesRepository =  matchesRepository;
    }

    private void loadMatches(String summonerId) {
        matchesDisposable = matchesRepository.getMatches(summonerId)
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
                        Log.e(TAG, throwable.toString());
                    }
                });
    }

    public void disposeObservables() {
        if (matchesDisposable != null) {
            matchesDisposable.dispose();
        }
    }

    // Getters
    public MutableLiveData<List<DomainMatch>> getMatchesMutableLiveData(String summonerId) {
        if (matchesMutableLiveData == null) {
            matchesMutableLiveData = new MutableLiveData<>();
        }

        // Load Data
        loadMatches(summonerId);
        return matchesMutableLiveData;
    }
}
