package com.example.bama.tracking.Adapter;

public class ModelData {
    String latitude, longitude, speed, feet, tanggal, waktu, id;

    public ModelData() {
    }

    public ModelData(String latitude, String longitude, String speed, String feet, String tanggal, String waktu, String id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.feet = feet;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.id= id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getFeet() {
        return feet;
    }

    public void setFeet(String feet) {
        this.feet= feet;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu= waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id= id;
    }

}
