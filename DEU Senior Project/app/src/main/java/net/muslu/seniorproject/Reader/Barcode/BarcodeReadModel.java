package net.muslu.seniorproject.Reader.Barcode;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Models.CargoPackage;
import net.muslu.seniorproject.Models.Customer;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class BarcodeReadModel extends Application implements Serializable {

    protected static final String API_URL = "http://barcodes4.me/barcode/c128b/";

    protected String barcodeImgApiURL;
    protected long barcode;

    protected CargoPackage cargoPackage;
    protected int packageId;
    protected double latitude;
    protected double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitutde() {
        return longitude;
    }

    public CargoPackage getCargoPackage() {
        return cargoPackage;
    }

    public void setCargoPackage(CargoPackage cargoPackage) {
        this.cargoPackage = cargoPackage;
    }

    public Customer getCustomer() {
        return getCargoPackage().getCustomer();
    }

    public String getLatLng() {
        return latitude + "," + this.longitude;
    }

    protected void setLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getBarcodeImgApiURL() {
        return this.barcodeImgApiURL;
    }

    public void setBarcodeImgApiURL() {
        this.barcodeImgApiURL = API_URL + getBarcode() + ".jpg?IsTextDrawn=1&TextSize=22&resolution=2";
    }

    public BarcodeReadModel(long barcode, CargoPackage cargoPackage) {
       setBarcode(barcode);
       setCargoPackage(cargoPackage);

       try {
           Geocoder geocoder = new Geocoder(BarcodeReadModel.this, Locale.getDefault());
           List<Address> latlng = null;
           latlng = geocoder.getFromLocationName(getCustomer().getAddress(), 1);
           double longitude = latlng.get(0).getLongitude();
           double latitude = latlng.get(0).getLatitude();
           setLatLng(latitude, longitude);
           Log.v("GECODER", "LONG LAT RESULT : (" + latitude + "," + longitude + ")");
       }
       catch (Exception e) { e.printStackTrace();}
    }

    public BarcodeReadModel(long barcode, double latitude, double longitude) {
        setBarcode(barcode);
        setCargoPackage(null);
        setLatLng(latitude, longitude);
        setPackageId(0);
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
