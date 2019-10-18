package net.muslu.seniorproject.Reader.Barcode;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import net.muslu.seniorproject.Api.Address;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.AddressByBarcode;

public class BarcodeRead extends BarcodeReaderActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_read);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);

        mScannerView.setSoundEffectsEnabled(true);
        contentFrame.addView(mScannerView);

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

        EditText editText = findViewById(R.id.barcode);
        editText.setText(rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeRead.this);
            }
        }, 1000);
    }

    protected String doInBackground(String ... params){
        AddressHelper addressByBarcode = new AddressByBarcode(
                AddressHelper.ApiProcess.GET_CUSTOMER_BY_BARCODE, 1231231212
        );

        String apiUrl = addressByBarcode.GetAddress();

        return "";
    }


}
