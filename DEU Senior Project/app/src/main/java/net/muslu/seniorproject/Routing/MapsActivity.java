package net.muslu.seniorproject.Routing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import net.muslu.seniorproject.Activities.DeliveryActivity;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private BarcodeData data;
    private GoogleMap mMap;
    private boolean cont = true;

    public void countdown(final ICallback callback, final int time)
    {
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable(){
            @Override
            public void run() {
                callback.onEndTime();
                handler.postDelayed(this, time);
                if(cont){

                    Functions.fetchLastLocation(getMapsActivityContext());

                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(new LatLng(Functions.getCargoman_lat(),Functions.getCargoman_lng()))
                            .zoom(20)
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

    ArrayList<BarcodeReadModel> addList = new ArrayList<BarcodeReadModel>();
    Markers marker = new Markers();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        data = (BarcodeData) getIntent().getSerializableExtra("data");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }

        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(Functions.getCargoman_lat(),Functions.getCargoman_lng())) // will be cargoman coords
                .zoom(10)
                .bearing(0)
                .tilt(45)
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
                cont = true;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                cont = false;
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

