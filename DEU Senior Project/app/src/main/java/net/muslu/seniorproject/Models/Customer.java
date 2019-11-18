package net.muslu.seniorproject.Models;

public class Customer {

    protected int id, priority;
    protected String fullName,address,phone;

    public Customer(int id, int priority, String fullName, String address, String phone) {
        setId(id);
        setPriority(priority);
        setFullName(fullName);
        setAddress(address);
        setPhone(phone);
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFullName() {
        return fullName;
    }

    protected void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    protected void setPhone(String phone) {
        this.phone = phone;
    }
}
