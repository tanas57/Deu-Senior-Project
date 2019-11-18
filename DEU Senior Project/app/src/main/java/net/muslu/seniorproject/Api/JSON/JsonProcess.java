package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Models.Customer;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import org.json.JSONException;
import org.json.JSONObject;

public final class JsonProcess {
    Customer customer;
    public static BarcodeReadModel GetPackageInfo(String s, long barcode){
        try {
            JSONObject pack = new JSONObject(s);
            return new BarcodeReadModel(barcode, new Customer(pack.getInt("customerId"),pack.getInt("customerPriority"),pack.getString("customerFullName"), pack.getString("customerAddress"), pack.getString("customerPhone")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
