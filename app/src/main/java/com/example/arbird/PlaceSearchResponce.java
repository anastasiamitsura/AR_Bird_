package com.example.arbird;

import java.util.ArrayList;
import java.util.List;

public class PlaceSearchResponce {
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
