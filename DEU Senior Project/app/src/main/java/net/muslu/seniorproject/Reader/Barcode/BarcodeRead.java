package net.muslu.seniorproject.Reader.Barcode;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

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

public class BarcodeRead extends BarcodeReaderActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private AppCompatTextView result;
    protected Coordinate coordinate;
    RecyclerView rv;

    Integer[] drawableArray = {R.drawable.barcode, R.drawable.barcode, R.drawable.barcode,
            R.drawable.barcode, R.drawable.barcode,R.drawable.barcode, R.drawable.barcode, R.drawable.barcode,
            R.drawable.barcode, R.drawable.barcode};
    String[] titleArray = {"Title1","Title2","Title3","Title4","Title5","Title6","Title7","Title8","Title9","Title10"};
    String[] subtitleArray = {"subtitle1","subtitle2","subtitle3","subtitle4","subtitle5","subtitle6","subtitle7","subtitle8","subtitle9","subtitle10"};
    CustomAdapter ad;

    public BarcodeRead() {
        this.coordinate = new Coordinate();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_read);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);

        contentFrame.addView(mScannerView);

        rv = findViewById(R.id.rv);
        ad = new CustomAdapter(BarcodeRead.this,drawableArray,titleArray,subtitleArray);
        rv.setAdapter(ad);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
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
        }, 100);
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
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            Log.d("GET JSON RESULT", s);
        }

    }
}
