package com.example.cristianverdes.mylolhelper.data.model.matches;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RiotGames {

    @SerializedName("games")
    private List<Match> matches;

    // Getters and setters
    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

}
