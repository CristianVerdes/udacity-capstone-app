package com.example.cristianverdes.mylolhelper.data.model.summoner;

import com.google.gson.annotations.SerializedName;

public class Summoner {

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("profileIconId")
    private String profileIconId;

    @SerializedName("name")
    private String name;

    @SerializedName("summonerLevel")
    private String summonerLevel;

    // Getters and setters

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(String profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(String summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
