package com.example.cristianverdes.mylolhelper.data.local;

import android.content.Context;
import android.os.AsyncTask;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class DeleteFavoriteMatchTask extends AsyncTask<DomainMatch, Void, Void> {
    private WeakReference<Context> contextWeakReference;
    private DbHelper db;

    public DeleteFavoriteMatchTask(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        this.db = DbHelper.getInstance(contextWeakReference.get());
    }

    @Override
    protected Void doInBackground(DomainMatch... domainMatches) {
        String json = pojoToString(domainMatches[0]);

        // Local DB
        db.deleteFavoriteMatch(json);

        // Firebase DB
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Firebase database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child(uid).child("favorite_matches").child(domainMatches[0].getGameId()).removeValue();
        }

        return null;
    }

    private String pojoToString(DomainMatch match) {
        Gson gson = new Gson();
        return gson.toJson(match);
    }
}
