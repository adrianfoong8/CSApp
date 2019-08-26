package com.example.csapp;

public class UploadLocation {

    String latitude;
    String longitude;

    public UploadLocation() {
    }

    public UploadLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
