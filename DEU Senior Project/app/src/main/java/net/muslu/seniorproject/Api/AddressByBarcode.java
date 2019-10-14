package net.muslu.seniorproject.Api;

public class AddressByBarcode extends AddressHelper implements IAddressByBarcode {

    public String AddressByBarcode(ApiProcess apiProcess, long barcode){
        String tempUrl = getApi_address();

        switch (apiProcess){
            case GET_CUSTOMER_BY_BARCODE:
                tempUrl = tempUrl + apiProcess.GET_CUSTOMER_BY_BARCODE.getUrl() + barcode;
                break;

            case GET_PACKAGE_BY_BARCODE:
                tempUrl = tempUrl + apiProcess.GET_PACKAGE_BY_BARCODE.getUrl() + barcode;
                break;
        }
        return  tempUrl;
    }
}
