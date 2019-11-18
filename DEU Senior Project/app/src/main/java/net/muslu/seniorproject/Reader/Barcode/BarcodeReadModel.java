package net.muslu.seniorproject.Reader.Barcode;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Models.Customer;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class BarcodeReadModel extends Application implements Serializable {

    protected static final String API_URL = "http://barcodes4.me/barcode/c128b/";

    protected String barcodeImgApiURL;
    protected long barcode;
    protected Customer customer;
    protected int packageId;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    protected LatLng latLng;

    public String getLatLng() {
        return latLng.latitude + "," + latLng.longitude;
    }


    protected void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getBarcodeImgApiURL() {
        return this.barcodeImgApiURL;
    }

    public void setBarcodeImgApiURL() {
        this.barcodeImgApiURL = API_URL + getBarcode() + ".jpg?IsTextDrawn=1&TextSize=22&resolution=2";
    }

    public BarcodeReadModel(long barcode, Customer customer) {
       setBarcode(barcode);
       setCustomer(customer);

       try {
           Geocoder geocoder = new Geocoder(BarcodeReadModel.this, Locale.getDefault());
           List<Address> latlng = null;
           latlng = geocoder.getFromLocationName(customer.getCustomerAddress(), 1);
           double longitude = latlng.get(0).getLongitude();
           double latitude = latlng.get(0).getLatitude();
           setLatLng(new LatLng(latitude, longitude));
           Log.v("GECODER", "LONG LAT RESULT : (" + latitude + "," + longitude + ")");
       }
       catch (Exception e) { e.printStackTrace();}
    }

    public long getBarcode() {
        return barcode;
    }

    protected void setBarcode(long barcode) {
        this.barcode = barcode;
        setBarcodeImgApiURL();
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

}
