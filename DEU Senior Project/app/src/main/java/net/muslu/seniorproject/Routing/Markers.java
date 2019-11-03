package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

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
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        int count = 0;
        List<Address> addresses = null;
        for (int i=0;i<markers.size();i++) {

            try {
                if(addresses!=null){
                    addresses = geocoder.getFromLocationName(markers.get(i).getCustomerAddress(), 1);
                }

                while (addresses == null) {
                    addresses = geocoder.getFromLocationName(markers.get(i).getCustomerAddress(), 1);
                    count++;
                    System.out.println("Count: "+count);
                    i=0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            double longitude = addresses.get(0).getLongitude();
            double latitude = addresses.get(0).getLatitude();
            LatLng pos = new LatLng(latitude, longitude);
            markerList.add((LatLng) pos);
            if (i == 0)
                mMap.addMarker(new MarkerOptions().position(pos).title("Başlangıç Adresi"));
            else if (i == addList.size() - 1)
                mMap.addMarker(new MarkerOptions().position(pos).title("Bitiş Adresi"));
            else
                mMap.addMarker(new MarkerOptions().position(pos).title("" + i + ". Teslimat Adresi")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        //Draw route

    }
}
