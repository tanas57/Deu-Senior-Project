package net.muslu.seniorproject.Algorithm;

import android.os.AsyncTask;
import net.muslu.seniorproject.Api.JSON.JsonDirectionMatrix;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GeneticAlgorithm {

    private List<Route> routes;
    private BarcodeData barcodeData;
    private int[][] distances;
    private int[][] durations;

    // customer priority
    // package priority

    public GeneticAlgorithm(JsonDirectionMatrix.GeneticAlgoritmData geneticAlgoritmData) {
        setBarcodeData(geneticAlgoritmData.getBarcodeData());
        setDistances(geneticAlgoritmData.getDistances());
        setDurations(geneticAlgoritmData.getDurations());

        routes = new ArrayList<>();
        for (int i = 0; i< getBarcodeData().GetSize(); i++){
            routes.add(new Route());
        }
        FillRoutes();
        new GeneticTask().execute();
    }

    private class GeneticTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
        }
    }
    public void Work(){
        // fill routes by shuflee
        for(Route item : routes){
            item.setFitnessScore(FitnessFunction(item));
        }
    }

    public void FillRoutes(){
        BarcodeData barcodeData = getBarcodeData();
        ArrayList<BarcodeReadModel> packages = barcodeData.GetData();

        for (Route item : routes){
            Collections.shuffle(packages);
            item.setBarcodeReadModels(packages);
        }

    }


    private int FitnessFunction(Route item){
        for(BarcodeReadModel model : item.getBarcodeReadModels()){
            
        }
        return 0;
    }

    public List<Route> getRoutes() { return routes;  }

    public void setRoutes(List<Route> routes) { this.routes = routes; }

    public BarcodeData getBarcodeData() { return barcodeData; }

    public void setBarcodeData(BarcodeData barcodeData) { this.barcodeData = barcodeData; }

    public int[][] getDistances() { return distances; }

    public void setDistances(int[][] distances) { this.distances = distances; }

    public int[][] getDurations() { return durations; }

    public void setDurations(int[][] durations) { this.durations = durations; }

}
