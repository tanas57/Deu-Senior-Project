package net.muslu.seniorproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;

import java.io.InputStream;

public class MainPage extends AppCompatActivity {

    private de.hdodenhof.circleimageview.CircleImageView cargomanProfile;
    private TextView pkgsize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        if (Functions.takePermission(getApplicationContext(), this, Manifest.permission.ACCESS_FINE_LOCATION))
            Functions.fetchLastLocation(getApplicationContext());


        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake2);
        final CardView route, packets, add_package;

        final TextView cargomanFullname = findViewById(R.id.cargomanFullname);
        cargomanFullname.setText(Functions.getCargoman().getFullname());

        cargomanProfile = findViewById(R.id.profile_image);

        pkgsize = findViewById(R.id.main_packet_size);
        pkgsize.setText(" " + Functions.getPackageSize());

        new DownloadImageTask(cargomanProfile)
                .execute((Functions.API_URL + "/cargoman/image/" + Functions.getCargoman().getId()));

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
        pkgsize.setText(" " + Functions.getPackageSize());
        super.onResume();
    }
}