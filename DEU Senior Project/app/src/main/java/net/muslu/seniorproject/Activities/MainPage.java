package net.muslu.seniorproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.ProjectData;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;

public class MainPage extends AppCompatActivity implements ProjectData {

    protected BarcodeData barcodeData;
    protected int packageId = 1;
    private LatLng cargoman;
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        barcodeData = new BarcodeData();

        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
        final CardView progress, route, packets;

        progress = findViewById(R.id.home_progress);
        route = findViewById(R.id.home_route);
        packets = findViewById(R.id.home_packets);


        route.setClickable(true);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"progress ekrandan kaldırıldı", Toast.LENGTH_LONG).show();

                route.startAnimation(shake);

                if(progress.getParent() != null)
                    ((ViewManager)progress.getParent()).removeView(progress);


            }
        });

        packets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packets.startAnimation(shake);
                Toast.makeText(getApplicationContext(), "Paketler açılıyor..", Toast.LENGTH_SHORT).show();
                Intent packetsIntent = new Intent(getApplicationContext(), PacketsActivity.class);
                packetsIntent.putExtra("data", getBarcodeData());
                startActivity(packetsIntent);

            }
        });

    }

    @Override
    public BarcodeData getBarcodes() {
        return getBarcodeData();
    }

    public BarcodeData getBarcodeData() {
        return barcodeData;
    }
    public void setBarcodeData(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
    }
}
