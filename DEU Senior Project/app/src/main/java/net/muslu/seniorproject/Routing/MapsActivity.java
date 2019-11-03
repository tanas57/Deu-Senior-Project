package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private BarcodeData data;


    private GoogleMap mMap;
    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    ArrayList<BarcodeReadModel> addList = new ArrayList<BarcodeReadModel>();
    Markers marker = new Markers();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        data = (BarcodeData)getIntent().getSerializableExtra("data");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(38.3749,27.1872))
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        addList = data.GetData();
        try {
            marker.createMarkers(mMap,this,addList);
            Route d = new Route();
            d.drawRoute(mMap,this,marker.markerList,true,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Context getMapsActivityContext() {
        return MapsActivity.this;
    }

}

