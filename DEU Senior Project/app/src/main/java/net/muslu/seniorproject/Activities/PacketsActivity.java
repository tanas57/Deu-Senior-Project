package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import net.muslu.seniorproject.CustomAdapter;
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

        getSupportActionBar().setTitle("Geri Bildirim");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            setData((BarcodeData) intent.getExtras().getSerializable("data"));
            rv = findViewById(R.id.rv);
            ad = new CustomAdapter(PacketsActivity.this, getData().GetData());
            rv.setAdapter(ad);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setHasFixedSize(false);
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

    public BarcodeData getData() {
        return data;
    }

    public void setData(BarcodeData data) {
        this.data = data;
    }
}
