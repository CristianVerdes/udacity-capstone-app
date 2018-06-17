package com.example.cristianverdes.mylolhelper.data.local;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.example.cristianverdes.mylolhelper.data.model.matches.Match;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class AddFavoriteMatchTask extends AsyncTask<DomainMatch, Void, Boolean> {
    private static final String TAG = AddFavoriteMatchTask.class.getSimpleName();
    private WeakReference<Context> contextWeakReference;

    public AddFavoriteMatchTask(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(DomainMatch... matches) {
        String json = pojoToString(matches[0]);
        DbHelper db = DbHelper.getInstance(contextWeakReference.get());
        try {
            if (!db.insertFavoriteMatch(json)) {
                Log.e(TAG, "Add favorite match error");
                return false;
            } else {
                // Get Firebase user ID
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();

                    // Firebase database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child(uid).child("favorite_matches").child(String.valueOf(matches[0].getGameId())).setValue(matches[0]);
                }

                return true;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean isInserted) {
        if (isInserted) {
            Toast.makeText(contextWeakReference.get(), "Match added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contextWeakReference.get(), "Match already added to favorites", Toast.LENGTH_SHORT).show();
        }

    }

    private String pojoToString(DomainMatch match) {
        Gson gson = new Gson();
        return gson.toJson(match);
    }
}
