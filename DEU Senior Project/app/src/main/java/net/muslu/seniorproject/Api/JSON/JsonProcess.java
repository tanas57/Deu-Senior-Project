package net.muslu.seniorproject.Api.JSON;

import net.muslu.seniorproject.Models.Customer;
import net.muslu.seniorproject.Models.CargoPackage;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import org.json.JSONException;
import org.json.JSONObject;

public final class JsonProcess {
    Customer customer;
    public static BarcodeReadModel GetPackageInfo(String s, long barcode){
        try {
            JSONObject pack = new JSONObject(s);
            JSONObject cus = pack.getJSONObject("customer");
            Customer customer = new Customer(
                    cus.getInt("id"),
                    1, // cus.getInt("customerPriority")
                    cus.getString("fullName"),
                    cus.getString("address"),
                    cus.getString("phone"));
            CargoPackage newPackage = new CargoPackage(
                    pack.getInt("id"),
                    barcode,
                    pack.getDouble("packageWeigth"),
                    pack.getDouble("packageDesi"),
                    null,
                    null,
                    customer,
                    null);

            return new BarcodeReadModel(barcode, newPackage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
