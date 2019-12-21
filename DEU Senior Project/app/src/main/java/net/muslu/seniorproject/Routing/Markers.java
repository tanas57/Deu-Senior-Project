package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Markers {

    ArrayList<LatLng> markerList = new ArrayList<LatLng>();
    private ArrayList<String> addList = new ArrayList<String>();

    GoogleMap mMap;
    Context context;


    public void createMarkers(GoogleMap map, final MapsActivity activity, Context c , ArrayList<BarcodeReadModel> markers) throws IOException {
        mMap = map;
        context=c;

        if(mMap != null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }
                public View getInfoContents(final Marker marker) {
                    String[] toGetDeliverOrder = marker.getId().split("m");
                    int deliverOrder = Integer.valueOf(toGetDeliverOrder[1]);
                    Chromosome ch = Functions.getRoutes().get(0);
                    BarcodeReadModel model = Functions.getRoutes().get(0).getBarcodeReadModels().get(deliverOrder);
                    View view = activity.getLayoutInflater().inflate(R.layout.snippet_view,null);

                    TextView deliveryNumber = view.findViewById(R.id.mapMarker_deliveryNumber);
                    TextView fullName = view.findViewById(R.id.mapMarker_custFullName);
                    TextView custPriority = view.findViewById(R.id.mapMarker_custPriority);
                    TextView addressDetail = view.findViewById(R.id.mapMarker_address);
                    Button deliverButton = view.findViewById(R.id.mapMarker_deliveryButton);

                    deliveryNumber.setText(deliverOrder+". Teslimat Adresi");
                    fullName.setText(model.getCustomer().getFullName()+" - ");
                    custPriority.setText("Müşteri Önceliği: "+model.getCargoPackage().getPriority());
                    addressDetail.setText(model.getCustomer().getAddress());


                    return view;
                }
            });
        }

        String temp = "";
        for(BarcodeReadModel item: markers) {
            temp += item.getPackageId() + " ";
        }
        Log.v("MARKERS", temp);
        for (int i=0;i<markers.size();i++) {
            LatLng pos = new LatLng(markers.get(i).getLatitude(), markers.get(i).getLongitutde());
            markerList.add(pos);
            if (i == 0)
                mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_home)));
            else if (i == addList.size() - 1)
                mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_pck)));
            else
                mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.mipmap.loc_pck)));
        }

        //Draw route

    }
}
