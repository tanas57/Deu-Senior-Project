package net.muslu.seniorproject.Reader.Barcode;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import net.muslu.seniorproject.Api.JSON.JsonProcess;
import net.muslu.seniorproject.CustomAdapter;
import net.muslu.seniorproject.Map.Coordinate;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.AddressByBarcode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BarcodeRead extends BarcodeReaderActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private Coordinate coordinate;
    private BarcodeData data;
    RecyclerView rv;

    private CustomAdapter ad;

    public BarcodeRead() {

        this.coordinate = new Coordinate();
        this.data = new BarcodeData();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_read);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);

        /*List<BarcodeFormat> format = new ArrayList<BarcodeFormat>();
        format.add(BarcodeFormat.CODE_128);

        mScannerView.setFormats(format);*/

        contentFrame.addView(mScannerView);
        /*
        this.data.AddData(R.drawable.barcode, "Tayip Muslu", "205/7 Sokak No 16/1 D 8 Buca İzmir", "05462003052");
        this.data.AddData(R.drawable.barcode, "Tayip Muslu", "205/7 Sokak No 16/1 D 8 Buca İzmir", "05462003052");
        this.data.AddData(R.drawable.barcode, "Tayip Muslu", "205/7 Sokak No 16/1 D 8 Buca İzmir", "05462003052");
        */

        rv = findViewById(R.id.rv);
        ad = new CustomAdapter(BarcodeRead.this, data.GetData());
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,  5555);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT,111);

        AddressHelper addressByBarcode = new AddressByBarcode(
                AddressHelper.ApiProcess.GET_CUSTOMER_BY_BARCODE, Long.parseLong(rawResult.getText())
        );

        String apiUrl = addressByBarcode.GetAddress();

        new Background().execute(apiUrl);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeRead.this);
            }
        }, 333);
    }

    protected class Background extends AsyncTask<String, String ,String> {
        protected String doInBackground(String... params) {

            String apiUrl = params[0];

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
                if(data.AddData(JsonProcess.GetPackageInfo(s))){
                    ad.notifyItemInserted(data.GetSize());
                    //Toast.makeText(getApplicationContext(), data.GetSize(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Bu paket daha önce eklendi", Toast.LENGTH_LONG).show();
                }
            }
            else
             Toast.makeText(getApplicationContext(), "Barkod tam anlaşılamadı", Toast.LENGTH_LONG).show();
        }

    }
}
