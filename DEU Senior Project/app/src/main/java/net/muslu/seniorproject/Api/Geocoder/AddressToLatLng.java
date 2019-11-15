package net.muslu.seniorproject.Api.Geocoder;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddressToLatLng extends Application {
    protected BarcodeReadModel barcodeReadModel;

    public BarcodeReadModel getBarcodeReadModel() {
        return barcodeReadModel;
    }

    public void GetLatLng() throws IOException{
        String dende = requestDirectionAPI();
    }

    protected void setBarcodeReadModel(BarcodeReadModel barcodeReadModel) {
        this.barcodeReadModel = barcodeReadModel;
    }

    public AddressToLatLng(BarcodeReadModel barcodeReadModel) {
        setBarcodeReadModel(barcodeReadModel);
    }

    protected String getRequestURL(){
        String str_start = "address=" + getBarcodeReadModel().getCustomerAddress();
        // set value enable the sensor
        String value = "sensor=false";
        // mode for direction
        String mode = "mode=driving";
        // build full parameters
        String param = str_start + "&" + value + "&" + mode;
        //output format
        String output = "json";
        // api key
        String key = "key=AIzaSyCrN6atEBUzLR6Ubv7J53IwWp5GJ5KzBxs";
        // create url to request
        String url = "https://maps.googleapis.com/maps/api/geocode/" + output + "?" + param + "&" + key;
        Log.v("GEOCODER_API_URL", url);
        return url;
    }

    protected String requestDirectionAPI() throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(getRequestURL());
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
        Log.v("GEOCODER_JSON_RESPONSE", responseString);
        return responseString;
    }

}
