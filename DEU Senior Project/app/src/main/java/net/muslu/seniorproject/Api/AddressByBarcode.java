package net.muslu.seniorproject.Api;

import android.util.Log;

public class AddressByBarcode extends AddressHelper implements IAddress {

    public AddressByBarcode(long barcode) {
        setBarcode(barcode);
        setApiProcess(ApiProcess.GET_CUSTOMER_BY_BARCODE);
    }

    public String GetAddress(){
        if(getApiProcess() != null && getBarcode() > 0) {
            setResult(getApi_address() + getApiProcess().getUrl() + getBarcode());

            return getResult();
        }
        Log.d("Error_addressByBarcode", "there is an error; it may be empty variable");
        return "error";
    }

}
