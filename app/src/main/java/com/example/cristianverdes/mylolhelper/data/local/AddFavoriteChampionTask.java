package com.example.cristianverdes.mylolhelper.data.local;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class AddFavoriteChampionTask extends AsyncTask<ChampionListItem, Void, Boolean> {
    private static final String TAG = AddFavoriteChampionTask.class.getSimpleName();
    private WeakReference<Context> contextWeakReference;

    public AddFavoriteChampionTask(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(ChampionListItem... championListItems) {
        String json = pojoToString(championListItems[0]);
        DbHelper db = DbHelper.getInstance(contextWeakReference.get());
        try {
            if (!db.insertFavoriteChampion(json)) {
                Log.e(TAG, "Add favorite champion error");
                return false;
            } else {
                // Get Firebase user ID
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();

                    // Firebase database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child(uid).child("favorite_champions").child(String.valueOf(championListItems[0].getId())).setValue(championListItems[0]);
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
            Toast.makeText(contextWeakReference.get(), "Champion added to favorites", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(contextWeakReference.get(), "Champion already added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private String pojoToString(ChampionListItem champion) {
        Gson gson = new Gson();
        return gson.toJson(champion);
    }
}
