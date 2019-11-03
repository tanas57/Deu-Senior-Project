package net.muslu.seniorproject.Map;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Reader.Barcode.BarcodeRead;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
/*
public class FetchCoordinate extends AsyncTask<Object, Object,Object> {

    protected LatLng latLng;
    protected BarcodeReadModel model;
    public FetchCoordinate(){
        latLng = null;
    }

    @Override
    /*
    protected String doInBackground(Object... params) {

        model = (BarcodeReadModel) params[1];
        Geocoder geocoder = new Geocoder(BarcodeRead.mapsActivity, Locale.getDefault());

        List<Address> addresses = null;

            try {
                if(addresses!=null){
                    addresses = geocoder.getFromLocationName((String)params[0], 1);
                }

                while (addresses == null) {
                    addresses = geocoder.getFromLocationName((String)params[0], 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        double longitude = addresses.get(0).getLongitude();
        double latitude = addresses.get(0).getLatitude();

        latLng = new LatLng(latitude, longitude);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        //model.setCoordinate(new Coordinate(latLng));
    }
}
*/