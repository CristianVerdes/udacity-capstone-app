package com.example.cristianverdes.mylolhelper.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RiotAcsApiClient {
    private static Retrofit retrofit = null;
    private static RiotAcsApiService riotAcsApiService;

    private static void createClient() {
        Gson gson = new GsonBuilder().create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.riotAcsBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static RiotAcsApiService getRiotAcsApiService() {
        createClient();
        if (riotAcsApiService == null) {
            riotAcsApiService = retrofit.create(RiotAcsApiService.class);
        }

        return riotAcsApiService;
    }
}
