package com.example.cristianverdes.mylolhelper.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.example.cristianverdes.mylolhelper.data.local.CacheChampionTask;
import com.example.cristianverdes.mylolhelper.data.local.DbHelper;
import com.example.cristianverdes.mylolhelper.data.local.DbHelperContract;
import com.example.cristianverdes.mylolhelper.data.model.champion.Champion;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.data.remote.RiotApiClient;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ChampionRepository {
    private static ChampionRepository championRepository;
    private WeakReference<Context> contextWeakReference;
    private Observable<DomainChampion> championObservable;

    private ChampionRepository(Context context) {
        contextWeakReference = new WeakReference<>(context);

    }

    public static ChampionRepository getInstance(Context context) {
        if (championRepository == null) {
            championRepository = new ChampionRepository(context);
            return championRepository;
        } else {
            return championRepository;
        }
    }

    // Getters
    public Observable<DomainChampion> getChampion(final int id) {
        return Observable.just(true)
                .map(new Function<Boolean, DomainChampion>() {
                    @Override
                    public DomainChampion apply(Boolean aBoolean) {
                        Cursor dbResult = DbHelper.getInstance(contextWeakReference.get()).getCachedChampion(id);
                        if (dbResult.moveToFirst()) {
                            String json = dbResult.getString(dbResult.getColumnIndex(DbHelperContract.CACHE_CHAMPION_JSON));
                            Champion champion = new Gson().fromJson(json, Champion.class);
                            return new DomainChampion(champion);
                        } else {
                            return DomainChampion.nullValue;
                        }
                    }
                })
                .filter(new Predicate<DomainChampion>() {
                    @Override
                    public boolean test(DomainChampion domainChampion) {
                        return domainChampion != DomainChampion.nullValue;
                    }
                })
                .switchIfEmpty(RiotApiClient.getRiotApiService().getCHampionDetails(id, NetworkConstants.API_KEY)
                        .map(new Function<Champion, DomainChampion>() {
                            @Override
                            public DomainChampion apply(Champion champion) {
                                // Cache Champion here
                                cacheData(id, champion);
                                return new DomainChampion(champion);
                            }
                        }));
    }

    private void cacheData(int id, Champion champion) {
        // Local cache
        CacheChampionTask cacheChampionTask = new CacheChampionTask(id, contextWeakReference.get());
        cacheChampionTask.execute(champion);
    }
}
