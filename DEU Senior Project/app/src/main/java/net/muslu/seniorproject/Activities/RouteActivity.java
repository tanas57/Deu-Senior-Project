package net.muslu.seniorproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Routing.RoutingListAdapter;
import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    private RoutingListAdapter routingListAdapter;
    private RecyclerView rout;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.our_routes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Chromosome> chromosomes = Functions.getRoutes();



        Log.v("RESPONSE CHROMOSOME", "FETCH CHROMOSOME");
        if (chromosomes != null && chromosomes.size() > 0) {
            setContentView(R.layout.activity_route);
            rout = findViewById(R.id.routes_rcy);
            Log.v("RESPONSE CHROMOSOME", "NUMBER OF CHROMOSOME => " + chromosomes.size());
            routingListAdapter = new RoutingListAdapter(getApplicationContext(), Functions.getRoutes(), new RoutingListAdapter.ClickListener() {
                @Override
                public void onPositionClicked(View view, int pos) {
                    Functions.setSelectedRoute(pos);
                    routingListAdapter.notifyDataSetChanged();
                }
            });

            Button button = findViewById(R.id.select_route);

            rout.setLayoutManager(new LinearLayoutManager(this));
            rout.setAdapter(routingListAdapter);

            // button select route
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    BarcodeData barcodeData = new BarcodeData();
                    int get = Functions.getSelectedRoute();
                    if(get < 0){
                        Toast.makeText(getApplicationContext(), "İlerlemek için rota seçiniz..", Toast.LENGTH_LONG).show();
                    }
                    else{
                        barcodeData.setData(chromosomes.get(get).getBarcodeReadModels());
                        intent.putExtra("data", barcodeData);
                        startActivity(intent);
                    }
                }
            });

            swipeContainer = findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    routingListAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

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