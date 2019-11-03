package net.muslu.seniorproject.Map;

import android.location.Address;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Coordinate {

    public LatLng getLatLng() {
        return latLng;
    }

    protected void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    protected LatLng latLng;

    public Coordinate(LatLng latLng) {
        setLatLng(latLng);
    }

    @NonNull
    @Override
    public String toString() {
        return latLng.latitude + " " + latLng.longitude;
    }
}