package com.example.arbird;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AdressRepository implements AutoCloseable {
    private final DataBaseHelper dataBaseHelper;
    private final ArrayList<AdresData> adresses = new ArrayList<>();

    public AdressRepository(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        adresses.addAll(dataBaseHelper.getAll());

    }

    public List<AdresData> getAdresss() {
        return (ArrayList<AdresData>) adresses.clone();
    }

    public void addAdress(AdresData adres) {
        adresses.add(adres);
        dataBaseHelper.add(adres);
    }

    public void removeByPosition(int position) {
        adresses.remove(position);
    }

    @Override
    public void close(){
        dataBaseHelper.close();
    }
}
