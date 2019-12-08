package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Markers {

    ArrayList<LatLng> markerList = new ArrayList<LatLng>();
    private ArrayList<String> addList = new ArrayList<String>();

    GoogleMap mMap;
    Context context;


    public void createMarkers(GoogleMap map,Context c , ArrayList<BarcodeReadModel> markers) throws IOException {
        mMap = map;
        context=c;

        String temp = "";
        for(BarcodeReadModel item: markers) {
            temp += item.getPackageId() + " ";
        }
        Log.v("MARKERS", temp);
        for (int i=0;i<markers.size();i++) {
            LatLng pos = new LatLng(markers.get(i).getLatitude(), markers.get(i).getLongitutde());
            markerList.add(pos);
            if (i == 0)
                mMap.addMarker(new MarkerOptions().position(pos).title("Başlangıç Adresi").icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_home)));
            else if (i == addList.size() - 1)
                mMap.addMarker(new MarkerOptions().position(pos).title("Bitiş Adresi").icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_pck)));
            else
                mMap.addMarker(new MarkerOptions().position(pos).title("" + i + ". Teslimat Adresi")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_pck)));
        }

        //Draw route

    }
}
