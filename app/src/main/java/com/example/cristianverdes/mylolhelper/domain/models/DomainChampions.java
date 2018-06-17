package com.example.cristianverdes.mylolhelper.domain.models;

import com.example.cristianverdes.mylolhelper.data.model.champion.Champion;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DomainChampions {

    public static final DomainChampions nullValue = new DomainChampions(new Champions());

    private List<ChampionListItem> championsArrayList;

    public DomainChampions() {
        this.championsArrayList = new ArrayList<>();
    }

    public DomainChampions(Champions champions) {
        championsArrayList = new ArrayList<>();

        for (Object o : champions.getChampions().entrySet()) {
            Map.Entry champion = (Map.Entry) o;
            championsArrayList.add((ChampionListItem) champion.getValue());
        }
    }

    // Getters and Setters
    public List<ChampionListItem> getChampionsArrayList() {
        return championsArrayList;
    }

    public void setChampionsArrayList(List<ChampionListItem> championsArrayList) {
        this.championsArrayList.addAll(championsArrayList);
    }
}
