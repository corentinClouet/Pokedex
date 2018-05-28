package com.example.android.pokedex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Pokemon implements Parcelable { //utilisation of Parcelable interface to send the object in DetaiActivity from MainActivity

    private int id;
    private String name;
    private int weight;
    private int height;
    private String sprite;
    private ArrayList<String> types;

    public Pokemon() {
    }

    public Pokemon(int id, String name, int weight, int height, String sprite, ArrayList<String> types) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.sprite = sprite;
        this.types = types;
    }

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.weight);
        dest.writeInt(this.height);
        dest.writeString(this.sprite);
        dest.writeStringList(this.types);
    }

    protected Pokemon(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.weight = in.readInt();
        this.height = in.readInt();
        this.sprite = in.readString();
        this.types = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel source) {
            return new Pokemon(source);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}
