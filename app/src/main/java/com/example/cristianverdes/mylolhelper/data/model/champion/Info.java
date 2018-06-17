package com.example.cristianverdes.mylolhelper.data.model.champion;

import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("attack")
    private int attack;

    @SerializedName("defense")
    private int defense;

    @SerializedName("maginc")
    private int magic;

    @SerializedName("difficulty")
    private int difficulty;

    // Getters and setters
    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
