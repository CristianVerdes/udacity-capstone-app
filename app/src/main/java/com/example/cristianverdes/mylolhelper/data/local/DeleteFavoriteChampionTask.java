package com.example.cristianverdes.mylolhelper.data.local;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.widgets.champions.FavoriteChampsWidget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class DeleteFavoriteChampionTask extends AsyncTask<ChampionListItem, Void, Void> {
    private WeakReference<Context> contextWeakReference;
    private DbHelper db;

    public DeleteFavoriteChampionTask(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        db = DbHelper.getInstance(contextWeakReference.get());
    }

    @Override
    protected Void doInBackground(ChampionListItem... championListItems) {
        String json = pojoToString(championListItems[0]);

        // Local DB
        db.deleteFavoriteChampion(json);

        // Firebase DB
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Firebase database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child(uid).child("favorite_champions").child(String.valueOf(championListItems[0].getId())).removeValue();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        db.close();
    }

    private String pojoToString(ChampionListItem champion) {
        Gson gson = new Gson();
        return gson.toJson(champion);
    }
}
