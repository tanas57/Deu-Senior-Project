package net.muslu.seniorproject.Reader.Barcode;

import android.Manifest;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;

import net.muslu.seniorproject.Api.JSON.JsonDirectionMatrix;
import net.muslu.seniorproject.Api.JSON.JsonProcess;
import net.muslu.seniorproject.Api.PackageByBarcode;
import net.muslu.seniorproject.CustomAdapter;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.AddressByBarcode;
import net.muslu.seniorproject.Routing.MapsActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BarcodeRead extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private BarcodeData data;
    private RecyclerView rv;
    private CustomAdapter ad;
    private int packageCounter = 0;
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    String [] barcodes = new String[] { "12345678910", "12345678911", "12345678912", "12345678915"};
    public BarcodeRead() {
        this.data = new BarcodeData();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_read);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);


        if(!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }

        rv = findViewById(R.id.rv);
        ad = new CustomAdapter(BarcodeRead.this, data.GetData());
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(false);

        final ImageView button = findViewById(R.id.route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BarcodeRead.this, "map is opening", Toast.LENGTH_LONG).show();

                JsonDirectionMatrix jsonDirectionMatrix = new JsonDirectionMatrix(BarcodeRead.this, data);
                jsonDirectionMatrix.Execute();
                /*
                Intent map = new Intent(getApplicationContext(), net.muslu.seniorproject.Routing.MapsActivity.class);
                map.putExtra("data", data);
                startActivity(map);
                */

            }
        });

        for(int i = 0; i<barcodes.length;i++){
            AddressHelper addressByBarcode = new PackageByBarcode(Long.parseLong(barcodes[i]));
            String apiUrl = addressByBarcode.GetAddress();

            new Background().execute(apiUrl, barcodes[i]);
        }


        mScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }else {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,  5555);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT,155);

        AddressHelper addressByBarcode = new PackageByBarcode(Long.parseLong(rawResult.getText()));

        String apiUrl = addressByBarcode.GetAddress();

        new Background().execute(apiUrl, rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeRead.this);
            }
        }, 333);
    }

    protected class Background extends AsyncTask<String, String ,String> {
        protected long barcode;
        protected String doInBackground(String... params) {

            String apiUrl = params[0];
            barcode = Long.parseLong(params[1]);
            Log.d("url", apiUrl);
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String result = "", temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    result += temp;
                }
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.w("GET JSON RESULT", s);
            if(!s.contains("error")){
                BarcodeReadModel newPackage = JsonProcess.GetPackageInfo(s, barcode);
                if(newPackage != null){
                    if(data.AddData(newPackage)){
                        newPackage.setPackageId(packageCounter);
                        packageCounter++;
                        ad.notifyItemInserted(data.GetSize());
                        Log.v("BARCODE IMG ADDRESS", newPackage.getBarcodeImgApiURL());
                        //Toast.makeText(getApplicationContext(), data.GetSize(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Bu paket daha önce eklendi", Toast.LENGTH_LONG).show();
                    }
                }
            }
            else
             Toast.makeText(getApplicationContext(), "Barkod tam anlaşılamadı", Toast.LENGTH_LONG).show();
        }

    }
}
