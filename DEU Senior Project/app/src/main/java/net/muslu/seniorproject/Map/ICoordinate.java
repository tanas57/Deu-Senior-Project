package net.muslu.seniorproject.Map;

import android.location.Address;
import java.util.List;

public interface ICoordinate {

    void GetAddress(String address);
    Address GetGeoCoordinates();
    List<Address> GetAllAddresses();
}
