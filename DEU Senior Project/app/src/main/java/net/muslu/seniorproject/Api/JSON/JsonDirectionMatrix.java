package net.muslu.seniorproject.Api.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
// direction & duration matrix
public class JsonDirectionMatrix {
    private double[] distances;
    private double[] durations;
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


    public double[] getDistances() { return distances; }

    public void setDistances(double[] distances) { this.distances = distances; }

    public double[] getDurations() { return durations; }

    public void setDurations(double[] durations) { this.durations = durations; }

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

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
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
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {


            ArrayList points = null;

            for(List<HashMap<String, String>> path : lists){
                points = new ArrayList();

                for(HashMap<String, String> point : path){
                    double dis = Double.parseDouble(point.get("dis"));
                    double dur = Double.parseDouble(point.get("dur"));

                    Log.v("PARSE_RESULT => " , ("distance :" + dis + " duration : " + dur));
                }

            }


        }
    }

}
