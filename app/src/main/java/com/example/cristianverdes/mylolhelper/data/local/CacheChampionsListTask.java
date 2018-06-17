package com.example.cristianverdes.mylolhelper.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class CacheChampionsListTask extends AsyncTask<Champions, Void, Void> {
    private static final String TAG = CacheChampionsListTask.class.getSimpleName();
    private WeakReference<Context> contextWeakReference;

    public CacheChampionsListTask(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Champions... champions) {
        String json = pojoToString(champions[0]);
        DbHelper db = DbHelper.getInstance(contextWeakReference.get());
        if (!db.insertAllChampionsCache(json)) {
            Log.e(TAG, "Cache all champions error");
        }
        return null;
    }

    private String pojoToString(Champions champions) {
        Gson gson = new Gson();
        return gson.toJson(champions);
    }

}
