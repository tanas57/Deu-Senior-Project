package net.muslu.seniorproject.Map;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

public class Coordinate implements ICoordinate {

    protected List<Address> addresses;

    public Coordinate() {
        this.addresses = new ArrayList<Address>();
    }

    protected List<Address> getAddresses() {
        return addresses;
    }

    protected void addAddress(Address address) {
        this.addresses.add(address);
    }

    @Override
    public void GetAddress(String address) {

    }

    @Override
    public Address GetGeoCoordinates() {
        return null;
    }

    @Override
    public List<Address> GetAllAddresses() {
        return null;
    }
}
