package net.muslu.seniorproject.Reader.Barcode;

public class BarcodeReadModel {

    protected int barcode;
    protected String customerFullName;
    protected String customerAddress;
    protected String customerPhone;

    public BarcodeReadModel(int barcode, String customerFullName, String customerAddress, String customerPhone) {
       setBarcode(barcode);
       setCustomerFullName(customerFullName);
       setCustomerAddress(customerAddress);
       setCustomerPhone(customerPhone);
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    protected void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    protected void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getBarcode() {
        return barcode;
    }

    protected void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    protected void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }
}
