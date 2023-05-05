package com.example.arbird;

public class PlaceShortData {
    private final String name;
    private final String address;
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
