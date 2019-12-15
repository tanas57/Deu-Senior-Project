package net.muslu.seniorproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import net.muslu.seniorproject.R;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        final CardView progress, route, packets;

        progress = findViewById(R.id.home_progress);
        route = findViewById(R.id.home_route);
        packets = findViewById(R.id.home_packets);


        route.setClickable(true);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"progress ekrandan kaldırıldı", Toast.LENGTH_LONG).show();
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
                route.startAnimation(shake);

                if(progress.getParent() != null)
                    ((ViewManager)progress.getParent()).removeView(progress);


            }
        });

        packets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
