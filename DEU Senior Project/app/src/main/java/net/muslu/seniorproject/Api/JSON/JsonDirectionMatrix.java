package net.muslu.seniorproject.Api.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.List;

/**
 *  Created by MusluNET on 17.11.19
 */
// direction & duration matrix
public class JsonDirectionMatrix {

    public class GeneticAlgoritmData {
        private int[][] distances;
        private int[][] durations;
        private BarcodeData barcodeData;

        public BarcodeData getBarcodeData() {
            return barcodeData;
        }

        public void setBarcodeData(BarcodeData barcodeData) {
            this.barcodeData = barcodeData;
        }

        public GeneticAlgoritmData() {
            barcodeData = getBarcodeData();
        }

        public int[][] getDistances() {
            return distances;
        }

        public void setDistances(int[][] distances) {
            this.distances = distances;
        }

        public int[][] getDurations() {
            return durations;
        }

        public void setDurations(int[][] durations) {
            this.durations = durations;
        }

    }

    private BarcodeData barcodeData;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BarcodeData getBarcodeData() { return barcodeData; }

    public void setBarcodeData(BarcodeData barcodeData) { this.barcodeData = barcodeData; }

    public JsonDirectionMatrix(Context context, BarcodeData barcodeData) {
        setContext(context);
        setBarcodeData(barcodeData);
    }

    public void Execute(){
        String url = getRequestURL();
        new TaskRequestDirection().execute(url);
    }
    public String getRequestURL(){
        String origin = "origins=";
        String destination = "destinations=";
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

    public String requestDirectionMatrix(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine())!= null){
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        Log.v("Distance Api Response", responseString);
        return responseString;
    }
    public class TaskRequestDirection extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = requestDirectionMatrix(strings[0]);
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

            GeneticAlgoritmData geneticAlgoritmData = new GeneticAlgoritmData();
            int barcodeSize =getBarcodeData().GetSize();
            int [][] distances = new int[barcodeSize][barcodeSize];
            int [][] durations = new int[barcodeSize][barcodeSize];

            int counter1 = 0, counter2 = 0;
            for(HashMap<String, String> path : lists){
                int dis = Integer.parseInt(path.get("dis"));
                int dur = Integer.parseInt(path.get("dur"));
                distances[counter1][counter2] = dis;
                durations[counter1][counter2] = dur;
                Log.v("PARSE_RESULT => " , ("distance :" + dis + " duration : " + dur));

                counter2++;
                if(counter2 % barcodeSize == 0){
                    counter1++;
                    counter2 = 0;
                }
            }

            geneticAlgoritmData.setDistances(distances);
            geneticAlgoritmData.setDurations(durations);
            geneticAlgoritmData.setBarcodeData(getBarcodeData());
            new GeneticAlgorithm(geneticAlgoritmData);
        }
    }

}
