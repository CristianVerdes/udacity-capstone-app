package com.example.cristianverdes.mylolhelper.data.repositories;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.local.AddFavoriteChampionTask;
import com.example.cristianverdes.mylolhelper.data.local.CacheChampionsListTask;
import com.example.cristianverdes.mylolhelper.data.local.DbHelper;
import com.example.cristianverdes.mylolhelper.data.local.DbHelperContract;
import com.example.cristianverdes.mylolhelper.data.local.DeleteFavoriteChampionTask;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.data.remote.RiotApiClient;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ChampionsRepository {
    private static String TAG = ChampionsRepository.class.getSimpleName();

    private static WeakReference<Context> contextWeakReference;
    private static ChampionsRepository championsRepository;
    private Observable<DomainChampions> championsObservable;
    private DbHelper db;

    private ChampionsRepository(Context context) {
        contextWeakReference = new WeakReference<>(context);
        db = DbHelper.getInstance(contextWeakReference.get());
    }

    public static ChampionsRepository getInstance(Context context) {
        if (championsRepository == null) {
            championsRepository = new ChampionsRepository(context);
            return championsRepository;
        } else {
            return championsRepository;
        }
    }

    // Getters
    public Observable<DomainChampions> getChampions() {
        return Observable.just(true)
                .map(new Function<Boolean, DomainChampions>() {
                    @Override
                    public DomainChampions apply(Boolean aBoolean) {
                        Cursor dbResult = db.getAllChampions();
                        if (dbResult.moveToFirst()) {
                            String json = dbResult.getString(dbResult.getColumnIndex(DbHelperContract.CHAMPIONS_JSON));
                            Champions champions = new Gson().fromJson(json, Champions.class);
                            return new DomainChampions(champions);
                        } else {
                            return DomainChampions.nullValue;
                        }
                    }
                })
                .filter(new Predicate<DomainChampions>() {
                    @Override
                    public boolean test(DomainChampions s) {
                        return s != DomainChampions.nullValue;
                    }
                })
                .switchIfEmpty(RiotApiClient.getRiotApiService().getChampions(NetworkConstants.API_KEY)
                        .map(new Function<Champions, DomainChampions>() {
                            @Override
                            public DomainChampions apply(Champions champions) throws Exception {
                                // Cache data here
                                cacheData(champions);
                                return new DomainChampions(champions);
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void cacheData(Champions champions) {
        CacheChampionsListTask cacheChampionsListTask = new CacheChampionsListTask(contextWeakReference.get());
        cacheChampionsListTask.execute(champions);
    }

    public Observable<DomainChampions> getFavoriteChampions() {
        final DbHelper db = DbHelper.getInstance(contextWeakReference.get());

        return Observable.just(true)
                .map(new Function<Boolean, DomainChampions>() {
                    @Override
                    public DomainChampions apply(Boolean aBoolean) throws Exception {
                        DomainChampions domainChampions = new DomainChampions();
                        List<ChampionListItem> champions = new ArrayList<>();

                        Cursor dbResult = db.getFavoriteChampions();

                        while (dbResult.moveToNext()) {
                            String json = dbResult.getString(dbResult.getColumnIndex(DbHelperContract.FAVORITE_CHAMPION_JSON));
                            ChampionListItem champion = new Gson().fromJson(json, ChampionListItem.class);
                            champions.add(champion);
                        }

                        domainChampions.setChampionsArrayList(champions);

                        return domainChampions;

                    }
                })
                .filter(new Predicate<DomainChampions>() {
                    @Override
                    public boolean test(DomainChampions domainChampions) throws Exception {
                        return domainChampions.getChampionsArrayList().size() != 0;
                    }
                })
                .switchIfEmpty(Observable.create(new ObservableOnSubscribe<DomainChampions>() {
                    @Override
                    public void subscribe(final ObservableEmitter<DomainChampions> emitter) throws Exception {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // Get values from Firebase DB
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference userTableDatabaseReference = databaseReference.child(uid);
                            userTableDatabaseReference.child("favorite_champions").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DomainChampions domainChampions = new DomainChampions();
                                    List<ChampionListItem> championsList = new ArrayList<>();

                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        ChampionListItem championListItem = singleSnapshot.getValue(ChampionListItem.class);
                                        championsList.add(championListItem);

                                        // Add data to local DB
                                        new AddFavoriteChampionTask(contextWeakReference.get()).execute(championListItem);
                                    }

                                    domainChampions.setChampionsArrayList(championsList);
                                    emitter.onNext(domainChampions);
                                    emitter.onComplete();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, databaseError.getDetails());
                                    emitter.onError(databaseError.toException());
                                    emitter.onComplete();
                                }
                            });
                        } else {
                            emitter.onNext(DomainChampions.nullValue);
                            emitter.onComplete();
                        }
                    }
                }));

    }

    public void deleteFavoriteChampion(ChampionListItem championListItem) {
        // Start Delete task
        new DeleteFavoriteChampionTask(contextWeakReference.get()).execute(championListItem);

    }
}

























