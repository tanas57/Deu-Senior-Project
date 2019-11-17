package net.muslu.seniorproject.Models;

public class Customer {

    protected int customerId,customerPriority;
    protected String customerFullName,customerAddress,customerPhone;

    public Customer(int customerId, int customerPriority, String customerFullName, String customerAddress, String customerPhone) {
        setCustomerId(customerId);
        this.customerPriority = customerPriority;
        this.customerFullName = customerFullName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerPriority() {
        return customerPriority;
    }

    public void setCustomerPriority(int customerPriority) {
        this.customerPriority = customerPriority;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }



}
