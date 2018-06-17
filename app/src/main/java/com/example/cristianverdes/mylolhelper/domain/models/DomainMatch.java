package com.example.cristianverdes.mylolhelper.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DomainMatch implements Parcelable {
    private String accountId;

    private String gameId;

    private String date;

    private String gameType;

    private String championId;

    private String status;


    private boolean win;


    private String kills;

    private String deaths;

    private String assists;


    private String largestMultiKill;

    private String totalDamageDealt;

    private String totalDamageDealtToChampions;

    private String goldEarned;

    private String totalHeal;


    // Getters and setters

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }

    // Stats

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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.gameId);
        dest.writeString(this.date);
        dest.writeString(this.gameType);
        dest.writeString(this.championId);
        dest.writeString(this.status);
        dest.writeByte(this.win ? (byte) 1 : (byte) 0);
        dest.writeString(this.kills);
        dest.writeString(this.deaths);
        dest.writeString(this.assists);
        dest.writeString(this.largestMultiKill);
        dest.writeString(this.totalDamageDealt);
        dest.writeString(this.totalDamageDealtToChampions);
        dest.writeString(this.goldEarned);
        dest.writeString(this.totalHeal);
    }

    public DomainMatch() {
    }

    protected DomainMatch(Parcel in) {
        this.accountId = in.readString();
        this.gameId = in.readString();
        this.date = in.readString();
        this.gameType = in.readString();
        this.championId = in.readString();
        this.status = in.readString();
        this.win = in.readByte() != 0;
        this.kills = in.readString();
        this.deaths = in.readString();
        this.assists = in.readString();
        this.largestMultiKill = in.readString();
        this.totalDamageDealt = in.readString();
        this.totalDamageDealtToChampions = in.readString();
        this.goldEarned = in.readString();
        this.totalHeal = in.readString();
    }

    public static final Creator<DomainMatch> CREATOR = new Creator<DomainMatch>() {
        @Override
        public DomainMatch createFromParcel(Parcel source) {
            return new DomainMatch(source);
        }

        @Override
        public DomainMatch[] newArray(int size) {
            return new DomainMatch[size];
        }
    };
}
