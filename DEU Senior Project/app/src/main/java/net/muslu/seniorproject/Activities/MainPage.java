package net.muslu.seniorproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Routing.MapsActivity;
import net.muslu.seniorproject.Routing.RoutingListAdapter;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    protected DataTransfer dataTransfer;
    protected int packageId = 1;
    public static final int REQUEST_CODE = 100;

    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        dataTransfer = new DataTransfer();
        packageId = dataTransfer.getPackageid();

        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
        final CardView progress, route, packets, add_package;

        progress = findViewById(R.id.home_progress);
        route = findViewById(R.id.home_route);
        packets = findViewById(R.id.home_packets);
        add_package = findViewById(R.id.home_add_package);

        route.setClickable(true);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Progress ekrandan kaldırıldı..", Toast.LENGTH_LONG).show();

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
                packetsIntent.putExtra("data", dataTransfer);
                startActivity(packetsIntent);

            }
        });

        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_package.startAnimation(shake);
                Intent camera = new Intent(getApplicationContext(), CameraActivity.class);
                camera.putExtra("data", dataTransfer);
                startActivityForResult(camera, REQUEST_CODE);
            }
        });

        CardView empty = findViewById(R.id.home_empty);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Make sure the request was successful-

            dataTransfer = (DataTransfer) data.getSerializableExtra("data");

            if(dataTransfer != null) {
                Log.v("BARKOD SIZE => ", " " + dataTransfer.getBarcodeData().GetSize());
            }
        }
    }

}
