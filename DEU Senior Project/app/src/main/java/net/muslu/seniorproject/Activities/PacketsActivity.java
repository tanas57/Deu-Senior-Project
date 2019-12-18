package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import net.muslu.seniorproject.CustomAdapter;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.ProjectData;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeRead;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

public class PacketsActivity extends AppCompatActivity {

    private BarcodeData data;
    private RecyclerView rv;
    private CustomAdapter ad;
    private DataTransfer dataTransfer;
    private int packageid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packets);

        getSupportActionBar().setTitle(getString(R.string.cargo_packages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            dataTransfer = (DataTransfer) intent.getExtras().getSerializable("data");
            setData(dataTransfer.getBarcodeData());
            packageid = dataTransfer.getPackageid();

            rv = findViewById(R.id.rv);
            ad = new CustomAdapter(PacketsActivity.this, getData().GetData(), new CustomAdapter.ClickListener() {

                @Override
                public void onPositionClicked(final View view,final BarcodeReadModel model, final int pos) {
                    if (view.getId() == R.id.makeCall) {
                        Toast.makeText(view.getContext(), "Müşteri aranıyor..", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse("tel:" + model.getCustomer().getPhone());

                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(PacketsActivity.this,
                                    new String []{Manifest.permission.CALL_PHONE},1);
                        }
                        startActivity(intent);
                    }
                    else if (view.getId() == R.id.delete) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PacketsActivity.this);
                        builder.setTitle(getString(R.string.packet_delete_title));
                        builder.setMessage(model.getCustomer().getFullName() + " " + getString(R.string.packet_delete_desc));
                        builder.setNegativeButton(getString(R.string.no), null);
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(view.getContext(), model.getCustomer().getFullName() + " müşterisinin " + model.getBarcode() + " numaralı paketi kaldırıldı." , Toast.LENGTH_SHORT).show();
                                data.GetData().remove(pos);
                                packageid--;
                                rv.setAdapter(ad);
                                Log.v("PACKAGE DELETED", model.getCustomer().getFullName() + " PACKAGE DELETED");
                            }
                        });
                        builder.show();



                    } else {
                        Toast.makeText(view.getContext(), "ROW PRESSED = " , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rv.setAdapter(ad);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setHasFixedSize(false);
        }
        /*ImageView makeCallImg = (ImageView) findViewById(R.id.makeCall);
        makeCallImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(v);
            }
        });
        */
    }

  /*  public void makePhoneCall(View view){
        Intent callIntent =new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:05343879697"));
        if (ActivityCompat.checkSelfPermission(PacketsActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }*/

    @Override
    public void onBackPressed() {

        dataTransfer.setBarcodeData(data);
        if(packageid < 1) packageid = 1;

        dataTransfer.setPackageid(packageid);

        Intent intent = new Intent();
        intent.putExtra("data", (DataTransfer) dataTransfer);
        setResult(RESULT_OK, intent);
        finish();
        Log.v("DATA TRANSFER", "DATA RETURNS FROM PACKETS ACTIVITY");
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    public BarcodeData getData() {
        return data;
    }

    public void setData(BarcodeData data) {
        this.data = data;
    }
}
