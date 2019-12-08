package net.muslu.seniorproject.Algorithm;

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.util.ArrayList;
import java.util.Comparator;

public class Chromosome {

    protected int metres;

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

    private ArrayList<BarcodeReadModel> barcodeReadModels;
    private double fitnessScore;

}

