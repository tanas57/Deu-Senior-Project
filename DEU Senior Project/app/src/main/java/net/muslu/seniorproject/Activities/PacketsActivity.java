package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import net.muslu.seniorproject.CustomAdapter;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.ProjectData;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeRead;

public class PacketsActivity extends AppCompatActivity {

    private BarcodeData data;
    private RecyclerView rv;
    private CustomAdapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packets);

        getSupportActionBar().setTitle(getString(R.string.cargo_packages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            DataTransfer dataTransfer = (DataTransfer) intent.getExtras().getSerializable("data");
            setData(dataTransfer.getBarcodeData());
            rv = findViewById(R.id.rv);
            ad = new CustomAdapter(PacketsActivity.this, getData().GetData());
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
        super.onBackPressed();

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
