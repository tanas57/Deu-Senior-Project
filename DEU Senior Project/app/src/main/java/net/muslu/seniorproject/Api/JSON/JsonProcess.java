package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import org.json.JSONException;
import org.json.JSONObject;

public final class JsonProcess {

    public static BarcodeReadModel GetPackageInfo(String s, long barcode){

        try {
            JSONObject pack = new JSONObject(s);
            return new BarcodeReadModel(barcode, pack.getString("fullName"), pack.getString("address"), pack.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
