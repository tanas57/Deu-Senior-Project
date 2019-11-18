package net.muslu.seniorproject.Api;

import android.util.Log;

public class PackageByBarcode extends AddressHelper implements IAddress {

    protected ApiProcess apiProcess;

    public PackageByBarcode(long barcode) {
        setBarcode(barcode);
        setApiProcess(ApiProcess.GET_PACKAGE_BY_BARCODE);
    }

    public String GetAddress(){
        if(getApiProcess() != null && getBarcode() > 0) {
            setResult(getApi_address() + getApiProcess().getUrl() + getBarcode());

            return getResult();
        }
        Log.d("Error_PackageByBarcode", "there is an error; it may be empty variable");
        return "error";
    }

}
