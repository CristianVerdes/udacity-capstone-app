package com.example.cristianverdes.mylolhelper.data.model.champions;

import com.example.cristianverdes.mylolhelper.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Champions {
    @SerializedName("data")
    private HashMap<String, ChampionListItem> champions;

    public Champions() {
        champions = new HashMap<>();
    }

    public void setChampions(HashMap champions) {
        this.champions.putAll(champions);
    }

    public HashMap<String, ChampionListItem> getChampions() {
        return champions;
    }
}
