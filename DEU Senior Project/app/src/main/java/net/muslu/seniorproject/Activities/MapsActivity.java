package net.muslu.seniorproject.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import net.muslu.seniorproject.Routing.Markers;
import net.muslu.seniorproject.Routing.Route;
import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BarcodeData data;
    private GoogleMap mMap;
    final Handler handler = new Handler();
    private boolean start = false;
    private boolean pause = false;

    public void countdown(final ICallback callback, final int time)
    {
        handler.postDelayed( new Runnable(){
            @Override
            public void run() {
                callback.onEndTime();
                handler.postDelayed(this, time);
                if(start && !pause){

                    Functions.fetchLastLocation(getMapsActivityContext());

                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(new LatLng(Functions.getCargoman_lat(),Functions.getCargoman_lng()))
                            .zoom(18)
                            .bearing(0)
                            .tilt(20)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 555, null);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                }
            }
        }, time);
    }

    public interface ICallback{
        void onEndTime();
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    ArrayList<BarcodeReadModel> addList = new ArrayList<>();
    Markers marker = new Markers();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        data = new BarcodeData();
        data.setData(Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels());

        if(data.GetSize() > 1) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            getSupportActionBar().setTitle(getString(R.string.my_route));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.route_start){
            if(start){
                start = false;
                Toast.makeText(getMapsActivityContext(), "durduruldu", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_play_arrow);
            }
            else{
                start = true;
                item.setIcon(R.drawable.ic_pause);
                Toast.makeText(getMapsActivityContext(), "başlatıldı", Toast.LENGTH_SHORT).show();
            }

        }
        else if(id == R.id.route_stop){
            start = false;
            Toast.makeText(getMapsActivityContext(), "iptal edildi.", Toast.LENGTH_SHORT).show();
        }
        else{
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        if(mMap != null) start = true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(Functions.getCargoman_lat(),Functions.getCargoman_lng())) // will be cargoman coords
                .zoom(14)
                .bearing(0)
                .tilt(55)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        addList = data.GetData();
        try {
            marker.createMarkers(mMap,this,getMapsActivityContext(),addList);
            Route d = new Route();
            d.drawRoute(mMap,this,marker.markerList,true,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        countdown(new ICallback() {
            @Override
            public void onEndTime() {

            }
        }, 1233);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pause = false;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                pause = true;
                return false;
            }

        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getMapsActivityContext(), DeliveryActivity.class);
                String[] parse = marker.getId().split("m");
                intent.putExtra("model_id", Integer.valueOf(parse[1]));
                startActivity(intent);
            }
        });

    }

    public Context getMapsActivityContext() {
        return MapsActivity.this;
    }

}

