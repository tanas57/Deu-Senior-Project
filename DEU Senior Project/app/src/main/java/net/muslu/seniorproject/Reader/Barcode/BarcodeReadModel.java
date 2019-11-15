package net.muslu.seniorproject.Reader.Barcode;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import net.muslu.seniorproject.Api.Geocoder.AddressToLatLng;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class BarcodeReadModel extends Application implements Serializable {

    protected static final String API_URL = "http://barcodes4.me/barcode/c128b/";

    protected String barcodeImgApiURL;
    protected long barcode;
    protected String customerFullName;
    protected String customerAddress;
    protected String customerPhone;

    public String getBarcodeImgApiURL() {
        return this.barcodeImgApiURL;
    }

    public void setBarcodeImgApiURL() {
        this.barcodeImgApiURL = API_URL + getBarcode() + ".jpg?IsTextDrawn=1&TextSize=22&resolution=2";
    }

    public BarcodeReadModel(long barcode, String customerFullName, String customerAddress, String customerPhone) {
       setBarcode(barcode);
       setCustomerFullName(customerFullName);
       setCustomerAddress(customerAddress);
       setCustomerPhone(customerPhone);
       try {
           //AddressToLatLng addressToLatLng = new AddressToLatLng(this);
           //addressToLatLng.GetLatLng();
           Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
           List<Address> latlng = null;
           latlng = geocoder.getFromLocationName(getCustomerAddress(), 1);
           double longitude = latlng.get(0).getLongitude();
           double latitude = latlng.get(0).getLatitude();
           Log.v("GECODER", "DÖNEN VERİLER: " + longitude + ", " + latitude);
       }
       catch (Exception e) { e.printStackTrace();}
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    protected void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    protected void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public long getBarcode() {
        return barcode;
    }

    protected void setBarcode(long barcode) {
        this.barcode = barcode;
        setBarcodeImgApiURL();
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    protected void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }
}
