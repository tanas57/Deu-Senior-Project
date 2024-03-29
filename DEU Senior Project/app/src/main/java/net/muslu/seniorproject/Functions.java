package net.muslu.seniorproject;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.Models.Cargoman;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Functions {
    private static Location currentLocation;
    private static FusedLocationProviderClient fusedLocationProviderClient;
    public static final String API_URL = "https://api.muslu.net";
    private static ArrayList<Chromosome> routes = new ArrayList<>();
    private static BarcodeData barcodeData = new BarcodeData();



    private static GoogleMap map ;
    private static int packageid = 1;
    public static final String CHANNEL_ID = "Rota İşlem Bildirimleri";
    private static NotificationManagerCompat notificationManager;
    private static int selectedRoute = -1;
    public static int getSelectedRoute() {
        return selectedRoute;
    }
    public static void setSelectedRoute(int pos) {
        selectedRoute = pos;
    }
    protected static Cargoman cargoman;

    public static Cargoman getCargoman() {
        return cargoman;
    }

    public static void setCargoman(Cargoman cargoman) {
        Functions.cargoman = cargoman;
    }

    public static boolean takePermission(Context context, Activity activity, String per){
        if(ContextCompat.checkSelfPermission(context, per)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String []{per},1);
            return false;
        }
        else return true;
    }

    public static int getPackageSize(){ return barcodeData.GetData().size(); }

    public static double getCargoman_lat() {
        return cargoman.getLatitude();
    }

    public static void setCargoman_lat(double cargoman_lat) {
        Functions.cargoman.setLatitude(cargoman_lat);
    }

    public static GoogleMap getMap() {
        return map;
    }

    public static void setMap(GoogleMap map) {
        Functions.map = map;
    }

    public static double getCargoman_lng() {
        return cargoman.getLongitude();
    }

    public static void setCargoman_lng(double cargoman_lng) {
        Functions.cargoman.setLongitude(cargoman_lng);
    }

    public static int getPackageid() {
        return packageid;
    }

    public static void setPackageid(int packageid) {
        Functions.packageid = packageid;
    }

    public static BarcodeData getPackets() {
        return barcodeData;
    }

    public static void setPackets(ArrayList<BarcodeReadModel> packets) {
        Functions.barcodeData.setData(packets);
    }

    public static boolean addPacket(BarcodeReadModel model){
        return barcodeData.AddData(model);
    }

    public static boolean remPacket(int pos) { return barcodeData.removeData(pos); }

    public static ArrayList<Chromosome> getRoutes() {
        return routes;
    }

    public static void setRoutes(ArrayList<Chromosome> routes) {
        Functions.routes = routes;
    }

    public static void addRoute(Chromosome chromosome) {
        routes.add(chromosome);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                try {
                    URL url = new URL("http://www.google.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("User-Agent", "test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000); // mTimeout is in seconds
                    urlc.connect();
                    if ((urlc.getResponseCode() <= 200)&&((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)||(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE))) {
                        return true;
                    } else {
                        return false;
                    }
                }
                catch (IOException e)
                {
                    Log.i("warning", "Error checking internet connection", e);
                    return false;
                }
            }
        }
        return false;
    }

    public static void sendNotification(int maxIteration,  int count , Context context, String name) {

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_black_24dp)
                .setContentTitle("Rota")
                .setContentText("Rotanız hesaplanıyor ..")
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(maxIteration, count, false);
        notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(Integer.valueOf(name), notification.build());

        if(maxIteration-2<=count) {
            notification.setContentText("Rota hesaplama işlemi tamamlandı.")
                     .setSmallIcon(R.drawable.ic_done_black_24dp)
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            notificationManager.notify(Integer.valueOf(name), notification.build());
        }
    }

    public static void createNotificationChannel(Context context, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is Channel");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Log.v("NOTIFICATION", "IS CREATED IN CHANNEL " + name);
        }
    }

    public static void fetchLastLocation(final Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    setCargoman_lat(location.getLatitude());
                    setCargoman_lng(location.getLongitude());
                    Log.v("CARGOMAN LOCATION", currentLocation.getLatitude()+" "+currentLocation.getLongitude());
                }
            }
        });
    }

    public static void changeMarkerIcon(int index, ArrayList<LatLng>mList , boolean status){

        if(!status){
            getMap().addMarker(new MarkerOptions().position(mList.get(index)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_no)));

            if(index!=mList.size()-1)
                getMap().addMarker(new MarkerOptions().position(mList.get(index+1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.next_loc)));

        }

        else if(status){
            getMap().addMarker(new MarkerOptions().position(mList.get(index)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_yes)));

            if(index!=mList.size()-1)
                getMap().addMarker(new MarkerOptions().position(mList.get(index+1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.next_loc)));

        }




    }
}
