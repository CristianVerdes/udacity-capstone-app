package com.example.cristianverdes.mylolhelper.data.repositories;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.local.AddFavoriteMatchTask;
import com.example.cristianverdes.mylolhelper.data.local.DbHelper;
import com.example.cristianverdes.mylolhelper.data.local.DbHelperContract;
import com.example.cristianverdes.mylolhelper.data.local.DeleteFavoriteMatchTask;
import com.example.cristianverdes.mylolhelper.data.model.matches.Match;
import com.example.cristianverdes.mylolhelper.data.model.matches.*;
import com.example.cristianverdes.mylolhelper.data.model.matches.Matches;
import com.example.cristianverdes.mylolhelper.data.remote.RiotAcsApiClient;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MatchesRepository {
    private static final String TAG = MatchesRepository.class.getSimpleName();
    private static MatchesRepository matchesRepository;
    private Observable<List<DomainMatch>> matchesObservable;
    private static WeakReference<Context> contextWeakReference;
    private static DbHelper db;


    public static MatchesRepository getInstance(Context context) {
        if (matchesRepository == null) {
            contextWeakReference = new WeakReference<>(context);
            db = DbHelper.getInstance(contextWeakReference.get());
            matchesRepository =  new MatchesRepository();
        }
        return matchesRepository;
    }

    public Observable<List<DomainMatch>> getMatches(String summonerId) {
        matchesObservable = RiotAcsApiClient.getRiotAcsApiService().getRecentMatches(summonerId)
                .map(new Function<Matches, List<DomainMatch>>() {
                    @Override
                    public List<DomainMatch> apply(Matches matches) throws Exception {
                        List<DomainMatch> matchesList = new ArrayList<>();

                        for (Match match: matches.getRiotGames().getMatches()) {
                            DomainMatch domainMatch = new DomainMatch();
                            // Game ID
                            domainMatch.setGameId(match.getGameId());
                            // Game type
                            domainMatch.setGameType(match.getGameMode());
                            // Date
                            Date date = new Date(match.getGameDate());
                            domainMatch.setDate(date.toString());

                            for (Participant participant: match.getParticipants()) {
                                // Champion id
                                domainMatch.setChampionId(participant.getChampionId());
                                // Win / Lose
                                domainMatch.setWin(participant.getStats().isWin());
                                // Kills
                                domainMatch.setKills(participant.getStats().getKills());
                                // Deaths
                                domainMatch.setDeaths(participant.getStats().getDeaths());
                                // Assists
                                domainMatch.setAssists(participant.getStats().getAssists());
                                // Multi kill
                                domainMatch.setLargestMultiKill(participant.getStats().getLargestMultiKill());
                                // Total damage
                                domainMatch.setTotalDamageDealt(participant.getStats().getTotalDamageDealt());
                                // Damage to champions
                                domainMatch.setTotalDamageDealtToChampions(participant.getStats().getTotalDamageDealtToChampions());
                                // Gold earned
                                domainMatch.setGoldEarned(participant.getStats().getGoldEarned());
                                // Total Heal
                                domainMatch.setTotalHeal(participant.getStats().getTotalHeal());
                            }
                            domainMatch.setAccountId(matches.getAccountId());
                            matchesList.add(domainMatch);
                        }
                        return matchesList;
                    }
                });

        return  matchesObservable;

    }

    public Observable<List<DomainMatch>> getFavoriteMatches() {

        return Observable.just(true)
                .map(new Function<Boolean, List<DomainMatch>>() {
                    @Override
                    public List<DomainMatch> apply(Boolean aBoolean) throws Exception {
                        List<DomainMatch> matches = new ArrayList<>();
                        Cursor dbResult = db.getFavoriteMatches();

                        while (dbResult.moveToNext()) {
                            String json = dbResult.getString(dbResult.getColumnIndex(DbHelperContract.FAVORITE_MATCH_JSON));
                            DomainMatch domainMatch = new Gson().fromJson(json, DomainMatch.class);
                            matches.add(domainMatch);
                        }

                        return matches;
                    }
                })
                .filter(new Predicate<List<DomainMatch>>() {
                    @Override
                    public boolean test(List<DomainMatch> domainMatches) throws Exception {
                        return domainMatches.size() != 0;
                    }
                })
                .switchIfEmpty(Observable.create(new ObservableOnSubscribe<List<DomainMatch>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<List<DomainMatch>> emitter) throws Exception {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // Get values from Firebase DB
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference userTableDatabaseReference = databaseReference.child(uid);

                            userTableDatabaseReference.child("favorite_matches").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<DomainMatch> matches = new ArrayList<>();

                                    for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                                        DomainMatch domainMatch = singleSnapshot.getValue(DomainMatch.class);
                                        matches.add(domainMatch);

                                        // Add data to local DB
                                        new AddFavoriteMatchTask(contextWeakReference.get()).execute(domainMatch);
                                    }

                                    emitter.onNext(matches);
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
                            emitter.onNext(new ArrayList<DomainMatch>());
                            emitter.onComplete();
                        }
                    }
                }));
    }

    public void deleteFavoriteMatch(DomainMatch domainMatch) {
        // Start delete task

        new DeleteFavoriteMatchTask(contextWeakReference.get()).execute(domainMatch);
    }
}



















