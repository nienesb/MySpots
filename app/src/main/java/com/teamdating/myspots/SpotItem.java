package com.teamdating.myspots;

/**
 * Created by j.boeser on 16-3-2017.
 */

public class SpotItem {

    private long id;
    private String name;
    private String city;
    private double latitude;
    private double longitude;


    public SpotItem (String name, String city, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.latitude = 0;
        this.longitude = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String setName(String name) {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public String setCity(String city) {
        return this.city;
    }

    public String getCity() {
        return city;
    }

    public double setLatitude(double latitude) {
        return this.latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double setLongitude(double longitude) {
        return this.longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
