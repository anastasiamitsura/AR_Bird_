package com.example.arbird;

import com.example.arbird.PlacesFiles.PlaceShortData;

import java.util.List;

public interface OnLoadingPlaceState {
    void changeState(State state);

    class State{
        static class Error extends State{}
        static class Loading extends State{}
        static class Success extends State{
            private final List<PlaceShortData> places;

            Success(List<PlaceShortData> places) {
                this.places = places;

            }

            public List<PlaceShortData> getPlaces() {
                return places;
            }
        }
    }
}
