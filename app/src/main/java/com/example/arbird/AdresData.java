package com.example.arbird;

import java.util.Objects;

public class AdresData {
    private String addres;
    private String city;
    private String country;
    private String postalCode;
    private String knownName;

    @Override
    public int hashCode() {
        return Objects.hash(addres, city, country, postalCode, knownName);
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }


    public AdresData(String addres, String city, String country, String postalCode, String knownName) {
        this.addres = addres;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdresData that = (AdresData) o;
        return addres.equals(that.addres) && city.equals(that.city) && country.equals(that.country) && postalCode.equals(that.postalCode) && knownName.equals(that.knownName);
    }

}
