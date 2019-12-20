package net.muslu.seniorproject;

import net.muslu.seniorproject.Algorithm.Chromosome;

import java.util.ArrayList;

public class Functions {

    private static ArrayList<Chromosome> routes = new ArrayList<>();

    public static ArrayList<Chromosome> getRoutes() {
        return routes;
    }

    public static void setRoutes(ArrayList<Chromosome> routes) {
        Functions.routes = routes;
    }

    public static void addRoute(Chromosome chromosome) {
        routes.add(chromosome);
    }
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Functions {

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

    public static final String CHANNEL_ID = "channel";
    private static NotificationManagerCompat notificationManager;



    public static void sendNotification(int maxIteration,  int count , Context context) {


        final NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_black_24dp)
                .setContentTitle("Rota")
                .setContentText("Rotanız hesaplanıyor ..")
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(maxIteration, count, false);
        notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(2, notification.build());




        if(maxIteration-1<=count) {
            notification.setContentText("Rota hesaplama işlemi tamamlandı.")
                     .setSmallIcon(R.drawable.ic_done_black_24dp)
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            notificationManager.notify(2, notification.build());
        }

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = count;

                    notification.setProgress(progressMax, progress, false);
                    notificationManager.notify(2, notification.build());
                    SystemClock.sleep(350);

                notification.setContentText("Rota hesaplama işlemi tamamlandı.")
                        .setProgress(0, 0, false)
                        .setOngoing(false);
                notificationManager.notify(2, notification.build());
            }
        }).start();*/
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is Channel");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }


}
