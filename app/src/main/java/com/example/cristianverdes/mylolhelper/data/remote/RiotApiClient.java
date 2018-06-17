package com.example.cristianverdes.mylolhelper.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RiotApiClient {
    private static Retrofit retrofit = null;
    private static RiotApiService riotApiService;


    private static void createClient() {
        Gson gson = new GsonBuilder().create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.riotBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static RiotApiService getRiotApiService() {
        createClient();
        if (riotApiService == null) {
            riotApiService = retrofit.create(RiotApiService.class);
        }
        return riotApiService;
    }
}
