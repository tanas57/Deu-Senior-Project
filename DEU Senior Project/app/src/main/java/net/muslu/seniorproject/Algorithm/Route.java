package net.muslu.seniorproject.Algorithm;

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.util.ArrayList;

public class Route {

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

    private ArrayList<BarcodeReadModel> barcodeReadModels;
    private double fitnessScore;

}
