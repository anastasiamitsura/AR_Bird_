package com.example.arbird;

import android.os.Debug;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceRepository {

    private static Retrofit retrofit = new Retrofit
            .Builder().baseUrl("https://catalog.api.2gis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final PlaceApi api = retrofit.create(PlaceApi.class);
    private List<PlaceShortData> shortPlaceList = new ArrayList<>();
    private OnLoadingPlaceState onLoadingPlaceState = null;

    /*public void search(String point1, String point2, String category){
        api.getSearchResult(category, point1, point2, "ruidzc1585").enqueue(new Callback<PlaceSearchResponce>() {
            @Override
            public void onResponse(Call<PlaceSearchResponce> call, Response<PlaceSearchResponce> response) {
                if (onLoadingPlaceState != null && response.isSuccessful()){
                    PlaceSearchResponce body = response.body();
                    if(body != null){
                        onLoadingPlaceState.changeState(new OnLoadingPlaceState.State.Success(response.body().getSearch()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceSearchResponce> call, Throwable t) {

            }
        });
    }*/

    public void setOnLoadingPlaceState(OnLoadingPlaceState state){
        onLoadingPlaceState = state;
    }

    public void removeOnLoadingPlaceState(){
        onLoadingPlaceState = null;
    }
}
