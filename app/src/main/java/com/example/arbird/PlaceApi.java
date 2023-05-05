package com.example.arbird;


import androidx.core.location.LocationRequestCompat;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {

    @GET("/3.0/items")
    Call<PlaceSearchResponce> getSearchResult(@Query("q") String q,
                                              @Query("point1") String point1,
                                              @Query("point2") String point2,
                                              @Query("key") String key);

}
