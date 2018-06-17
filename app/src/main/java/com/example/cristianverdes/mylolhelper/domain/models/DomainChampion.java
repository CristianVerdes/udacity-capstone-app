package com.example.cristianverdes.mylolhelper.domain.models;

import com.example.cristianverdes.mylolhelper.data.model.champion.Champion;

public class DomainChampion {
    public static final DomainChampion nullValue = new DomainChampion();

    private String name;
    private String icon;
    private String title;

    private String lore;

    private String attack;
    private String defence;
    private String magic;
    private String difficulty;

    private String passive;
    private String q;
    private String w;
    private String e;
    private String r;

    private String allyTips;
    private String defeatTips;

    public DomainChampion() {

    }

    public DomainChampion(Champion champion) {
        icon = champion.getChampionImage().getPhotoPath();

        name = champion.getName();
        title = champion.getTitle();

        lore = champion.getLore();

        attack = String.valueOf(champion.getInfo().getAttack());
        defence = String.valueOf(champion.getInfo().getDefense());
        magic = String.valueOf(champion.getInfo().getMagic());
        difficulty = String.valueOf(champion.getInfo().getDifficulty());

        passive = champion.getPassive().getName() + "\n" + champion.getPassive().getDescription();
        q = champion.getSpells().get(0).getName() + "\n" + champion.getSpells().get(0).getDescription();
        w = champion.getSpells().get(1).getName() + "\n" + champion.getSpells().get(1).getDescription();
        e = champion.getSpells().get(2).getName() + "\n" + champion.getSpells().get(2).getDescription();
        r = champion.getSpells().get(3).getName() + "\n" + champion.getSpells().get(3).getDescription();

        StringBuilder allyTipsBuilder = new StringBuilder();
        for (String tip: champion.getAllyTips()) {
            allyTipsBuilder.append("-");
            allyTipsBuilder.append(tip);
            allyTipsBuilder.append("\n");
        }
        allyTips = allyTipsBuilder.toString();

        StringBuilder defeatTipsBuilder = new StringBuilder();
        for (String tip: champion.getEnemyTips()) {
            defeatTipsBuilder.append("-");
            defeatTipsBuilder.append(tip);
            defeatTipsBuilder.append("\n");
        }
        defeatTips = defeatTipsBuilder.toString();
    }

    // Getters


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLore() {
        return lore;
    }

    public String getAttack() {
        return attack;
    }

    public String getDefence() {
        return defence;
    }

    public String getMagic() {
        return magic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getPassive() {
        return passive;
    }

    public String getQ() {
        return q;
    }

    public String getW() {
        return w;
    }

    public String getE() {
        return e;
    }

    public String getR() {
        return r;
    }

    public String getAllyTips() {
        return allyTips;
    }

    public String getDefeatTips() {
        return defeatTips;
    }
}
