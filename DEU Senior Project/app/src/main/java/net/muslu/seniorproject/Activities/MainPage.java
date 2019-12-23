package net.muslu.seniorproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        if(Functions.takePermission(getApplicationContext(),this, Manifest.permission.ACCESS_FINE_LOCATION))
            Functions.fetchLastLocation(getApplicationContext());


        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
        final CardView route, packets, add_package;

        route = findViewById(R.id.home_route);
        packets = findViewById(R.id.home_packets);
        add_package = findViewById(R.id.home_add_package);

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
    }
}
