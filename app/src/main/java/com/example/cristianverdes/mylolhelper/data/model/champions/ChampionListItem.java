package com.example.cristianverdes.mylolhelper.data.model.champions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChampionListItem implements Parcelable {
    public static ChampionListItem nullValue = new ChampionListItem();

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private ChampionImage championImage;


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public ChampionImage getChampionImage() {
        return championImage;
    }

    public void setChampionImage(ChampionImage championImage) {
        this.championImage = championImage;
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeParcelable(this.championImage, flags);
    }

    public ChampionListItem() {
    }

    protected ChampionListItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.title = in.readString();
        this.championImage = in.readParcelable(ChampionImage.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChampionListItem> CREATOR = new Parcelable.Creator<ChampionListItem>() {
        @Override
        public ChampionListItem createFromParcel(Parcel source) {
            return new ChampionListItem(source);
        }

        @Override
        public ChampionListItem[] newArray(int size) {
            return new ChampionListItem[size];
        }
    };
}
