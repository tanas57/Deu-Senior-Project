package net.muslu.seniorproject.Algorithm;

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.util.ArrayList;

public class Chromosome {

    protected int metres;
    protected int durations;
    private ArrayList<BarcodeReadModel> barcodeReadModels;
    private double fitnessScore;
    protected AlgorithmType algorithmType;

    public int getDurations() {
        return durations;
    }

    public void setDurations(int durations) {
        this.durations = durations;
    }

    public int getMetres() {
        return metres;
    }

    public void setMetres(int metres) {
        this.metres = metres;
    }

    public ArrayList<BarcodeReadModel> getBarcodeReadModels() {
        return barcodeReadModels;
    }

    public void setBarcodeReadModels(ArrayList<BarcodeReadModel> barcodeReadModels) {
        this.barcodeReadModels = barcodeReadModels;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}

