package com.example.cristianverdes.mylolhelper.data.model.champions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChampionImage implements Parcelable {

    @SerializedName("full")
    private String photoPath;


    // Getters and Setters
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoPath);
    }

    public ChampionImage() {
    }

    protected ChampionImage(Parcel in) {
        this.photoPath = in.readString();
    }

    public static final Parcelable.Creator<ChampionImage> CREATOR = new Parcelable.Creator<ChampionImage>() {
        @Override
        public ChampionImage createFromParcel(Parcel source) {
            return new ChampionImage(source);
        }

        @Override
        public ChampionImage[] newArray(int size) {
            return new ChampionImage[size];
        }
    };
}
