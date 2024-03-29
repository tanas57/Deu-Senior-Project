package net.muslu.seniorproject.Algorithm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import net.muslu.seniorproject.Api.JSON.GeneticAlgorithmData;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private List<Chromosome> population;
    private BarcodeData barcodeData;
    private double[][] distances;
    private double[][] durations;
    private BarcodeReadModel cargoman;
    private AlgorithmType algorithmType;
    private String id;
    public BarcodeReadModel getCargoman() {
        return cargoman;
    }

    public void setCargoman(BarcodeReadModel cargoman) {
        this.cargoman = cargoman;
    }

    private int MAX_ITERATION = 100;
    private static final double MUTATION_RATE = 0.2; // between 0 and 1
    private static final double ELITIZIM_SIZE = 0.35;
    private int POPULATION_MULTIPLIER = 1;
    private Context context;
    private long startTime = System.currentTimeMillis();
    private long endTime;
    // customer priority
    // package priority

    public GeneticAlgorithm(Context context, GeneticAlgorithmData geneticAlgorithmData) {
        this.context = context;
        setBarcodeData(geneticAlgorithmData.getBarcodeData());
        setDistances(geneticAlgorithmData.getDistances());
        setDurations(geneticAlgorithmData.getDurations());
        setCargoman(geneticAlgorithmData.getCargoman());
        setAlgorithmType(geneticAlgorithmData.getAlgorithmType());

        Random rd = new Random();
        id = String.valueOf(rd.nextInt(1111));

        population = new ArrayList<>();

        int popSize = getBarcodeData().GetSize();

        MAX_ITERATION = MAX_ITERATION * popSize;

        if(popSize < 10){
            POPULATION_MULTIPLIER = 6;
        }
        else if(popSize < 20){
            POPULATION_MULTIPLIER = 4;
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
        new GeneticTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private int getPopulationSize(){ return population.size(); }

    public void Work() {

        int counter = 1;
        while (counter <= MAX_ITERATION) {

            for (Chromosome item : getPopulation()) {
                item.setFitnessScore(FitnessFunction(item));
            }

            int elitizim = (int) Math.ceil(getPopulationSize() * ELITIZIM_SIZE);

            sortPopulation();

            ArrayList<Chromosome> nextGen = new ArrayList<>();

            ArrayList<Chromosome> parents = new ArrayList<>();

            for (int i = 0; i < elitizim; i++) {
                Chromosome mutatedElit = null;
                if(i < elitizim*MUTATION_RATE) {
                    //RouteDetail(population.get(i), "BEFORE ELITIZIM");
                    //mutatedElit = mutationForElitizim(population.get(i));
                    //RouteDetail(population.get(i), "AFTER ELITIZIM");
                    mutatedElit = population.get(i);
                }
                else
                    mutatedElit = PointMutationOverOne(population.get(i));
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

                //RouteDetail(mother, "MOTHER");
                //RouteDetail(father, "FATHER");

                Chromosome child1 = null, child2 = null;
                while (child1 == child2){
                    child1 = CrossOverMP(mother, father);
                    child2 = CrossOverMP(mother, father);
                }

                //RouteDetail(child1, "CHILD1");
                //RouteDetail(child2, "CHILD2");

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

            if(counter%100==0) {
                Functions.sendNotification(MAX_ITERATION, counter, context, id);
                Log.v("COUNTER", counter + " ");
            }

            counter++;
            setPopulation(nextGen);
        }
    }

    private void sortPopulation() {

        int n = population.size();

        QuickSort ob = new QuickSort();
        ob.sort(population, 0, n-1);

    }
    // it will use if it is necessary..
    /**
     * This functions shows the package id's of selected chromosome
     * @param route Which chromosome
     * @param info a message letters ( max 25 characters ) it shows logcat message title.
     */
    private void RouteDetail(Chromosome route, String info){
        String temp = "";

        if(route == null) return;

        for(BarcodeReadModel model : route.getBarcodeReadModels()){
             temp += model.getPackageId() + " ";
        }
        temp += " Fitness : " + route.getFitnessScore()+ " Metres : " + route.getMetres();
        Log.v(info+ " ROUTE DETAIL", temp);
    }

    private Chromosome mutationForElitizim(Chromosome chromosome){

        Random rd =  new Random();
        double currentFitness = chromosome.getFitnessScore();

        int n = rd.nextInt(chromosome.getBarcodeReadModels().size() / 3);

        ArrayList<Integer> controlArr = new ArrayList<>();
        while(true){
            int choosenGen = rd.nextInt(chromosome.getBarcodeReadModels().size());

            if(choosenGen == 0 || choosenGen == chromosome.getBarcodeReadModels().size() - 1) continue; // we must not change cargoman

            if(controlArr.contains(choosenGen)) continue; // we must not add same index

            controlArr.add(choosenGen);

            if(controlArr.size() >= n) break;

        }

        ArrayList<PointSortItem> fitness = new ArrayList<>();

        ArrayList<BarcodeReadModel> models = chromosome.getBarcodeReadModels();

        int change1 = 0, change2 = 0;
        for (int i = 0; i < n; i++){
            int selectedIndex = controlArr.get(i);

            for(int j = -1; j < 2; j = j+2){
                if(selectedIndex == 1){
                    // only control its right
                    change1 = selectedIndex;
                    change2 = selectedIndex+1;
                    BarcodeReadModel temp = models.get(change1);
                    models.set(change1, models.get(change2));
                    models.set(change2, temp);
                    break;
                }
                else{
                    change1 = selectedIndex;
                    change2 = selectedIndex + j;
                    BarcodeReadModel temp = models.get(change1);
                    models.set(change1, models.get(change2));
                    models.set(change2, temp);
                }
                chromosome.setBarcodeReadModels(models);
                chromosome.setFitnessScore(FitnessFunction(chromosome));
                fitness.add(new PointSortItem(chromosome.getFitnessScore(), change1, change2));

                BarcodeReadModel temp = models.get(change2);
                models.set(change2, models.get(change1));
                models.set(change1, temp);

            }
        }

        int size = fitness.size();
        for (int a = 0; a < size; a++)
            for (int j = 0; j < size - a - 1; j++)
                if (fitness.get(j).getFitness() < fitness.get(j + 1).getFitness()) {
                    // swap arr[j+1] and arr[i]
                    PointSortItem temp = fitness.get(j);
                    fitness.set(j, fitness.get(j + 1));
                    fitness.set(j + 1, temp);
                }

        if(fitness.size() > 0){
            if(fitness.get(0).getFitness() >= currentFitness){
                PointSortItem item = fitness.get(0);
                BarcodeReadModel temp = models.get(item.startindex);
                models.set(item.startindex, models.get(item.stopindex));
                models.set(item.stopindex, temp);
            }

        }

        chromosome.setBarcodeReadModels(models);
        chromosome.setFitnessScore(FitnessFunction(chromosome));
        return chromosome;

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
            BarcodeReadModel temp = models.get(item.startindex);
            models.set(item.startindex, models.get(item.stopindex));
            models.set(item.stopindex, temp);
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

        int tempIndex = -2 ; int tempIndex2 = -2;

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
        double temp = 0, tempMetres = 0; int metres = 0, duration = 0;
        ArrayList<BarcodeReadModel> models = item.getBarcodeReadModels();
        BarcodeReadModel previous = null, next = null;
        for(int i = 0; i < models.size(); i++){
            if(i < models.size() - 1){
                previous = models.get(i);
                next = models.get(i+1);
                tempMetres = distances[previous.getPackageId()][next.getPackageId()];
                duration += durations[previous.getPackageId()][next.getPackageId()];
                switch (algorithmType){
                    case ONLY_DISTANCE:
                        temp += distances[previous.getPackageId()][next.getPackageId()];
                        break;
                    case ONLY_DURATION:
                        temp += durations[previous.getPackageId()][next.getPackageId()] / 60.0;
                        break;
                    case ALL_OF_THEM:
                    case BOTH_DISTANCE_DURATION:
                        double dis = distances[previous.getPackageId()][next.getPackageId()];
                        double dur = durations[previous.getPackageId()][next.getPackageId()];
                        double result = (((dis/1000.0)* dis) /(dur/60.0)*dur)/10000;
                        temp += result;
                        break;
                    case DISTANCE_PRIORITY:
                        temp += distances[previous.getPackageId()][next.getPackageId()];
                        break;
                    case DURATION_PRIORITY:
                        temp += durations[previous.getPackageId()][next.getPackageId()] / 60.0;
                        break;
                }

                metres += tempMetres;
            }
        }

        item.setDurations(duration);
        item.setMetres(metres);

        switch (algorithmType){
            case ONLY_DISTANCE:
                return 1/(temp/1000); // convert metres to kilometers
            case ONLY_DURATION:
                return 1/temp;
            case ALL_OF_THEM:
            case BOTH_DISTANCE_DURATION:
                return 1/temp;
            case DISTANCE_PRIORITY:
                return 1/(temp/1000);
            case DURATION_PRIORITY:
                return 1/temp;
        }
        return 0;
    }

    private class GeneticTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            Functions.createNotificationChannel(context, id);
            Log.v("GENETIC TASK", "IS IN PROGRESS");
            Work();
            Log.v("GENETIC TASK","END");

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
            tmpstr = "";

            double avarage = 0;

            if(algorithmType == AlgorithmType.DISTANCE_PRIORITY ) avarage = route.getMetres() / route.getBarcodeReadModels().size();
            else if(algorithmType == AlgorithmType.DURATION_PRIORITY) avarage = route.getDurations() / route.getBarcodeReadModels().size();

            ArrayList<BarcodeReadModel> changelist = new ArrayList<>();
            double changeSum = 0;

            if(algorithmType == AlgorithmType.DISTANCE_PRIORITY || algorithmType == AlgorithmType.DURATION_PRIORITY
                    || algorithmType == AlgorithmType.ALL_OF_THEM) {

               // find 2 or 3

                double originalFitness = route.getFitnessScore();
                // check 3
                for(int priority = 3; priority >= 2; priority-- ) {
                    for (int i = 1; i < route.getBarcodeReadModels().size(); i++) {
                        if (route.getBarcodeReadModels().get(i).getCargoPackage().getPriority() != 1) {
                            if (i == 1 || i == route.getBarcodeReadModels().size()) continue;

                            BarcodeReadModel temp = route.getBarcodeReadModels().get(i - 1);
                            route.getBarcodeReadModels().set(i - 1, route.getBarcodeReadModels().get(i));
                            route.getBarcodeReadModels().set(i, temp);

                            route.setFitnessScore(FitnessFunction(route));

                            double divider = 0;

                            if(priority == 2) divider = avarage / 10;
                            else if(priority == 3) divider = avarage / 5;

                            if (!((route.getFitnessScore() - originalFitness) < divider)) {
                                temp = route.getBarcodeReadModels().get(i - 1);
                                route.getBarcodeReadModels().set(i - 1, route.getBarcodeReadModels().get(i));
                                route.getBarcodeReadModels().set(i, temp);

                                route.setFitnessScore(FitnessFunction(route));
                            }
                            else{
                                Log.v("PRIORTIY", "DEĞİŞİKLİK YAPILDI");
                            }
                        }
                    }
                }

                route.setFitnessScore(FitnessFunction(route));

                for (BarcodeReadModel item2 : route.getBarcodeReadModels()) {
                    tmpstr += item2.getPackageId() + " ";
                }
                tmpstr += " point => " + route.getFitnessScore() + " METRES " + route.getMetres();
                Log.v("PRIORITY FITNESS ", tmpstr);

                tmpstr += " Fitness : " + route.getFitnessScore() + " metres " + route.getMetres();
                Log.v("PRIORITY SELECTION => ", tmpstr);

            }

            for (BarcodeReadModel item : route.getBarcodeReadModels()) {
                if (item.getPackageId() == 0) continue;
                Log.v("CUSTOMER " + item.getPackageId(), " priority : " + item.getCargoPackage().getPriority() + " " + item.getCustomer().getFullName() + " " + item.getCustomer().getAddress());
            }

            route.setAlgorithmType(algorithmType);
            Functions.addRoute(route);
        }
    }

    public List<Chromosome> getPopulation() { return population;  }

    public void setPopulation(List<Chromosome> routes) { this.population = routes; }

    public BarcodeData getBarcodeData() { return barcodeData; }

    public void setBarcodeData(BarcodeData barcodeData) { this.barcodeData = barcodeData; }

    public double[][] getDistances() { return distances; }

    public void setDistances(double[][] distances) { this.distances = distances; }

    public double[][] getDurations() { return durations; }

    public void setDurations(double[][] durations) { this.durations = durations; }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}