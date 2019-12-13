package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

public class GeneticAlgorithmData {
    protected int[][] distances;
    protected int[][] durations;
    protected BarcodeData barcodeData;
    protected BarcodeReadModel cargoman;

    public BarcodeReadModel getCargoman() {
        return cargoman;
    }

    public void setCargoman(BarcodeReadModel cargoman) {
        this.cargoman = cargoman;
    }

    public BarcodeData getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
    }

    public GeneticAlgorithmData() {
        barcodeData = getBarcodeData();
    }

    public int[][] getDistances() {
        return distances;
    }

    public void setDistances(int[][] distances) {
        this.distances = distances;
    }

    public int[][] getDurations() {
        return durations;
    }

    public void setDurations(int[][] durations) {
        this.durations = durations;
    }

}