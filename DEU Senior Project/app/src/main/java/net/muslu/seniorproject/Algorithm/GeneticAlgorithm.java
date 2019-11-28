package net.muslu.seniorproject.Algorithm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Api.JSON.JsonDirectionMatrix;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import net.muslu.seniorproject.Routing.MapsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private List<Route> routes;
    private BarcodeData barcodeData;
    private int[][] distances;
    private int[][] durations;

    private static final int MAX_ITERATION = 500;
    private LatLng cargoman;

    private Context context;
    // customer priority
    // package priority

    public GeneticAlgorithm(Context context, JsonDirectionMatrix.GeneticAlgoritmData geneticAlgoritmData) {
        this.context = context;
        setBarcodeData(geneticAlgoritmData.getBarcodeData());
        setDistances(geneticAlgoritmData.getDistances());
        setDurations(geneticAlgoritmData.getDurations());

        routes = new ArrayList<>();
        for (int i = 0; i< getBarcodeData().GetSize() * 2; i++){
            routes.add(new Route());
        }

        cargoman = new LatLng(38.371881, 27.194662);

        FillRoutes();
        new GeneticTask().execute();
    }

    private double standartDeviation(){
        double sum = 0;
        for(Route route : routes){
            sum += route.getFitnessScore();
        }

        double variation = 0, sd = 0;

        for(Route route : routes){
            variation +=  Math.pow(route.getFitnessScore() - sum, 2);
        }
        sd = Math.sqrt(variation / routes.size() - 1);
        Log.v("STANDART DEV.", "SUM " + sum + " VARIATION " + variation + " SD " + sd);
        return sd;
    }

    public void Work(){

        int counter = 1;

        while(counter <= MAX_ITERATION){
            for(Route item : routes){
                item.setFitnessScore(FitnessFunction(item));
                ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
                String temp = "";
                for(BarcodeReadModel item2 : models){
                    temp += item2.getPackageId() + " ";
                }
                temp += " point => " + item.getFitnessScore() + " METRES " + item.getMetres();
                Log.v("FITNESS SCORE", temp);
            }
            Route mother = SelectRouteWithWhellSelection();
            Route father = SelectRouteWithWhellSelection();
            RouteDetail(mother);
            RouteDetail(father);
            Route child = CrossOverMP(mother, father);
            RouteDetail(child);

            if(routes.size() > 0){
                Route temp = routes.get(0);
                double tempFitness = Double.MAX_VALUE;
                for(Route route: routes){
                    if(tempFitness > route.getFitnessScore()){
                        tempFitness = route.getFitnessScore();
                        temp = route;
                    }
                }
                for(int i = 0; i < routes.size(); i++){
                    if(routes.get(i) == temp){
                        routes.set(i, child);
                        Log.v("KILLLING", "POPULATION CHANGED");
                        break;
                    }
                }
            }

            counter++;
            standartDeviation();
        }
    }

    private void RouteDetail(Route route){
        String temp = "";
        for(BarcodeReadModel model : route.getBarcodeReadModels()){
            temp += model.getPackageId() + " ";
        }
        temp += route.getFitnessScore();
        Log.v("ROUTE DETAIL", temp);
    }

    // Maximal Preservative Crossover (MPX) :
    private Route CrossOverMP(Route parent1, Route parent2){

        Random random = new Random();
        double ttt = parent1.getBarcodeReadModels().size() / 2;
        int n = random.nextInt((int)Math.round(ttt));
        if(n == 0) n = 1;
        int startBit = random.nextInt(parent1.getBarcodeReadModels().size() - n);
        if(startBit == 0) startBit = 1;
        ArrayList<BarcodeReadModel> points = new ArrayList<>();

        points.add(parent1.getBarcodeReadModels().get(0)); // cargomen added firstly

        for(int i = 0; i < n; i++){
            points.add(parent1.getBarcodeReadModels().get(startBit+i));
        }

        ArrayList<BarcodeReadModel> parentsPoint = parent2.getBarcodeReadModels();

        for(int i = 1; i < parentsPoint.size(); i++){
            //Log.v("FOR 1", parentsPoint.get(i).getPackageId() + " ");
            boolean flag = false;
            for(int j = 1; j < points.size(); j++){
                //Log.v("FOR 2", parentsPoint.get(i).getPackageId() + " " + points.get(j).getPackageId());
                int parentid = parentsPoint.get(i).getPackageId();
                int childd = points.get(j).getPackageId();
                if( parentid == childd){
                    flag = true;
                    break;
                }
            }
            if(flag == false){
                points.add(parentsPoint.get(i));
                Log.v("EKLENDI", "YENI DEĞER EKLENDİ");
            }
        }

        Route child = new Route();
        child.setBarcodeReadModels(points);


        child.setFitnessScore(FitnessFunction(child));
        return child;
    }
    // (Proportional Roulette Whell Selection

    private Route SelectRouteWithWhellSelection(){
        double sum = 0;
        ArrayList<Double> points = new ArrayList<>();
        for(Route item : routes){
            sum += item.getFitnessScore();
            points.add(item.getFitnessScore());
        }

        for(int i = 0; i < points.size(); i++) {
            //double probability = Math.abs(1 - points.get(i)/sum);
            double probability = points.get(i)/sum;
            points.set(i, probability);
            Log.v("PROBABILITIES", " " + probability);
        }

        double choose = (Math.random() % 101);
        double between = 0.0;
        for (Route route : routes){
            if(between <= choose && choose <= between + route.getFitnessScore()){
                return route;
            }
            between += route.getFitnessScore();
        }
        return null;
    }

    public void FillRoutes(){
        for (Route item : routes) item.setBarcodeReadModels(GetShuffledModel());

    }

    private ArrayList<BarcodeReadModel> GetShuffledModel(){
        ArrayList<BarcodeReadModel> shuffle = new ArrayList<>();

        for(BarcodeReadModel item : getBarcodeData().GetData()) shuffle.add(item);

        Collections.shuffle(shuffle);
        shuffle.add(0, new BarcodeReadModel(-1, cargoman.latitude, cargoman.longitude));
        return shuffle;
    }

    private double FitnessFunction(Route item){
        double temp = 0;
        ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
        BarcodeReadModel previous = null, next = null;
        for(int i = 1; i < models.size(); i++){
            if(i < models.size() - 1){
                previous = models.get(i);
                next = models.get(i+1);
                //temp += distances[previous.getPackageId()][next.getPackageId()] / durations[previous.getPackageId()][next.getPackageId()];
                temp += distances[previous.getPackageId()][next.getPackageId()];
            }
        }
        //return temp;
        item.setMetres((int)temp);
        return 1/(temp/1000); // convert metres to kilometers
    }

    private class GeneticTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            Work();
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);

            Route route = null;

            double control = -1;
            Log.v("POPULATION SIZE", "SIZE => " + routes.size());

            String tmpstr = "";
            for(int i = 0; i < routes.size(); i++){
                Route temp = routes.get(i);
                if(temp.getFitnessScore() > control){
                    control = temp.getFitnessScore();
                    route = temp;
                }

                for(BarcodeReadModel item2 : temp.getBarcodeReadModels()){
                    tmpstr += item2.getPackageId() + " ";
                }
                tmpstr += " point => " + temp.getFitnessScore() + " METRES " + temp.getMetres();
                Log.v("FITNESS WITH SELECTION", tmpstr);
                tmpstr = "";

            }
            Intent intent = new Intent(context, MapsActivity.class);
            BarcodeData barcodeData = new BarcodeData();
            barcodeData.setData(route.getBarcodeReadModels());
            intent.putExtra("data", barcodeData);
            context.startActivity(intent);

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