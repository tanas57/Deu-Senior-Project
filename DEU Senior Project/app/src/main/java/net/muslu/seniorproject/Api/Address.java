package net.muslu.seniorproject.Api;

public class Address extends AddressHelper implements IAddress{

    @Override
    public String GetAddress(ApiProcess apiProcess) {
        String tempUrl = getApi_address();

        switch (apiProcess){

            case GET_PACKAGES:
                tempUrl = tempUrl + apiProcess.GET_PACKAGE_BY_BARCODE.getUrl();
                break;

            case GET_CUSTOMER_ALL:
                tempUrl = tempUrl + apiProcess.GET_CUSTOMER_ALL.getUrl();
                break;
        }
        return  tempUrl;
    }
}
