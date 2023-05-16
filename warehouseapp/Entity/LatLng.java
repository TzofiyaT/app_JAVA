package com.example.warehouseapp.Entity;

public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng() {
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
