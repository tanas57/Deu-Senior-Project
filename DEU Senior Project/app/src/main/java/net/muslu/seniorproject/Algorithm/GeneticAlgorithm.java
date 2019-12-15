package net.muslu.seniorproject.Algorithm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NavUtils;

import net.muslu.seniorproject.Api.JSON.DMBelowTenPoint;
import net.muslu.seniorproject.Api.JSON.GeneticAlgorithmData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import net.muslu.seniorproject.Routing.MapsActivity;

import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private List<Chromosome> population;
    private BarcodeData barcodeData;
    private int[][] distances;
    private int[][] durations;
    private BarcodeReadModel cargoman;

    public BarcodeReadModel getCargoman() {
        return cargoman;
    }

    public void setCargoman(BarcodeReadModel cargoman) {
        this.cargoman = cargoman;
    }

    private int MAX_ITERATION = 100;
    private static final double MUTATION_RATE = 0.15; // between 0 and 1
    private static final double ELITIZIM_SIZE = 0.25;
    private int POPULATION_MULTIPLIER = 1;
    private Context context;
    // customer priority
    // package priority

    long startTime = System.currentTimeMillis();
    long endTime;

    public GeneticAlgorithm(Context context, GeneticAlgorithmData geneticAlgorithmData) {
        this.context = context;
        setBarcodeData(geneticAlgorithmData.getBarcodeData());
        setDistances(geneticAlgorithmData.getDistances());
        setDurations(geneticAlgorithmData.getDurations());
        setCargoman(geneticAlgorithmData.getCargoman());

        population = new ArrayList<>();

        int popSize = getBarcodeData().GetSize();

        MAX_ITERATION = MAX_ITERATION * popSize;

        if(popSize < 10){
            POPULATION_MULTIPLIER = 5;
        }
        else if(popSize < 20){
            POPULATION_MULTIPLIER = 1;
        }
        else if(popSize < 40){
            POPULATION_MULTIPLIER = 3;
        }
        else if(popSize < 60){
            POPULATION_MULTIPLIER = 2;
        }

        popSize = popSize * POPULATION_MULTIPLIER;

        for (int i = 0; i< popSize; i++){
            population.add(new Chromosome());
        }

        FillRoutes();
        new GeneticTask().execute();
    }

    private int getPopulationSize(){ return population.size(); }
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

    public void Work() {

        int counter = 1;
        int iteratePercent = (int) Math.ceil(MAX_ITERATION * 0.25); // for choosing which crossover method applied
        while (counter <= MAX_ITERATION) {

            for (Chromosome item : getPopulation()) {
                item.setFitnessScore(FitnessFunction(item));
            }

            int elitizim = (int) Math.ceil(getPopulationSize() * ELITIZIM_SIZE);

            sortPopulation();

            ArrayList<Chromosome> nextGen = new ArrayList<>();

            ArrayList<Chromosome> parents = new ArrayList<>();

            if(counter == 100){
                Log.v("1000"," 10000");
            }

            for (int i = 0; i < elitizim; i++) {
                Chromosome mutatedElit = PointMutationOverOne(population.get(i));
                nextGen.add(mutatedElit);
                parents.add(mutatedElit);
            }


            // cross-over
            int changeIndex = getPopulationSize() - 1;
            for (int i = 0; i < elitizim / 2; i++) {

                Chromosome mother = null, father = null;

                while (true) {
                    mother = SelectRouteWithWhellSelection(parents);
                    father = SelectRouteWithWhellSelection(parents);
                    if(mother != father && mother != null && father != null) break;
                }

                RouteDetail(mother, "MOTHER");
                RouteDetail(father, "FATHER");

                Chromosome child1 = null, child2 = null;
                while (child1 == child2){
                    child1 = CrossOverMP(mother, father);
                    child2 = CrossOverMP(mother, father);
                }

                RouteDetail(child1, "CHILD1");
                RouteDetail(child2, "CHILD2");

                nextGen.add(child1);
                nextGen.add(child2);;
            }

            // MUTATION
            int nextcounter = 1;
            Random rd = new Random();
            while (true) {
                Chromosome mutate = PointMutationOverOne(population.get(elitizim + nextcounter));

                nextGen.add(mutate);

                if (nextGen.size() >= getPopulationSize()) break;

                nextcounter++;
            }

            setPopulation(nextGen);
            counter++;
            //standartDeviation();
            //Log.v("ITERATION : " + counter, "ENDS");
        }
    }

    private void sortPopulation() {

        int n = population.size();

        QuickSort ob = new QuickSort();
        ob.sort(population, 0, n-1);
    }

    private void RouteDetail(Chromosome route, String info){
        String temp = "";

        if(route == null) return;

        for(BarcodeReadModel model : route.getBarcodeReadModels()){
             temp += model.getPackageId() + " ";
        }
        temp += " Fitness : " + route.getFitnessScore()+ " Metres : " + route.getMetres();
        Log.v(info+ " ROUTE DETAIL", temp);
    }

    private Chromosome onePointMutatiton(Chromosome chromosome){

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

        //RouteDetail(newChild, "NEWCHILD");
        newFitness = FitnessFunction(newChild);
        newChild.setFitnessScore(newFitness);

        //Log.v("ONE POINT MUTATITON", "Eski değer : " + fitness + " Yeni değer : " + newFitness);

        if(newFitness >= fitness) return newChild;

        return chromosome;
    }

    private class PointSortItem{
        double fitness;
        int startindex;
        int stopindex;

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        public PointSortItem(double fitness, int startindex, int stopindex) {
            this.fitness = fitness;
            this.startindex = startindex;
            this.stopindex = stopindex;
        }
    }

    private Chromosome PointMutationOverOne(Chromosome chromosome){
        int mutateGen = (int)Math.round(MUTATION_RATE * chromosome.getBarcodeReadModels().size());

        if(mutateGen % 2 == 1) mutateGen++;

        ArrayList<Integer> changeArr = new ArrayList<>();
        Random rd = new Random();

        while(true){
            int choosenGen = rd.nextInt(chromosome.getBarcodeReadModels().size());

            if(choosenGen == 0) continue; // we must not change cargoman

            if(changeArr.contains(choosenGen)) continue; // we must not add same index

            changeArr.add(choosenGen);

            if(changeArr.size() >= mutateGen) break;

        }
        ArrayList<BarcodeReadModel> models = chromosome.getBarcodeReadModels();

        double currentFitness = chromosome.getFitnessScore();

        ArrayList<PointSortItem> fitness = new ArrayList<>();

        for (int i = 0; i < mutateGen/2; i++){
            int change1 = changeArr.get(i);
            int change2 = changeArr.get(changeArr.size()-i-1);
            BarcodeReadModel temp = models.get(change1);
            models.set(change1, models.get(change2));
            models.set(change2, temp);

            chromosome.setBarcodeReadModels(models);
            chromosome.setFitnessScore(FitnessFunction(chromosome));
            fitness.add(new PointSortItem(chromosome.getFitnessScore(), change1, change2));

            temp = models.get(change2);
            models.set(change2, models.get(change1));
            models.set(change1, temp);

        }


        int size = fitness.size();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size - i - 1; j++)
                if (fitness.get(j).getFitness() < fitness.get(j + 1).getFitness()) {
                    // swap arr[j+1] and arr[i]
                    PointSortItem temp = fitness.get(j);
                    fitness.set(j, fitness.get(j + 1));
                    fitness.set(j + 1, temp);
                }



        if(fitness.size() > 0){
            PointSortItem item = fitness.get(0);
            if(item.getFitness() >= currentFitness){
                BarcodeReadModel temp = models.get(item.startindex);
                models.set(item.startindex, models.get(item.stopindex));
                models.set(item.stopindex, temp);
                chromosome.setBarcodeReadModels(models);
                RouteDetail(chromosome, "MUTATED");
            }
        }




        chromosome.setBarcodeReadModels(models);
        chromosome.setFitnessScore(FitnessFunction(chromosome));
        return chromosome;
    }
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
        for (int i = 0; i<parent1.getBarcodeReadModels().size();i++) {
            boolean flag = false;
            boolean flag2 = false;
            if (firstChildPoints.get(i) == null) {
                for (int j = 0; j < parent1.getBarcodeReadModels().size(); j++) {
                    if (!firstChildPoints.contains(parent1.getBarcodeReadModels().get(j))) {
                        flag = true;
                        tempIndex = j;
                        break;
                    }
                }
            }

            if (secondChildPoints.get(i) == null) {
                for (int z = 0; z < parent2.getBarcodeReadModels().size(); z++) {
                    if (!secondChildPoints.contains(parent2.getBarcodeReadModels().get(z))) {
                        flag2 = true;
                        tempIndex2 = z;
                        break;
                    }

                }

            }
            if (flag) {
                firstChildPoints.set(i, parent1.getBarcodeReadModels().get(tempIndex));
            }
            if (flag2) {
                secondChildPoints.set(i, parent2.getBarcodeReadModels().get(tempIndex2));
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
        int ttt = (int)Math.round(parent1.getBarcodeReadModels().size() / 2);

        int n = random.nextInt(ttt);

        if(n == 0) n = 1;
        int startBit = random.nextInt(parent1.getBarcodeReadModels().size() - n);
        if(startBit == 0) startBit = 1;

        ArrayList<BarcodeReadModel> points = new ArrayList<>(); // child1

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
                //Log.v("EKLENDI", "YENI DEĞER EKLENDİ");
            }
        }

        Chromosome child = new Chromosome();
        child.setBarcodeReadModels(points);
        child.setFitnessScore(FitnessFunction(child));
        return child;
    }
    // (Proportional Roulette Whell Selection

    private Chromosome SelectRouteWithWhellSelection(ArrayList<Chromosome> parents){
        double sum = 0;
        ArrayList<Double> points = new ArrayList<>();
        for(Chromosome item : parents){
            sum += item.getFitnessScore();
            points.add(item.getFitnessScore());
        }

        for(int i = 0; i < points.size(); i++) {
            //double probability = Math.abs(1 - points.get(i)/sum);
            double probability = points.get(i)/sum;
            points.set(i, probability);
            //Log.v("PROBABILITIES", " " + probability);
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
        for (Chromosome item : getPopulation()) {
            item.setBarcodeReadModels(GetShuffledModel());
        }
    }

    private ArrayList<BarcodeReadModel> GetShuffledModel(){
        ArrayList<BarcodeReadModel> shuffle = new ArrayList<>();

        for(BarcodeReadModel item : getBarcodeData().GetData()) shuffle.add(item);

        Collections.shuffle(shuffle);
        shuffle.add(0, getCargoman());
        return shuffle;
    }

    private double FitnessFunction(Chromosome item){
        double temp = 0; int metres = 0, tempMetres;
        ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
        BarcodeReadModel previous = null, next = null;
        for(int i = 0; i < models.size(); i++){
            if(i < models.size() - 1){
                previous = models.get(i);
                next = models.get(i+1);
                tempMetres = distances[previous.getPackageId()][next.getPackageId()];
                metres += tempMetres;
                //temp += (distances[previous.getPackageId()][next.getPackageId()] / 1000) / (durations[previous.getPackageId()][next.getPackageId()] / 60);
                //temp += (durations[previous.getPackageId()][next.getPackageId()] / 60);
                temp += distances[previous.getPackageId()][next.getPackageId()];
            }
        }

        item.setMetres(metres);
        //return 1/temp;
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
            endTime = System.currentTimeMillis();
            long estimatedTime = endTime - startTime;
            double seconds = (double)estimatedTime/1000;
            Log.v("RUNNING TIME => ", seconds + " ");
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
                Log.v("RESULT FITNESS", tmpstr);
                tmpstr = "";

            }

            for(BarcodeReadModel item2 : route.getBarcodeReadModels()){
                tmpstr += item2.getPackageId() + " ";
            }

            tmpstr += " Fitness : " + route.getFitnessScore() + " metres " + route.getMetres();
            Log.v("SELECTION => ", tmpstr);

            for (BarcodeReadModel item : route.getBarcodeReadModels()){
                if(item.getPackageId() == 0) continue;
                Log.v("CUSTOMER " + item.getPackageId()," priority : " + item.getCargoPackage().getPriority() + " "+  item.getCustomer().getFullName() + " " + item.getCustomer().getAddress());
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