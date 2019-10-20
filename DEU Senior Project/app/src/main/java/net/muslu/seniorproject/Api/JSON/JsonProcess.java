package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import org.json.JSONException;
import org.json.JSONObject;

public final class JsonProcess {

    public static BarcodeReadModel GetPackageInfo(String s){

        try {
            JSONObject pack = new JSONObject(s);
            return new BarcodeReadModel(pack.getInt("id"), pack.getString("fullName"), pack.getString("address"), pack.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
