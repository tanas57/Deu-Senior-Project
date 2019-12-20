package net.muslu.seniorproject;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;

import java.io.Serializable;
import java.util.ArrayList;

public class DataTransfer implements Serializable {
    private int packageid;
    private BarcodeData barcodeData;
    private int[][] duration;
    private int[][] distance;
    private double cargomanLatitude;
    private ArrayList<Chromosome> routes;

    public DataTransfer(){
        barcodeData = new BarcodeData();
        routes = new ArrayList<>();
        packageid = 1;
        cargomanLatitude = 38.371881;
        cargomanLongitude = 27.194662;
    }


    public double getCargomanLatitude() {
        return cargomanLatitude;
    }

    public void setCargomanLatitude(double cargomanLatitude) {
        this.cargomanLatitude = cargomanLatitude;
    }

    public double getCargomanLongitude() {
        return cargomanLongitude;
    }

    public void setCargomanLongitude(double cargomanLongitude) {
        this.cargomanLongitude = cargomanLongitude;
    }

    private double cargomanLongitude;


    public int[][] getDuration() {
        return duration;
    }

    public void setDuration(int[][] duration) {
        this.duration = duration;
    }

    public int[][] getDistance() {
        return distance;
    }

    public void setDistance(int[][] distance) {
        this.distance = distance;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public BarcodeData getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
    }

    public ArrayList<Chromosome> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Chromosome> routes) {
        this.routes = routes;
    }

    public void addRoute(Chromosome chromosome) { routes.add(chromosome); }

}
