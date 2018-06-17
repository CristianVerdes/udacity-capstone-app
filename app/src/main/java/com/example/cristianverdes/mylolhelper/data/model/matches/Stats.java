package com.example.cristianverdes.mylolhelper.data.model.matches;

import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("win")
    private boolean win;


    @SerializedName("kills")
    private String kills;

    @SerializedName("deaths")
    private String deaths;

    @SerializedName("assists")
    private String assists;


    @SerializedName("largestMultiKill")
    private String largestMultiKill;

    @SerializedName("totalDamageDealt")
    private String totalDamageDealt;

    @SerializedName("totalDamageDealtToChampions")
    private String totalDamageDealtToChampions;

    @SerializedName("goldEarned")
    private String goldEarned;

    @SerializedName("totalHeal")
    private String totalHeal;

    // Getters and setters

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String getKills() {
        return kills;
    }

    public void setKills(String kills) {
        this.kills = kills;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getLargestMultiKill() {
        return largestMultiKill;
    }

    public void setLargestMultiKill(String largestMultiKill) {
        this.largestMultiKill = largestMultiKill;
    }

    public String getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(String totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public String getTotalDamageDealtToChampions() {
        return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(String totalDamageDealtToChampions) {
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public String getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(String goldEarned) {
        this.goldEarned = goldEarned;
    }

    public String getTotalHeal() {
        return totalHeal;
    }

    public void setTotalHeal(String totalHeal) {
        this.totalHeal = totalHeal;
    }
}
