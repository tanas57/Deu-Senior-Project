package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.maps.model.LatLng;

import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import net.muslu.seniorproject.Routing.Markers;

public class DeliveryActivity extends AppCompatActivity {
    static boolean stat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);


        getSupportActionBar().setTitle(getString(R.string.cargo_delivery));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Functions.getRoutes() == null)return;
        if(Functions.getRoutes().size() < 1) return;

        Intent intent = getIntent();
        if(intent == null) return;
        if(intent.getExtras() == null) return;

        final int model_id = intent.getExtras().getInt("model_id");
        final BarcodeReadModel model = Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id);

        final LatLng pos = new LatLng(Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getLatitude(),
                Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getLongitutde() );
        if(model != null){

            final SignaturePad mSignaturePad = findViewById(R.id.delivery_signature);
            final String deliverySelectionArr[] = getResources().getStringArray(R.array.delivery_selection);
            TextView fullname, address;
            Button clear, save;
            final Spinner deliverySpinner;
            final ArrayAdapter<String> deliverySpinnerAdpter;
            ImageView call;

            fullname = findViewById(R.id.delivery_name);
            address  = findViewById(R.id.delivery_address);
            call     = findViewById(R.id.delivery_makecall);
            deliverySpinner = (Spinner) findViewById(R.id.delivery_options_spinner);
            deliverySpinnerAdpter = new ArrayAdapter<String>(this, R.layout.spinner_item,deliverySelectionArr );
            deliverySpinnerAdpter.setDropDownViewResource(R.layout.spinner_item);
            deliverySpinner.setAdapter(deliverySpinnerAdpter);

            clear = findViewById(R.id.delivery_clear_signature);
            save  = findViewById(R.id.delivery_save);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignaturePad.clear();
                }
            });

            fullname.setText(model.getCustomer().getFullName());
            address.setText(model.getCustomer().getAddress());

            deliverySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    //Hangi il seçilmişse onun ilçeleri adapter'e ekleniyor.
                    if(parent.getSelectedItem().toString().equals(deliverySelectionArr[0]))
                    {
                        Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getCargoPackage().setStatus(true);
                        stat = Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getCargoPackage().isStatus();
                    }
                    else{
                        Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getCargoPackage().setStatus(false);
                        stat = Functions.getRoutes().get(Functions.getSelectedRoute()).getBarcodeReadModels().get(model_id).getCargoPackage().isStatus();
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DeliveryActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return;
                    }
                    Uri uri = Uri.parse("tel:" + model.getCustomer().getPhone());
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intent);
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.packet_saved), Toast.LENGTH_LONG).show();
                    int index = Markers.markerList.indexOf(pos);
                    Functions.changeMarkerIcon(index,Markers.markerList,stat);
                    onBackPressed();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}
