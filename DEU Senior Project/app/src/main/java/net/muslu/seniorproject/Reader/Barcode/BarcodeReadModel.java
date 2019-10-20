package net.muslu.seniorproject.Reader.Barcode;


import android.graphics.Bitmap;
import android.util.Log;

import java.text.MessageFormat;

public class BarcodeReadModel {

    protected static final String API_URL = "http://barcodes4.me/barcode/c128b/";

    protected String barcodeImgApiURL;
    protected long barcode;
    protected String customerFullName;
    protected String customerAddress;
    protected String customerPhone;

    public Bitmap getBarcodeImg() {
        return barcodeImg;
    }

    public void setBarcodeImg(Bitmap barcodeImg) {
        this.barcodeImg = barcodeImg;
        Log.v("BARCODE IMG GENERATION", "image generated to a new bitmap object");
    }

    protected Bitmap barcodeImg;

    public String getBarcodeImgApiURL() {
        return this.barcodeImgApiURL;
    }

    public void setBarcodeImgApiURL() {
        this.barcodeImgApiURL = API_URL + getBarcode() + ".jpg?IsTextDrawn=1&TextSize=13&resolution=2";
    }

    public BarcodeReadModel(long barcode, String customerFullName, String customerAddress, String customerPhone) {
       setBarcode(barcode);
       setCustomerFullName(customerFullName);
       setCustomerAddress(customerAddress);
       setCustomerPhone(customerPhone);
       setBarcodeImg(BarcodeIMG.Result(getBarcodeImgApiURL()));
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
