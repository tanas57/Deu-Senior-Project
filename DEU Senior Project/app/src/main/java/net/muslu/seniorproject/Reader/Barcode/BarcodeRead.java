package net.muslu.seniorproject.Reader.Barcode;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
    private EditText editText;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_read);

        result = findViewById(R.id.barcode);
        result.setText("denemeeee");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);

        contentFrame.addView(mScannerView);

        editText = findViewById(R.id.barcode2);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Background().execute(editText.getText().toString());
            }
        });

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
        //Toast.makeText(this, "Contents = " + rawResult.getText() +
        //  ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,  5555);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT,111);

        new Background().execute(rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeRead.this);
            }
        }, 1000);
    }

    class Background extends AsyncTask<String, String ,String> {
        protected String doInBackground(String... params) {
            AddressHelper addressByBarcode = new AddressByBarcode(
                    AddressHelper.ApiProcess.GET_CUSTOMER_BY_BARCODE, Long.parseLong(params[0])
            );

            String apiUrl = addressByBarcode.GetAddress();

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
            result.setText(s);
            Log.d("error", s);
        }
    }
}
