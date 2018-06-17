package com.example.cristianverdes.mylolhelper.data.remote;

import com.example.cristianverdes.mylolhelper.data.model.champion.Champion;
import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.example.cristianverdes.mylolhelper.data.model.summoner.Summoner;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RiotApiService {
    @GET("static-data/v3/champions?locale=en_US&champListData=image&dataById=false")
    Observable<Champions> getChampions(@Query("api_key") String apiKey);

    @GET("static-data/v3/champions/{id}?locale=en_US&champData=all&tags=all")
    Observable<Champion> getCHampionDetails(@Path("id")int id, @Query("api_key") String apiKey);

    @GET("summoner/v3/summoners/by-name/{summonerName}")
    Observable<Summoner> getSummoner( @Path("summonerName")String summonerName, @Query("api_key") String apiKey);


}
