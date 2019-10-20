package net.muslu.seniorproject.Reader.Barcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.net.URL;

public final class BarcodeIMG {

    public static Bitmap Result(String url){
        try {
            URL imgUrl = new URL(url);
            Bitmap bmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
            return bmp;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
