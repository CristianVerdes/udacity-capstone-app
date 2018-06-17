package com.example.cristianverdes.mylolhelper.data.model.matches;

import com.google.gson.annotations.SerializedName;

public class Matches {

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("games")
    private RiotGames riotGames;

    // Getters and setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public RiotGames getRiotGames() {
        return riotGames;
    }

    public void setRiotGames(RiotGames riotGames) {
        this.riotGames = riotGames;
    }
}
