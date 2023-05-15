package com.example.arbird;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultDoubleGis {
    public List<PlaceShortData> getItems() {
        return items;
    }

    @SerializedName("items")
    public final List<PlaceShortData> items;

    public ResultDoubleGis(List<PlaceShortData> items) {
        this.items = items;
    }
}
