package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Routing.MapsActivity;
import net.muslu.seniorproject.Routing.RoutingListAdapter;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getSupportActionBar().setTitle(getString(R.string.cargo_packages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Chromosome> chromosomes = Functions.getRoutes();

        Log.v("RESPONSE CHROMOSOME", "FETCH CHROMOSOME");
        if (chromosomes != null && chromosomes.size() > 0) {
            Log.v("RESPONSE CHROMOSOME", "NUMBER OF CHROMOSOME => " + chromosomes.size());
            RoutingListAdapter routingListAdapter = new RoutingListAdapter(getApplicationContext(), Functions.getRoutes(), new RoutingListAdapter.ClickListener() {
                @Override
                public void onPositionClicked(View view, int pos) {
                    selected = pos;
                }
            });


            RecyclerView rout = findViewById(R.id.routes);
            Button button = findViewById(R.id.select_route);

            rout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rout.setAdapter(routingListAdapter);

            // button select route
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    BarcodeData barcodeData = new BarcodeData();
                    barcodeData.setData(chromosomes.get(selected).getBarcodeReadModels());
                    intent.putExtra("data", barcodeData);
                    startActivity(intent);
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