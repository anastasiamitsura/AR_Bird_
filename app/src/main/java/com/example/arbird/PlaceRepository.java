package com.example.arbird;

import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceRepository {
    Map<String, List<PlaceShortData>> map = new HashMap<String, List<PlaceShortData>>();
    private static final String[] categories = {"красота", "магазин", "фитнес", "авто", "кафе", "ресторан", "аптека"};
    private static Retrofit retrofit = new Retrofit
            .Builder().baseUrl("https://catalog.api.2gis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final PlaceApi api = retrofit.create(PlaceApi.class);
    private List<PlaceShortData> shortPlaceList = new ArrayList<>();
    private OnLoadingPlaceState onLoadingPlaceState = null;

    public void search(double latityde, double longitude) {
        String point1 = String.format(Locale.ENGLISH, "%.4f,%.4f",
                longitude - 0.0004,
                latityde + 0.0004
        );
        String point2 = String.format(Locale.ENGLISH, "%.4f,%.4f",
                longitude + 0.0004,
                latityde - 0.0004
        );
        for (String category : categories) {
            api.getSearchResult(category, point1, point2, "ruidzc1585").enqueue(
                    new Callback<ResponseDoubleGis>() {
                        @Override
                        public void onResponse(
                                Call<ResponseDoubleGis> call,
                                Response<ResponseDoubleGis> response
                        ) {
                            if (onLoadingPlaceState != null && response.isSuccessful()) {
                                ResponseDoubleGis body = response.body();
                                if (body != null) {
                                    if(response.body().getResult() != null){
                                        map.put(category, response.body().getResult().getItems());

                                    }
                                    else map.put(category, null);
                                    check();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDoubleGis> call, Throwable t) {
                            Log.e("ERORR", t.getMessage());
                        }
                    }
            );
        }
    }

    public void check(){
        List<PlaceShortData> list = new ArrayList<>();
        if(map.size() == categories.length){
            for (List<PlaceShortData> body : map.values()) {
                if(body != null){
                    list.addAll(body);
                }
            }
            onLoadingPlaceState.changeState(
                    new OnLoadingPlaceState.State.Success(
                            list
                    )
            );
        }
    }

    public void setOnLoadingPlaceState(OnLoadingPlaceState state) {
        onLoadingPlaceState = state;
    }

    public void removeOnLoadingPlaceState() {
        onLoadingPlaceState = null;
    }
}
