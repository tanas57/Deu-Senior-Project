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
import android.widget.RadioGroup;
import android.widget.Toast;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Routing.MapsActivity;
import net.muslu.seniorproject.Routing.RoutingListAdapter;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    private RoutingListAdapter routingListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().setTitle(getString(R.string.cargo_packages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Chromosome> chromosomes = Functions.getRoutes();

        Log.v("RESPONSE CHROMOSOME", "FETCH CHROMOSOME");
        if (chromosomes != null && chromosomes.size() > 0) {
            final RecyclerView rout = findViewById(R.id.routes);
            setContentView(R.layout.activity_route);
            Log.v("RESPONSE CHROMOSOME", "NUMBER OF CHROMOSOME => " + chromosomes.size());
            routingListAdapter = new RoutingListAdapter(getApplicationContext(), Functions.getRoutes(), new RoutingListAdapter.ClickListener() {
                @Override
                public void onPositionClicked(View view, int pos) {
                    Functions.setSelectedRoute(pos);
                    rout.setAdapter(routingListAdapter);
                }
            });



            Button button = findViewById(R.id.select_route);

            rout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rout.setAdapter(routingListAdapter);

            // button select route
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    BarcodeData barcodeData = new BarcodeData();
                    barcodeData.setData(chromosomes.get(Functions.getSelectedRoute()).getBarcodeReadModels());
                    intent.putExtra("data", barcodeData);
                    startActivity(intent);
                }
            });
        }
        else{
            setContentView(R.layout.warning_layout);
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