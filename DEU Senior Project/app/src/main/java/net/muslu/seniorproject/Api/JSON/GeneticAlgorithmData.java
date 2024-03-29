package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

public class GeneticAlgorithmData {
    protected double[][] distances;
    protected double[][] durations;
    protected BarcodeData barcodeData;
    protected BarcodeReadModel cargoman;
    protected AlgorithmType algorithmType;

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

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

    public double[][] getDistances() {
        return distances;
    }

    public void setDistances(double[][] distances) {
        this.distances = distances;
    }

    public double[][] getDurations() {
        return durations;
    }

    public void setDurations(double[][] durations) {
        this.durations = durations;
    }

}