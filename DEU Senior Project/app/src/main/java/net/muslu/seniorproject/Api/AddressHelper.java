package net.muslu.seniorproject.Api;

public class AddressHelper {

    protected String api_address = "https://localhost:44362";

    protected String getApi_address() {
        return api_address;
    }

    public enum ApiProcess{
        GET_PACKAGES("/package/list"),
        GET_PACKAGE_BY_BARCODE("/package/"),
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
