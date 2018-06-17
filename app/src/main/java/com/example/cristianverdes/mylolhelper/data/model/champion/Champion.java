package com.example.cristianverdes.mylolhelper.data.model.champion;

import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionImage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Champion {

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("lore")
    private String lore;

    @SerializedName("info")
    private Info info;

    @SerializedName("passive")
    private Spell passive;

    @SerializedName("spells")
    private List<Spell> spells;

    @SerializedName("allytips")
    private List<String> allyTips;

    @SerializedName("enemytips")
    private List<String> enemyTips;

    @SerializedName("image")
    private ChampionImage championImage;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public void setSpells(List<Spell> spells) {
        this.spells = spells;
    }

    public List<String> getAllyTips() {
        return allyTips;
    }

    public void setAllyTips(List<String> allyTips) {
        this.allyTips = allyTips;
    }

    public List<String> getEnemyTips() {
        return enemyTips;
    }

    public void setEnemyTips(List<String> enemyTips) {
        this.enemyTips = enemyTips;
    }

    public Spell getPassive() {
        return passive;
    }

    public void setPassive(Spell passive) {
        this.passive = passive;
    }

    public ChampionImage getChampionImage() {
        return championImage;
    }

    public void setChampionImage(ChampionImage championImage) {
        this.championImage = championImage;
    }
}
