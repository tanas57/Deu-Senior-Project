package net.muslu.seniorproject.Api;

public class AddressHelper implements IAddress {

    protected long barcode;
    protected ApiProcess apiProcess;
    protected String result;

    protected String getResult() {
        return result;
    }

    protected void setResult(String result) {
        this.result = result;
    }

    protected ApiProcess getApiProcess() {
        return apiProcess;
    }

    protected void setApiProcess(ApiProcess apiProcess) {
        this.apiProcess = apiProcess;
    }

    protected long getBarcode() {
        return barcode;
    }

    protected void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public AddressHelper() {
        setApi_address("http://api.muslu.net");
    }

    protected String api_address;

    protected String getApi_address() {
        return api_address;
    }

    protected void setApi_address(String api_address){ this.api_address = api_address;}


    @Override
    public String GetAddress() {
        return null;
    }


    public enum ApiProcess{
        GET_PACKAGES("/package/list"),
        GET_PACKAGE_BY_BARCODE("/package/getPackage/"),
        GET_CUSTOMER_BY_BARCODE("/package/customer/"),
        GET_CUSTOMER("/customer/"),
        GET_CUSTOMER_ALL("/customer/list"),
        SET_PACKAGE_STATUS("package/edit/");

        private String url;

        ApiProcess(String apiUrl) {
            this.url = apiUrl;
        }

        public String getUrl() {
            return url;
        }
    }
}
