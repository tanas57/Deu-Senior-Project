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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private List<Chromosome> population;
    private BarcodeData barcodeData;
    private int[][] distances;
    private int[][] durations;

    private static final int MAX_ITERATION = 555;
    private LatLng cargoman;

    private Context context;
    // customer priority
    // package priority

    public GeneticAlgorithm(Context context, JsonDirectionMatrix.GeneticAlgoritmData geneticAlgoritmData) {
        this.context = context;
        setBarcodeData(geneticAlgoritmData.getBarcodeData());
        setDistances(geneticAlgoritmData.getDistances());
        setDurations(geneticAlgoritmData.getDurations());

        population = new ArrayList<>();
        for (int i = 0; i< getBarcodeData().GetSize() * 2; i++){
            population.add(new Chromosome());
        }

        cargoman = new LatLng(38.371881, 27.194662);

        FillRoutes();
        new GeneticTask().execute();
    }

    private double standartDeviation(){
        double sum = 0;
        for(Chromosome route : getPopulation()){
            sum += route.getFitnessScore();
        }

        double variation = 0, sd = 0;

        for(Chromosome route : population){
            variation +=  Math.pow(route.getFitnessScore() - sum, 2);
        }
        sd = Math.sqrt(variation / population.size() - 1);
        Log.v("STANDART DEV.", "SUM " + sum + " VARIATION " + variation + " SD " + sd);
        return sd;
    }

    public void Work(){

        int counter = 1;

        while(counter <= MAX_ITERATION){
            for(Chromosome item : getPopulation()){
                item.setFitnessScore(FitnessFunction(item));
                ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
                String temp = "";
                for(BarcodeReadModel item2 : models){
                    temp += item2.getPackageId() + " ";
                }
                temp += " point => " + item.getFitnessScore() + " METRES " + item.getMetres();
                Log.v("FITNESS SCORE", temp);
            }
            Chromosome mother = SelectRouteWithWhellSelection();
            Chromosome father = SelectRouteWithWhellSelection();
            RouteDetail(mother);
            RouteDetail(father);
            ArrayList<Chromosome> childs = CrossOverPOS(mother,father);
            Chromosome child = childs.get(0);


            RouteDetail(child);

            if(population.size() > 0){
                Chromosome temp = getPopulation().get(0);
                double tempFitness = Double.MAX_VALUE;
                for(Chromosome route: getPopulation()){
                    if(tempFitness > route.getFitnessScore()){
                        tempFitness = route.getFitnessScore();
                        temp = route;
                    }
                }
                for(int i = 0; i < getPopulation().size(); i++){
                    if(getPopulation().get(i) == temp){
                        getPopulation().set(i, child);
                        Log.v("KILLLING", "POPULATION CHANGED");
                        break;
                    }
                }
            }

            counter++;
            standartDeviation();
        }
    }

    private void RouteDetail(Chromosome route){
        String temp = "";
        for(BarcodeReadModel model : route.getBarcodeReadModels()){
            temp += model.getPackageId() + " ";
        }
        temp += route.getFitnessScore();
        Log.v("ROUTE DETAIL", temp);
    }

    /*
    private Chromosome onePointMutatiton(){

        double fitness = chromosome.getFitnessScore();
        Chromosome newChild = chromosome;
        ArrayList<BarcodeReadModel> models = new ArrayList<>();

        for(BarcodeReadModel item: chromosome.getBarcodeReadModels()){
            models.add(item);
        }

        int point1 = 0, point2 = 0;
        Random random = new Random();

        double newFitness = 0;

        while(true){
            point1 = random.nextInt(chromosome.getBarcodeReadModels().size());
            point2 = random.nextInt(chromosome.getBarcodeReadModels().size());
            if(point1 != 0 && point2 != 0 && point1 != point2) break;
        }

        BarcodeReadModel temp = chromosome.getBarcodeReadModels().get(point1);
        models.set(point1, chromosome.getBarcodeReadModels().get(point2));
        models.set(point2, temp);
        newChild.setBarcodeReadModels(models);

        RouteDetail(newChild);
        newFitness = FitnessFunction(newChild);
        newChild.setFitnessScore(newFitness);

        Log.v("ONE POINT MUTATITON", "Eski değer : " + fitness + " Yeni değer : " + newFitness);

        if(newFitness >= fitness) return newChild;

        return chromosome;
    }
*/

    //Position Based Crossover (POS) :
    private ArrayList<Chromosome> CrossOverPOS(Chromosome parent1 , Chromosome parent2){


        Random random = new Random();
        int forRandom = parent1.getBarcodeReadModels().size();
        ArrayList<Integer> indexes = new ArrayList<>();
        int index = -2;


        while (indexes.size() < 3){
            index=random.nextInt(forRandom);
            if(index !=0) {
                if(indexes.contains(index) == false) indexes.add(index);
            }
        }
        ArrayList<BarcodeReadModel> firstChildPoints = new ArrayList<>();
        ArrayList<BarcodeReadModel> secondChildPoints = new ArrayList<>();

        while(firstChildPoints.size()<parent1.getBarcodeReadModels().size()){
            firstChildPoints.add(null);
            secondChildPoints.add(null);
        }

        firstChildPoints.set(0,parent1.getBarcodeReadModels().get(0));
        secondChildPoints.set(0,parent2.getBarcodeReadModels().get(0));
        for(int i = 0; i<indexes.size();i++){

            firstChildPoints.set(indexes.get(i),parent2.getBarcodeReadModels().get(indexes.get(i)));
            secondChildPoints.set(indexes.get(i),parent1.getBarcodeReadModels().get(indexes.get(i)));
        }
        int tempIndex = -2 ; int tempIndex2=-2;
        for (int i = 0; i<parent1.getBarcodeReadModels().size();i++)
       {
           boolean flag = false;
           boolean flag2 = false;
            if(firstChildPoints.get(i)==null){
              for (int j = 0; j<parent1.getBarcodeReadModels().size();j++){
                  if(!firstChildPoints.contains(parent1.getBarcodeReadModels().get(j))){
                        flag = true;
                        tempIndex=j;
                        break;
                  }

              }

            }

           if(secondChildPoints.get(i)==null){
               for (int z = 0; z<parent2.getBarcodeReadModels().size();z++){
                   if(!secondChildPoints.contains(parent2.getBarcodeReadModels().get(z))){
                       flag2 = true;
                       tempIndex2=z;
                       break;
                   }

               }

           }
              if(flag){
                    firstChildPoints.set(i,parent1.getBarcodeReadModels().get(tempIndex));
                }
                if(flag2){
                    secondChildPoints.set(i,parent2.getBarcodeReadModels().get(tempIndex2));
                }
            }







        ArrayList<Chromosome> myChilds = new ArrayList<>();
        Chromosome ch1 = new Chromosome();
        ch1.setBarcodeReadModels(firstChildPoints);
        ch1.setFitnessScore(FitnessFunction(ch1));
        myChilds.add(ch1);
        Chromosome ch2 = new Chromosome();
        ch2.setBarcodeReadModels(secondChildPoints);
        ch2.setFitnessScore(FitnessFunction(ch2));
        myChilds.add(ch2);



        return myChilds;



    }



    // Maximal Preservative Crossover (MPX) :
    private Chromosome CrossOverMP(Chromosome parent1, Chromosome parent2){

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

        Chromosome child = new Chromosome();
        child.setBarcodeReadModels(points);


        child.setFitnessScore(FitnessFunction(child));
        return child;
    }
    // (Proportional Roulette Whell Selection

    private Chromosome SelectRouteWithWhellSelection(){
        double sum = 0;
        ArrayList<Double> points = new ArrayList<>();
        for(Chromosome item : getPopulation()){
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
        for (Chromosome route : getPopulation()){
            if(between <= choose && choose <= between + route.getFitnessScore()){
                return route;
            }
            between += route.getFitnessScore();
        }
        return null;
    }

    public void FillRoutes(){
        for (Chromosome item : getPopulation()) item.setBarcodeReadModels(GetShuffledModel());

    }

    private ArrayList<BarcodeReadModel> GetShuffledModel(){
        ArrayList<BarcodeReadModel> shuffle = new ArrayList<>();

        for(BarcodeReadModel item : getBarcodeData().GetData()) shuffle.add(item);

        Collections.shuffle(shuffle);
        shuffle.add(0, new BarcodeReadModel(-1, cargoman.latitude, cargoman.longitude));
        return shuffle;
    }

    private double FitnessFunction(Chromosome item){
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

            Chromosome route = null;

            double control = -1;
            Log.v("POPULATION SIZE", "SIZE => " + getPopulation().size());

            String tmpstr = "";
            for(int i = 0; i < getPopulation().size(); i++){
                Chromosome temp = getPopulation().get(i);
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

    public List<Chromosome> getPopulation() { return population;  }

    public void setPopulation(List<Chromosome> routes) { this.population = routes; }

    public BarcodeData getBarcodeData() { return barcodeData; }

    public void setBarcodeData(BarcodeData barcodeData) { this.barcodeData = barcodeData; }

    public int[][] getDistances() { return distances; }

    public void setDistances(int[][] distances) { this.distances = distances; }

    public int[][] getDurations() { return durations; }

    public void setDurations(int[][] durations) { this.durations = durations; }

}