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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

public class DeliveryActivity extends AppCompatActivity {

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

        int model_id = intent.getExtras().getInt("model_id");

        final BarcodeReadModel model = Functions.getRoutes().get(0).getBarcodeReadModels().get(model_id);

        if(model != null){
            final SignaturePad mSignaturePad = (SignaturePad) findViewById(R.id.delivery_signature);

            CardView customer = findViewById(R.id.delivery_customer);
            TextView fullname, address;
            Button clear, save;
            ImageView call;

            fullname = findViewById(R.id.delivery_name);
            address  = findViewById(R.id.delivery_address);
            call     = findViewById(R.id.delivery_makecall);

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
