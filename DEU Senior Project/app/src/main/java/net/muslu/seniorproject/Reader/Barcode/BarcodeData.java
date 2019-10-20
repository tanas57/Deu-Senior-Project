package net.muslu.seniorproject.Reader.Barcode;

import java.util.ArrayList;

public class BarcodeData {

    protected ArrayList<BarcodeReadModel> data;

    public BarcodeData() {
        this.data = new ArrayList<BarcodeReadModel>();
    }

    public boolean AddData(BarcodeReadModel barcodeReadModel){

        if(barcodeReadModel == null) return false;

        for (int i = 0; i < GetSize(); i++){
            if(getDataByID(i).getBarcode() == barcodeReadModel.getBarcode())
                return false;
        }

        data.add(barcodeReadModel);

        return true;
    }

    public boolean AddData(int barcode, String fullname, String address, String phone){

        boolean isAdded = false;
        for(int i = 0; i < GetSize(); i++){
            if(getDataByID(i).getBarcode() == barcode){
                isAdded = true;
                break;
            }
        }
        if(!isAdded){
            this.data
                    .add(new BarcodeReadModel(
                            barcode,
                            fullname,
                            address,
                            phone
                    ));
            return true;
        }

        return false;
    }

    public boolean RemoveData(int barcode){

        for(int i = 0; i < GetSize(); i++){
            BarcodeReadModel temp = getDataByID(i);
            if(temp.getBarcode() == barcode){
                data.remove(temp);
                return true;
            }
        }
        return false;
    }

    public BarcodeReadModel getDataByID(int id){
        return data.get(id);
    }

    public ArrayList<BarcodeReadModel> GetData(){ return this.data; }

    public int GetSize() { return this.data.size(); }
}
