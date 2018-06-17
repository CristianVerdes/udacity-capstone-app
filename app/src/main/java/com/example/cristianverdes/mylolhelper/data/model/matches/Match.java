package com.example.cristianverdes.mylolhelper.data.model.matches;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Match {

    @SerializedName("gameId")
    private String gameId;

    @SerializedName("gameMode")
    private String gameMode;

    @SerializedName("gameCreation")
    private long gameDate;

    @SerializedName("participants")
    private List<Participant> participants;

    // Getters and Setters

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public long getGameDate() {
        return gameDate;
    }

    public void setGameDate(long gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
