package com.example.cristianverdes.mylolhelper.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.model.champion.Champion;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class CacheChampionTask extends AsyncTask<Champion, Void, Void> {
    private static final String TAG = CacheChampionsListTask.class.getSimpleName();
    private WeakReference<Context> contextWeakReference;
    private int championId;

    public CacheChampionTask(int id, Context context) {
        contextWeakReference = new WeakReference<>(context);
        championId = id;
    }

    @Override
    protected Void doInBackground(Champion... champions) {
        String championJson = pojoToString(champions[0]);
        DbHelper db = DbHelper.getInstance(contextWeakReference.get());

        if (!db.insertChampionCache(championId, championJson)) {
            Log.e(TAG, "Cache champion error.");
        }

        return null;
    }

    private String pojoToString(Champion champion) {
        Gson gson = new Gson();
        return gson.toJson(champion);
    }
}
