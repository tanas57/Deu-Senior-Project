package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
                mMap.addMarker(new MarkerOptions().position(pos).title("Başlangıç Adresi"));
            else if (i == addList.size() - 1)
                mMap.addMarker(new MarkerOptions().position(pos).title("Bitiş Adresi"));
            else
                mMap.addMarker(new MarkerOptions().position(pos).title("" + i + ". Teslimat Adresi : lat/lng " + markers.get(i).getLatitude() + " " + markers.get(i).getLongitutde() + markers.get(i).getCustomer().getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        //Draw route

    }
}
