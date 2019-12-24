package net.muslu.seniorproject.Api.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Algorithm.GeneticAlgorithm;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  Created by MusluNET on 17.11.19
 */
// direction & duration matrix, below ten points
public class DMBelowTenPoint {

    GeneticAlgorithmData geneticAlgorithmData = new GeneticAlgorithmData();

    private BarcodeData barcodeData;
    private Context context;
    private ArrayList<AlgorithmType> algorithmType;

    public ArrayList<AlgorithmType> getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(ArrayList<AlgorithmType> algorithmType) {
        this.algorithmType = algorithmType;
    }

    public void setCargoman(BarcodeReadModel cargoman) {
        geneticAlgorithmData.cargoman = cargoman;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BarcodeData getBarcodeData() { return barcodeData; }

    public void setBarcodeData(BarcodeData barcodeData) { this.barcodeData = barcodeData; }

    public DMBelowTenPoint(Context context, BarcodeData barcodeData, ArrayList<AlgorithmType> algorithmType) {
        setContext(context);
        setBarcodeData(barcodeData);
        setAlgorithmType(algorithmType);
    }

    public void Execute(){
        String url = getRequestURL();
        new TaskRequestDistance().execute(url);
    }
    public String getRequestURL(){
        String origin = "origins=";
        String destination = "destinations=";

        getBarcodeData().GetData().add(0, geneticAlgorithmData.getCargoman()); // add cargoman starting address

        for(int i = 0; i < getBarcodeData().GetSize(); i++){
            BarcodeReadModel temp = getBarcodeData().getDataByID(i);
            origin += temp.getLatLng();
            destination += temp.getLatLng();
            if(i != getBarcodeData().GetSize()-1) {
                origin += "|"; destination += "|";
            }
        }
        // set value enable the sensor
        String value = "sensor=false";
        // mode for direction
        String mode = "mode=driving";
        // distance unit
        String unit = "units=metric";
        // build full parameters
        String param = origin + "&" + destination + "&" + value + "&" + mode + "&" + unit;
        //output format
        String output = "json";
        // api key
        String key = "key="+context.getString(R.string.google_maps_key);
        // create url to request
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + param + "&" + key;
        Log.v("DIST_API_REQUEST_URI=> ", url);
        return url;
    }

    public class TaskRequestDistance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = DistanceMatrixFunctions.requestDistanceMatrix(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // parse json here
            new TaskParser().execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<HashMap<String, String>>>{

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<HashMap<String, String>> routes = null;
            try{
                jsonObject = new JSONObject(strings[0]);
                DistanceParser directionsParser = new DistanceParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> lists) {


            int barcodeSize =getBarcodeData().GetSize();
            double [][] distances = new double[barcodeSize][barcodeSize];
            double [][] durations = new double[barcodeSize][barcodeSize];

            int counter1 = 0, counter2 = 0;
            for(HashMap<String, String> path : lists){
                int dis = Integer.parseInt(path.get("dis"));
                int dur = Integer.parseInt(path.get("dur"));
                distances[counter1][counter2] = dis;
                durations[counter1][counter2] = dur;
                counter2++;
                if(counter2 % barcodeSize == 0){
                    counter1++;
                    counter2 = 0;
                }
            }

            String temp = "";
            for (int i = 0; i < barcodeSize; i++){
                for (int j = 0; j < barcodeSize; j++){
                    temp += distances[i][j] + " ";
                }
                Log.v("MATRIX", temp);
                temp = "";
            }

            getBarcodeData().GetData().remove(0);

            geneticAlgorithmData.setDistances(distances);
            geneticAlgorithmData.setDurations(durations);
            geneticAlgorithmData.setBarcodeData(getBarcodeData());

            for(AlgorithmType type : getAlgorithmType()){
                geneticAlgorithmData.setAlgorithmType(type);
                new GeneticAlgorithm(context, geneticAlgorithmData);
            }
        }
    }

}
