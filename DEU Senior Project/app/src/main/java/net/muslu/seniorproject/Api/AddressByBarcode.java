package net.muslu.seniorproject.Api;

import android.util.Log;

public class AddressByBarcode extends AddressHelper implements IAddress {

    public AddressByBarcode(ApiProcess apiProcess, long barcode) {
        setBarcode(barcode);
        setApiProcess(apiProcess);
    }

    public String GetAddress(){
        if(getApiProcess() != null && getBarcode() > 0) {
            setResult(getApi_address() + getApiProcess().getUrl() + getBarcode());

            return getResult();
        }
        Log.v("error_addressByBarcode", "there is an error; it may be empty variable");
        return "error";
    }

}
