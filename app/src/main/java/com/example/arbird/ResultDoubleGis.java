package com.example.arbird;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResultDoubleGis {
    public List<PlaceShortData> getItems() {
        return items;
    }


    @SerializedName("items")
    public final List<PlaceShortData> items;

    public ResultDoubleGis(){
        this(new ArrayList<>());
    }
    public ResultDoubleGis(List<PlaceShortData> search) {
        this.items = search;
    }
}
