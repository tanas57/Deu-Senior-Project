package net.muslu.seniorproject.Reader.Barcode;

import android.Manifest;
import android.content.DialogInterface;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.Result;

import net.muslu.seniorproject.Activities.PacketsActivity;
import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Algorithm.GeneticAlgorithm;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.JSON.GeneticAlgorithmData;
import net.muslu.seniorproject.Api.JSON.JsonProcess;
import net.muslu.seniorproject.Api.PackageByBarcode;
import net.muslu.seniorproject.DataTransfer;
import net.muslu.seniorproject.MainActivity;
import net.muslu.seniorproject.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.wasabeef.blurry.Blurry;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    AlgorithmType returnedType;
    String [] selectRoute ;
    private ZXingScannerView zXingScannerView;
    String[] perms = {Manifest.permission.CAMERA};

    ViewGroup contentFrame;

    DataTransfer dataTransfer;
    BarcodeData barcodeData;
    int packageid;

    private LatLng cargoman;

    String [] barcodes = new String[10];

    public void onCreate(Bundle state) {
        super.onCreate(state);

        if(getIntent() != null){

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                dataTransfer = (DataTransfer)getIntent().getSerializableExtra("data");
                barcodeData = dataTransfer.getBarcodeData();
                packageid = dataTransfer.getPackageid();
                cargoman = new LatLng(dataTransfer.getCargomanLatitude(), dataTransfer.getCargomanLongitude());
            }
            else return;
        }

        for(int i = 10; i < barcodes.length + 10; i++ ){
            String temp2 = "123456789" + i;
            barcodes[i-10] = temp2;
        }
        /*
        for(int i = 0; i<barcodes.length;i++){
            AddressHelper addressByBarcode = new PackageByBarcode(Long.parseLong(barcodes[i]));
            String apiUrl = addressByBarcode.GetAddress();

            new Background().execute(apiUrl, barcodes[i]);
        }

         */

        setContentView(R.layout.activity_camera);
        contentFrame = (ViewGroup) findViewById(R.id.fragment_container);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getOrCreateBadge(R.id.nav_package_list).setNumber(2);
        //BadgeDrawable badge = bottomNavigationView.showBadge(menuItemId);

        zXingScannerView = new ZXingScannerView(this);
        contentFrame.addView(zXingScannerView);
        zXingScannerView.setBorderColor(getResources().getColor(R.color.cameraBorderColor));
        zXingScannerView.setLaserColor(getResources().getColor(R.color.cameraLaserColor));
        zXingScannerView.setBorderCornerRadius(50);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.setFocusableInTouchMode(true);
        zXingScannerView.setClickable(true);
        zXingScannerView.setIsBorderCornerRounded(true);
        zXingScannerView.setBorderAlpha((float)50.0);

        if(!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_homePage:
                            onBackPressed();
                            break;
                        case R.id.nav_package_list:
                            Intent packets = new Intent(getApplicationContext(), PacketsActivity.class);
                            packets.putExtra("data", dataTransfer);
                            startActivity(packets);
                            break;
                        case R.id.nav_route:

                            Blurry.with(getApplicationContext()).sampling(50).radius(10).onto(contentFrame);

                            zXingScannerView.setLaserEnabled(false);
                            selectRoute= getResources().getStringArray(R.array.route_selection);
                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraActivity.this,R.style.MyAlertDialog);

                            mBuilder.setTitle("Rota hesaplama türünü seçiniz: ");

                            mBuilder.setSingleChoiceItems(selectRoute, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,  int which) {
                                    //Log.v("İtem:",selectRoute[which]);
                                    switch (which){
                                        case 0:
                                            returnedType=AlgorithmType.ONLY_DISTANCE;
                                            break;
                                        case 1:
                                            returnedType=AlgorithmType.ONLY_DURATION;
                                            break;
                                        case 2:
                                            returnedType=AlgorithmType.BOTH_DISTANCE_DURATION;
                                            break;
                                        case 3:
                                            returnedType=AlgorithmType.DISTANCE_PRIORITY;
                                            break;
                                        case 4:
                                            returnedType=AlgorithmType.DURATION_PRIORITY;
                                            break;
                                        case 5:
                                            returnedType=AlgorithmType.ALL_OF_THEM;
                                            break;
                                    }


                                }
                            });
                            mBuilder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    zXingScannerView.startCamera();
                                    zXingScannerView.setLaserEnabled(true);
                                    Blurry.delete(contentFrame);
                                }
                            })  ;
                            AlertDialog myDiaolog = mBuilder.create();
                            myDiaolog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
                            myDiaolog.show();

                            zXingScannerView.setLaserEnabled(false);
                            zXingScannerView.stopCamera();

                            myDiaolog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Blurry.delete(contentFrame);
                                    zXingScannerView.startCamera();
                                }
                            });

                            break;
                    }

                    return true;
                }
            };

    @Override
    public void onBackPressed() {

        dataTransfer.setBarcodeData(barcodeData);
        dataTransfer.setPackageid(packageid);

        Intent intent = new Intent();
        intent.putExtra("data", (DataTransfer) dataTransfer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }else {
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
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
                zXingScannerView.resumeCameraPreview(CameraActivity.this);
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
                    if(barcodeData.AddData(newPackage)){
                        newPackage.setPackageId(packageid);
                        packageid++;
                        //ad.notifyItemInserted(data.GetSize());
                        Log.v("BARCODE IMG ADDRESS", newPackage.getBarcodeImgApiURL());
                        //Toast.makeText(getApplicationContext(), data.GetSize(), Toast.LENGTH_LONG).show();

                        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(200);
                        }

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
