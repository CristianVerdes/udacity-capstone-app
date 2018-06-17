package com.example.cristianverdes.mylolhelper.domain.models;

import com.example.cristianverdes.mylolhelper.data.model.summoner.Summoner;
import com.google.gson.annotations.SerializedName;

public class DomainSummoner {
    private String accountId;

    private String profileIconId;

    private String name;

    private String summonerLevel;

    // Constructor
    public DomainSummoner(Summoner summoner) {
        this.accountId = summoner.getAccountId();
        this.profileIconId = summoner.getProfileIconId();
        this.name = summoner.getName();
        this.summonerLevel = summoner.getSummonerLevel();
    }

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
