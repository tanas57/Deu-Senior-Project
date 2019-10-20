package net.muslu.seniorproject.Reader.Barcode;

import net.muslu.seniorproject.R;

import java.util.ArrayList;

public class BarcodeData {

    protected ArrayList<BarcodeReadModel> data;

    public BarcodeData() {
        this.data = new ArrayList<BarcodeReadModel>();
    }

    public void AddData(int barcode, String fullname, String address, String phone){
        this.data
                .add(new BarcodeReadModel(
                        barcode,
                        fullname,
                        address,
                        phone
                ));
    }

    public BarcodeReadModel getDataByID(int id){
        return data.get(id);
    }

    public ArrayList<BarcodeReadModel> GetData(){ return this.data; }
    
    public int GetSize() { return this.data.size(); }
}
