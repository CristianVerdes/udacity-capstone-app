package com.example.cristianverdes.mylolhelper.data.remote;

import com.example.cristianverdes.mylolhelper.data.model.matches.Matches;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RiotAcsApiService {
    @GET("stats/player_history/EUN1/{id}")
    Observable<Matches> getRecentMatches(@Path("id") String id);
}
