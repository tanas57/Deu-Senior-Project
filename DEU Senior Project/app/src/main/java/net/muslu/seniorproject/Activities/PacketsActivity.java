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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import net.muslu.seniorproject.CustomAdapter;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

public class PacketsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private CustomAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.cargo_packages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Functions.getPackageSize() > 0) {
            setContentView(R.layout.activity_packets);
            rv = findViewById(R.id.rv);
            ad = new CustomAdapter(PacketsActivity.this, Functions.getPackets().GetData(), new CustomAdapter.ClickListener() {

                @Override
                public void onPositionClicked(final View view, final BarcodeReadModel model, final int pos) {
                    if (view.getId() == R.id.makeCall) {
                        Toast.makeText(view.getContext(), "Müşteri aranıyor..", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse("tel:" + model.getCustomer().getPhone());

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PacketsActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);
                            return;
                        }

                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        startActivity(intent);
                    } else if (view.getId() == R.id.delete) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PacketsActivity.this);
                        builder.setTitle(getString(R.string.packet_delete_title));
                        builder.setMessage(model.getCustomer().getFullName() + " " + getString(R.string.packet_delete_desc));
                        builder.setNegativeButton(getString(R.string.no), null);
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(view.getContext(), model.getCustomer().getFullName() + " müşterisinin " + model.getBarcode() + " numaralı paketi kaldırıldı.", Toast.LENGTH_SHORT).show();
                                Functions.remPacket(pos);
                                Functions.setPackageid(Functions.getPackageid() - 1);
                                ad.notifyDataSetChanged();
                                Log.v("PACKAGE DELETED", model.getCustomer().getFullName() + " PACKAGE DELETED");
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(view.getContext(), "ROW PRESSED = ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rv.setAdapter(ad);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setHasFixedSize(false);
        }
        else{
            setContentView(R.layout.warning_layout);
            TextView emptyText = findViewById(R.id.empty_text);
            emptyText.setText(getResources().getString(R.string.empty_packet_text));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);

        MenuItem search = menu.findItem(R.id.menu_search_item);
        SearchView searchView = (SearchView)search.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(Functions.getPackageSize() > 0)
                    ad.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() != R.id.menu_search_item)
            onBackPressed();
        return true;
    }
}
