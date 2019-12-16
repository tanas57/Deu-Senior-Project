package net.muslu.seniorproject.Routing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import net.muslu.seniorproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Route {
    GoogleMap mMap;
    Context context;

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, boolean withIndications, boolean optimize)
    {
        mMap = map;
        context = c;
         if(points.size() > 2)
        {
            ArrayList<LatLng> url = new ArrayList<>();

            for(int i = 0; i < points.size(); i++){
                url.add(points.get(i));

                if( (i % 24 == 0 && i != 0) ||(i == points.size()-1) ){
                    String url2 = makeURL(url,"driving",optimize);
                    new connectAsyncTask(url2,withIndications).execute();
                    url = new ArrayList<>();
                    url.add(points.get(i)); // destinatons is going to be origin
                }
            }

            return true;
        }
        return false;
    }

    private String makeURL (ArrayList<LatLng> points, String mode, boolean optimize){
        String api_key = context.getString(R.string.google_maps_key);
        String directionMode="driving";
        String output = "json";
        // Building the url to the web service

        String str_origin = "origin=" + points.get(0).latitude + "," + points.get(0).longitude;
        // Destination of route
        String str_dest = "destination=" + points.get(points.size()-1).latitude + "," + points.get(points.size()-1).longitude;

        // Mode
        mode = "mode=" + directionMode;
        // Building the parameters to the web service

        StringBuilder urlString = new StringBuilder();

        urlString.append("&waypoints=");
        if(optimize)
            urlString.append( points.get(1).latitude);
        urlString.append(',');
        urlString.append(points.get(1).longitude);

        for(int i=2;i<points.size()-1;i++)
        {
            urlString.append('|');
            urlString.append( points.get(i).latitude);
            urlString.append(',');
            urlString.append(points.get(i).longitude);
        }
        String parameters = str_origin + "&" + str_dest + "&" + mode + urlString.toString();
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + api_key;
        //Log.v("URL:",url);
        Log.v("DIRECTION API URL:",url);
        return url;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
    
	private class connectAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        boolean steps;
        connectAsyncTask(String urlPass, boolean withSteps){
            url = urlPass;
            steps = withSteps;

        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.waiting_route_draw));
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result,steps);
            }
        }
    }

    private void drawPath(String  result, boolean withSteps) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(15)
                        .color(Color.rgb(240,140,40)).geodesic(true));
            }

        }
        catch (JSONException e) {
        }
    }

}
