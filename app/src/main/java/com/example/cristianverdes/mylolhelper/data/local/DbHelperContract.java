package com.example.cristianverdes.mylolhelper.data.local;

public class DbHelperContract {
    public static final String DATABASE_NAME = "db";

    // Champions
    public static final String CHAMPIONS_TABLE_NAME = "champions";
    public static final String CHAMPIONS_ID = "id";
    public static final String CHAMPIONS_JSON = "champs_json";

    // Favorite Champion
    public static final String FAVORITE_CHAMPIONS_TABLE_NAME = "favoriteChampions";
    public static final String FAVORITE_CHAMPION_ID = "id";
    public static final String FAVORITE_CHAMPION_JSON = "fav_champ_json";

    // Favorite Match
    public static final String FAVORITE_MATCHES_TABLE_NAME = "favoriteMatches";
    public static final String FAVORITE_MATCH_ID = "id";
    public static final String FAVORITE_MATCH_JSON = "fav_match_json";

    // Cache Champion
    public static final String CACHE_CHAMPION_TABLE_NAME = "championCache";
    public static final String CACHE_CHAMPION_ID = "data_id";
    public static final String CACHE_CHAMPION_JSON = "cache_champ_json";

    // SQLite Queries
    // Champions
    public static final String CREATE_CHAMPIONS_TABLE =
            "CREATE TABLE " +
                    CHAMPIONS_TABLE_NAME +
                    " ( " +
                    CHAMPIONS_ID + " PRIMARY KEY, " +
                    CHAMPIONS_JSON + " TEXT )";

    public static final String GET_ALL_CHAMPIONS =
            "SELECT * FROM " + CHAMPIONS_TABLE_NAME;

    // Favorite Champion
    public static final String CREATE_FAVORITE_CHAMPS_TABLE =
            "CREATE TABLE " +
                    FAVORITE_CHAMPIONS_TABLE_NAME + " ( " +
                    FAVORITE_CHAMPION_ID + " PRIMARY KEY, " +
                    FAVORITE_CHAMPION_JSON + " TEXT UNIQUE)";

    public static final String GET_FAVORITE_CHAMPIONS =
            "SELECT * FROM " + FAVORITE_CHAMPIONS_TABLE_NAME;

    // Favorite Match
    public static final String CREATE_FAVORITE_MATCHES_TABLE =
            "CREATE TABLE " +
                    FAVORITE_MATCHES_TABLE_NAME + " ( " +
                    FAVORITE_MATCH_ID + " PRIMARY KEY, " +
                    FAVORITE_MATCH_JSON + " TEXT UNIQUE)";

    public static final String GET_FAVORITE_MATCHES =
            "SELECT * FROM " + FAVORITE_MATCHES_TABLE_NAME;

    // Cache Champion
    public static final String CREATE_CACHE_CHAMPS_TABLE =
            "CREATE TABLE " +
                    CACHE_CHAMPION_TABLE_NAME +
                    " ( ID PRIMARY KEY, " +
                    CACHE_CHAMPION_ID + " INTEGER, " +
                    CACHE_CHAMPION_JSON + " TEXT )";

    public static final String GET_CACHED_CHAMPION =
            "SELECT * FROM " + DbHelperContract.CACHE_CHAMPION_TABLE_NAME +
                    " WHERE " + DbHelperContract.CACHE_CHAMPION_ID +
                    " = ? ";
}
