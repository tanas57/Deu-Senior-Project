package net.muslu.seniorproject.Activities;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Api.JSON.DMBelowTenPoint;
import net.muslu.seniorproject.Api.JSON.DMGreaterTenPoint;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.InputStream;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class MainPage extends AppCompatActivity {

    private de.hdodenhof.circleimageview.CircleImageView cargomanProfile;
    private TextView pkgsize;
    private boolean[] isChecked = new boolean[6];
    private ArrayList<AlgorithmType> algorithmTypes = new ArrayList<>();
    private String [] selectRoute ;
    private LatLng cargoman;
    private ViewGroup contentFrame;
    private void cargomanLocation(){
        if(Functions.takePermission(getApplicationContext(),this, Manifest.permission.ACCESS_FINE_LOCATION))
            Functions.fetchLastLocation(getApplicationContext());

        cargoman = new LatLng(Functions.getCargoman_lat(), Functions.getCargoman_lng());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        contentFrame = findViewById(R.id.home_content);
        if (Functions.takePermission(getApplicationContext(), this, Manifest.permission.ACCESS_FINE_LOCATION))
            Functions.fetchLastLocation(getApplicationContext());


        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
        final CardView route, packets, add_package, create_route;

        final TextView cargomanFullname = findViewById(R.id.cargomanFullname);
        cargomanFullname.setText(Functions.getCargoman().getFullname());

        cargomanProfile = findViewById(R.id.profile_image);

        pkgsize = findViewById(R.id.cargomanPacketsize);
        pkgsize.setText("Bugün taşınması gereken paket sayısı : " + (Functions.getPackageSize()));

        new DownloadImageTask(cargomanProfile)
                .execute((Functions.API_URL + "/cargoman/image/" + Functions.getCargoman().getId()));

        route = findViewById(R.id.home_route);
        packets = findViewById(R.id.home_packets);
        add_package = findViewById(R.id.home_add_package);
        create_route = findViewById(R.id.home_create_route);

        route.setClickable(true);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.startAnimation(shake);
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                startActivity(intent);
            }
        });

        packets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packets.startAnimation(shake);
                Toast.makeText(getApplicationContext(), "Paketler açılıyor..", Toast.LENGTH_SHORT).show();
                Intent packetsIntent = new Intent(getApplicationContext(), PacketsActivity.class);
                startActivity(packetsIntent);
            }
        });

        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fetchLastLocation(getApplicationContext());
                add_package.startAnimation(shake);
                Intent camera = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(camera);
            }
        });


        create_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_route.startAnimation(shake);
                Blurry.with(getApplicationContext()).sampling(11).radius(44).onto(contentFrame);

                selectRoute= getResources().getStringArray(R.array.route_selection);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainPage.this,R.style.MyAlertDialogHome);

                mBuilder.setTitle("Rota hesaplama türünü seçiniz: ");
                mBuilder.setCancelable(false);

                mBuilder.setMultiChoiceItems(selectRoute, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            algorithmTypes.add(Functions.getReturnedType(which));
                        }
                        else if (algorithmTypes.contains(Functions.getReturnedType(which))) {
                            algorithmTypes.remove(Functions.getReturnedType(which));
                        }
                    }
                });

                mBuilder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Blurry.delete(contentFrame);

                        cargomanLocation(); // update cargoman location

                        if(Functions.getPackageSize()>0) {
                            if (Functions.getPackageSize() > 9) {
                                DMGreaterTenPoint dmGreaterTenPoint = new DMGreaterTenPoint(MainPage.this, Functions.getPackets(), algorithmTypes);
                                dmGreaterTenPoint.setCargoman(new BarcodeReadModel(0, cargoman.latitude, cargoman.longitude, getApplicationContext()));
                                dmGreaterTenPoint.Execute();
                            } else {

                                DMBelowTenPoint dmBelowTenPoint = new DMBelowTenPoint(MainPage.this, Functions.getPackets(), algorithmTypes);
                                dmBelowTenPoint.setCargoman(new BarcodeReadModel(0, cargoman.latitude, cargoman.longitude, getApplicationContext()));
                                dmBelowTenPoint.Execute();
                            }

                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Bu işlemden önce paket eklenmesi gerekmektedir", Toast.LENGTH_LONG).show();
                        }

                    }
                })  ;
                AlertDialog myDiaolog = mBuilder.create();
                myDiaolog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
                myDiaolog.show();

                myDiaolog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Blurry.delete(contentFrame);
                    }
                });

            }
        });



    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        de.hdodenhof.circleimageview.CircleImageView bmImage;

        public DownloadImageTask(de.hdodenhof.circleimageview.CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onResume() {
        pkgsize.setText("Bugün taşınması gereken paket sayısı : " + Functions.getPackageSize());
        super.onResume();
    }
}