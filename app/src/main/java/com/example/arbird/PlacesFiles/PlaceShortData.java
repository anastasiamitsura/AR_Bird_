package com.example.arbird.PlacesFiles;

import com.google.gson.annotations.SerializedName;

public class PlaceShortData {
    @SerializedName("name")
    private final String name;
    @SerializedName("address_name")
    private final String address;
    @SerializedName("address_comment")
    private final String etaj;


    public PlaceShortData(){
        this("Cafe", "Улица Колотушкина", "5 этаж");
    }

    public PlaceShortData(String name, String address, String etaj) {
        this.name = name;
        this.address = address;
        this.etaj = etaj;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return address;
    }

    public String getEtaj() {
        return etaj;
    }

}
