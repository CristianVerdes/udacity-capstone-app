package com.example.cristianverdes.mylolhelper.data.model.matches;

import com.google.gson.annotations.SerializedName;

public class Participant {

    @SerializedName("championId")
    private String championId;

    @SerializedName("stats")
    private Stats stats;

    // Getters and Setters

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
