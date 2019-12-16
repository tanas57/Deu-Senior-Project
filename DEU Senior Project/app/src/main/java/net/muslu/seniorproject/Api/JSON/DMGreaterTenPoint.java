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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DMGreaterTenPoint {

    GeneticAlgorithmData geneticAlgorithmData = new GeneticAlgorithmData();


    private BarcodeData barcodeData;
    private Context context;
    private ArrayList<PointData> points;
    private AlgorithmType algorithmType;

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

    private class PointData{
        private int index;
        private int[] distance;
        private int[] duration;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int[] getDistance() {
            return distance;
        }

        public void setDistance(int[] distance) {
            this.distance = distance;
        }

        public int[] getDuration() {
            return duration;
        }

        public void setDuration(int[] duration) {
            this.duration = duration;
        }
    }
    private class UrlData{
        protected int index;
        protected String url;
        protected String response;


        public UrlData(int index, String url) {
            this.index = index;
            this.url = url;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
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

    public DMGreaterTenPoint(Context context, BarcodeData barcodeData, AlgorithmType algorithmType) {
        setContext(context);
        setBarcodeData(barcodeData);
        setAlgorithmType(algorithmType);
        points = new ArrayList<>();
    }

    public void Execute(){
        ArrayList<String> urls = getRequestURL();
        for(int i = 0; i< urls.size(); i++)
            new TaskRequestDistance().execute(new UrlData(i, urls.get(i)));
    }

    public ArrayList<String> getRequestURL(){

        // set value enable the sensor
        String value = "sensor=false";
        // mode for direction
        String mode = "mode=driving";
        // distance unit
        String unit = "units=metric";String output = "json";
        // api key
        String key = "key="+context.getString(R.string.google_maps_key);

        getBarcodeData().GetData().add(0, geneticAlgorithmData.getCargoman()); // add cargoman starting address

        ArrayList<String> urls = new ArrayList<>();

        for(int i = 0; i < getBarcodeData().GetSize(); i++){
            BarcodeReadModel temp = getBarcodeData().getDataByID(i);
            String origin = "origins=" + temp.getLatLng();
            String destination = "destinations=";
            for(int j = 0; j < getBarcodeData().GetSize(); j++){
                temp = getBarcodeData().getDataByID(j);
                destination += temp.getLatLng();
                if(j != getBarcodeData().GetSize()-1) {
                    destination += "|";
                }
            }
            String param = origin + "&" + destination + "&" + value + "&" + mode + "&" + unit;
            String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + param + "&" + key;
            urls.add(url);
            Log.v("DIST_API_REQUEST_URI ", url);
        }

        // build full parameters

        //output format

        // create url to request


        return urls;
    }

    public class TaskRequestDistance extends AsyncTask<UrlData, Void, String> {
        UrlData urlData = null;
        @Override
        protected String doInBackground(UrlData... strings) {
            String responseString = "";
            try{
                urlData = (UrlData) strings[0];
                responseString = DistanceMatrixFunctions.requestDistanceMatrix(strings[0].getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            urlData.setResponse(s);
            // parse json here
            new TaskParser().execute(urlData);
        }
    }

    public class TaskParser extends AsyncTask<UrlData, Void, List<HashMap<String, String>>>{

        UrlData urlData = null;
        @Override
        protected List<HashMap<String, String>> doInBackground(UrlData... strings) {
            JSONObject jsonObject = null;
            List<HashMap<String, String>> routes = null;
            try{
                urlData = (UrlData)strings[0];
                jsonObject = new JSONObject(urlData.getResponse());
                DistanceParser directionsParser = new DistanceParser();
                routes = directionsParser.parse2(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> lists) {

            int barcodeSize = getBarcodeData().GetSize();
            int [] distances = new int[barcodeSize];
            int [] durations = new int[barcodeSize];

            int counter1 = 0;

            PointData pointData = new PointData();
            pointData.setIndex(urlData.getIndex());

            for(HashMap<String, String> path : lists){
                int dis = Integer.parseInt(path.get("dis"));
                int dur = Integer.parseInt(path.get("dur"));
                if(counter1>=barcodeSize) break;
                distances[counter1] = dis;
                durations[counter1] = dur;
                counter1++;
            }

            String temp = "", temp2 = "";
            for (int i = 0; i < barcodeSize; i++){
                temp += distances[i] + " ";
                temp2 += durations[i] + " ";
            }
            //Log.v("MATRIX DISTANCE", temp);
            temp = "";
            Log.v("MATRIX DURATION", temp2);
            temp = "";

            pointData.setDistance(distances);
            pointData.setDuration(durations);

            points.add(pointData);
            if(points.size() == barcodeSize){
                getBarcodeData().GetData().remove(0);
                int[][] distancesMx = new int[barcodeSize][barcodeSize];
                int[][] durationMx = new int[barcodeSize][barcodeSize];

                for(int i=0; i < points.size(); i++){
                    int[] tempDis = points.get(i).getDistance();
                    int[] tempDur = points.get(i).getDistance();
                    for(int j=0; j<tempDis.length; j++){
                        distancesMx[points.get(i).getIndex()][j] = tempDis[j];
                        durationMx[points.get(i).getIndex()][j] = tempDur[j];
                    }
                }

                geneticAlgorithmData.setAlgorithmType(getAlgorithmType());
                geneticAlgorithmData.setDistances(distancesMx);
                geneticAlgorithmData.setDurations(durationMx);
                geneticAlgorithmData.setBarcodeData(getBarcodeData());
                new GeneticAlgorithm(context, geneticAlgorithmData);
            }
        }
    }
}
