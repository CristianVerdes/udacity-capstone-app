package com.example.cristianverdes.mylolhelper.data.repositories;

import android.content.Context;

import com.example.cristianverdes.mylolhelper.data.model.summoner.Summoner;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.data.remote.RiotApiClient;
import com.example.cristianverdes.mylolhelper.domain.models.DomainSummoner;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SummonerRepository {
    private static SummonerRepository summonerRepository;

    private Observable<DomainSummoner> summonerObservable;

    public static SummonerRepository getInstance() {
        if (summonerRepository == null) {
            summonerRepository = new SummonerRepository();
        }
        return summonerRepository;
    }

    public Observable<DomainSummoner> getSummoner(String summonerName) {
        summonerObservable = RiotApiClient.getRiotApiService().getSummoner(summonerName, NetworkConstants.API_KEY)
                .map(new Function<Summoner, DomainSummoner>() {
                    @Override
                    public DomainSummoner apply(Summoner summoner) {
                        return new DomainSummoner(summoner);
                    }
                });
        
        return summonerObservable;
    }

}
