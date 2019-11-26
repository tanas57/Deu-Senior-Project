package net.muslu.seniorproject.Algorithm;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    /*
        * kaçtane kromozom sayımız olacak
        * kromozomları random doldurma
        * bunları puanlamak
        * aralarından en iyi iki ebevyn seçtik
        *
     */

    public GeneticAlgorithm(JsonDirectionMatrix.GeneticAlgoritmData geneticAlgoritmData) {
        setBarcodeData(geneticAlgoritmData.getBarcodeData());
        setDistances(geneticAlgoritmData.getDistances());
        setDurations(geneticAlgoritmData.getDurations());

        routes = new ArrayList<>();
        for (int i = 0; i< getBarcodeData().GetSize() * 2; i++){
            routes.add(new Route());
        }
        FillRoutes();
        Work();
        //new GeneticTask().execute();
    }

    public void Work(){
        // fill routes by shuflee
        for(Route item : routes){
            item.setFitnessScore(FitnessFunction(item));
            ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
            String temp = "";
            for(BarcodeReadModel item2 : models){
                temp += item2.getPackageId() + " ";
            }
            temp += " point => " + item.getFitnessScore();
            Log.v("FITNESS SCORE", temp);
            temp = "";
        }
    }

    public void FillRoutes(){
        for (Route item : routes){
            item.setBarcodeReadModels(GetShuffledModel());
            /*String temp = "";
            ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
            for(BarcodeReadModel item2 : models){
                temp += item2.getPackageId() + " ";
            }
            Log.v("ROUTES", temp);
            temp = "";
            */
        }

    }

    private ArrayList<BarcodeReadModel> GetShuffledModel(){
        ArrayList<BarcodeReadModel> shuffle = new ArrayList<>();
        for(BarcodeReadModel item : getBarcodeData().GetData()){
            shuffle.add(item);
        }
        Collections.shuffle(shuffle);
        return shuffle;
    }

    private double FitnessFunction(Route item){
        double temp = 0;
        ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
        BarcodeReadModel previous = null, next = null;
        for(int i = 0; i < models.size(); i++){
            if(i < models.size() - 1){
                previous = models.get(i);
                next = models.get(i+1);
                //temp += distances[previous.getPackageId()][next.getPackageId()] / durations[previous.getPackageId()][next.getPackageId()];
                temp += distances[previous.getPackageId()][next.getPackageId()];
            }
        }
        return temp;
    }


    private class GeneticTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Work();
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
        }
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
