package com.example.cristianverdes.mylolhelper.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.model.matches.Match;
import com.google.gson.Gson;

import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CACHE_CHAMPION_ID;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CACHE_CHAMPION_JSON;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CACHE_CHAMPION_TABLE_NAME;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CHAMPIONS_JSON;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CHAMPIONS_TABLE_NAME;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CREATE_CACHE_CHAMPS_TABLE;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CREATE_CHAMPIONS_TABLE;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CREATE_FAVORITE_CHAMPS_TABLE;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.CREATE_FAVORITE_MATCHES_TABLE;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.DATABASE_NAME;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.FAVORITE_CHAMPIONS_TABLE_NAME;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.FAVORITE_CHAMPION_ID;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.FAVORITE_CHAMPION_JSON;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.FAVORITE_MATCHES_TABLE_NAME;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.FAVORITE_MATCH_JSON;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.GET_ALL_CHAMPIONS;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.GET_CACHED_CHAMPION;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.GET_FAVORITE_CHAMPIONS;
import static com.example.cristianverdes.mylolhelper.data.local.DbHelperContract.GET_FAVORITE_MATCHES;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper dbHelper;

    // Constructor
    public static synchronized DbHelper getInstance(Context context) {
        // Use application context, which will ensure that
        // you don't leak an Activity context.
        if (dbHelper == null) {
            dbHelper = new DbHelper(context.getApplicationContext());
        }

        return dbHelper;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // ABSTRACT CLASS: SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAMPIONS_TABLE);
        db.execSQL(CREATE_FAVORITE_CHAMPS_TABLE);
        db.execSQL(CREATE_CACHE_CHAMPS_TABLE);
        db.execSQL(CREATE_FAVORITE_MATCHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHAMPIONS_TABLE_NAME);
        onCreate(db);
    }

    // BdHelper functions
    // CACHE: All champions
    public boolean insertAllChampionsCache(String allChampionJson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHAMPIONS_JSON, allChampionJson);

        db.insert(CHAMPIONS_TABLE_NAME, null, contentValues);

        return true;
    }

    public Cursor getAllChampions() {
        return getReadableDatabase().rawQuery(GET_ALL_CHAMPIONS, null);
    }

    // CACHE: Champion
    public boolean insertChampionCache(int id, String championJson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CACHE_CHAMPION_ID, id);
        contentValues.put(CACHE_CHAMPION_JSON, championJson);

        db.insert(CACHE_CHAMPION_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getCachedChampion(int id) {
        return getReadableDatabase().rawQuery(GET_CACHED_CHAMPION, new String[] {String.valueOf(id)});
    }

    // FAVORITE:
    // Match
    public boolean insertFavoriteMatch(String match) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVORITE_MATCH_JSON, match);

        db.insertOrThrow(FAVORITE_MATCHES_TABLE_NAME, null, contentValues);

        return true;
    }

    public Cursor getFavoriteMatches() {
        return getReadableDatabase().rawQuery(GET_FAVORITE_MATCHES, null);
    }

    public Cursor getFavoriteChampions() {
        return getReadableDatabase().rawQuery(GET_FAVORITE_CHAMPIONS, null);
    }

    public boolean insertFavoriteChampion(String champion) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVORITE_CHAMPION_JSON, champion);

        db.insertOrThrow(FAVORITE_CHAMPIONS_TABLE_NAME, null, contentValues);

        return true;
    }

    public void deleteFavoriteChampion(String championJSON) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FAVORITE_CHAMPIONS_TABLE_NAME, FAVORITE_CHAMPION_JSON + " LIKE  '" + championJSON + "' ", null);
    }

    public void deleteFavoriteMatch(String matchJSON) {
        SQLiteDatabase db = this.getWritableDatabase();

        db. delete(FAVORITE_MATCHES_TABLE_NAME, FAVORITE_MATCH_JSON + " LIKE '" + matchJSON + "' ", null);
    }
}
