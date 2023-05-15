package com.example.arbird;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlaceSearchResponce {
    @SerializedName("items")
    private final List<PlaceShortData> search;

    public PlaceSearchResponce(){
        this(new ArrayList<>());
    }
    public PlaceSearchResponce(List<PlaceShortData> search) {
        this.search = search;

    }

    public List<PlaceShortData> getSearch() {
        return search;
    }
}
