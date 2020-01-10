package net.muslu.seniorproject.Activities;

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
import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Algorithm.GeneticAlgorithm;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.JSON.DMBelowTenPoint;
import net.muslu.seniorproject.Api.JSON.DMGreaterTenPoint;
import net.muslu.seniorproject.Api.JSON.GeneticAlgorithmData;
import net.muslu.seniorproject.Api.JSON.JsonProcess;
import net.muslu.seniorproject.Api.PackageByBarcode;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.Reader.Barcode.BarcodeData;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import net.muslu.seniorproject.TempData;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private AlgorithmType returnedType;
    private String [] selectRoute ;
    private ZXingScannerView zXingScannerView;
    private String[] perms = {Manifest.permission.CAMERA};
    private ViewGroup contentFrame;
    private BottomNavigationView bottomNav;
    private BarcodeData barcodeData;
    private LatLng cargoman;

    private boolean[] isChecked = new boolean[6];
    private ArrayList<AlgorithmType> algorithmTypes = new ArrayList<>();
    private int psize = 10;
    String [] barcodes = new String[psize];

    private void cargomanLocation(){
        if(Functions.takePermission(getApplicationContext(),this, Manifest.permission.ACCESS_FINE_LOCATION))
            Functions.fetchLastLocation(getApplicationContext());

        cargoman = new LatLng(Functions.getCargoman_lat(), Functions.getCargoman_lng());
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_camera);
        barcodeData = Functions.getPackets();

        cargomanLocation(); // update cargoman location

        contentFrame = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(Functions.getPackageid() > 1){
            bottomNav.getOrCreateBadge(R.id.nav_package_list).setNumber(Functions.getPackageid() - 1);
        }

        if(Functions.takePermission(getApplicationContext(),this, Manifest.permission.CAMERA))
            Functions.fetchLastLocation(getApplicationContext());

        zXingScannerView = new ZXingScannerView(this);
        contentFrame.addView(zXingScannerView);
        zXingScannerView.setBorderColor(getResources().getColor(R.color.curated_light));
        zXingScannerView.setLaserColor(getResources().getColor(R.color.cameraLaserColor));
        zXingScannerView.setBorderCornerRadius(50);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.setFocusableInTouchMode(true);
        zXingScannerView.setClickable(true);
        zXingScannerView.setIsBorderCornerRounded(true);
        zXingScannerView.setBorderAlpha((float)50.0);


        if(Functions.getPackageSize() < psize) {
            for (int i = 10; i < barcodes.length + 10; i++) {
                String temp2 = "123456789" + i;
                barcodes[i - 10] = temp2;
            }

            for (int i = 0; i < barcodes.length; i++) {
                AddressHelper addressByBarcode = new PackageByBarcode(Long.parseLong(barcodes[i]));
                String apiUrl = addressByBarcode.GetAddress();

                new Background().execute(apiUrl, barcodes[i]);
            }
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
                            Intent packetsIntent = new Intent(getApplicationContext(), PacketsActivity.class);
                            startActivity(packetsIntent);
                            break;
                        case R.id.nav_route:

                            Blurry.with(getApplicationContext()).sampling(50).radius(10).onto(contentFrame);

                            zXingScannerView.setLaserEnabled(false);
                            selectRoute= getResources().getStringArray(R.array.route_selection);
                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraActivity.this,R.style.MyAlertDialog);

                            mBuilder.setTitle("Rota hesaplama türünü seçiniz: ");
                            mBuilder.setCancelable(false);

                            mBuilder.setMultiChoiceItems(selectRoute, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) {
                                        algorithmTypes.add(getReturnedType(which));
                                    }
                                    else if (algorithmTypes.contains(getReturnedType(which))) {
                                        algorithmTypes.remove(getReturnedType(which));
                                    }
                                }
                            });

                            mBuilder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            mBuilder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    zXingScannerView.startCamera();
                                    zXingScannerView.setLaserEnabled(true);
                                    Blurry.delete(contentFrame);

                                    cargomanLocation(); // update cargoman location

                                    if(Functions.getPackageSize() > 9){
                                        DMGreaterTenPoint dmGreaterTenPoint = new DMGreaterTenPoint(CameraActivity.this, Functions.getPackets(), algorithmTypes);
                                        dmGreaterTenPoint.setCargoman(new BarcodeReadModel(0, cargoman.latitude, cargoman.longitude, getApplicationContext()));
                                        dmGreaterTenPoint.Execute();
                                    }else{

                                        DMBelowTenPoint dmBelowTenPoint = new DMBelowTenPoint(CameraActivity.this, Functions.getPackets(), algorithmTypes);
                                        dmBelowTenPoint.setCargoman(new BarcodeReadModel(0, cargoman.latitude, cargoman.longitude, getApplicationContext()));
                                        dmBelowTenPoint.Execute();
                                    }


                                   /*
                                    int size = Functions.getPackageSize() + 1;

                                    TempData tempData = new TempData(size);
                                    double [][] distances = tempData.getData().get(0);
                                    double [][] durations = tempData.getData().get(1);

                                    for(AlgorithmType type: algorithmTypes){

                                        Log.v("GENETIC STARTS", "WITH CHOICE => " + type);

                                        GeneticAlgorithmData geneticAlgorithmData = new GeneticAlgorithmData();
                                        geneticAlgorithmData.setCargoman(new BarcodeReadModel(0, 38.371881, 27.194662, getApplicationContext()));
                                        geneticAlgorithmData.setDistances(distances);
                                        geneticAlgorithmData.setDurations(durations);
                                        geneticAlgorithmData.setBarcodeData(barcodeData);
                                        geneticAlgorithmData.setAlgorithmType(type);

                                        new GeneticAlgorithm(getApplicationContext(), geneticAlgorithmData);

                                    }
 */
                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                    startActivity(intent);

                                }
                            })  ;
                            AlertDialog myDiaolog = mBuilder.create();
                            myDiaolog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
                            myDiaolog.show();

                            myDiaolog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Blurry.delete(contentFrame);
                                    zXingScannerView.startCamera();
                                }
                            });

                            zXingScannerView.setLaserEnabled(false);
                            zXingScannerView.stopCamera();

                            break;
                    }
                    return true;
                }
            };

    private AlgorithmType getReturnedType(int pos){
        switch (pos){
            case 0:
                return AlgorithmType.ONLY_DISTANCE;
            case 1:
                return AlgorithmType.ONLY_DURATION;
            case 2:
                return AlgorithmType.BOTH_DISTANCE_DURATION;
            case 3:
                return AlgorithmType.DISTANCE_PRIORITY;
            case 4:
                return AlgorithmType.DURATION_PRIORITY;
            case 5:
                return AlgorithmType.ALL_OF_THEM;
            default: return AlgorithmType.ONLY_DISTANCE;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Functions.takePermission(getApplicationContext(),this, Manifest.permission.CAMERA)){
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();
        }
        if(Functions.getPackageid() > 1){
            bottomNav.getOrCreateBadge(R.id.nav_package_list).setNumber(Functions.getPackageid() - 1);
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
                BarcodeReadModel newPackage = JsonProcess.GetPackageInfo(s, barcode, CameraActivity.this);
                if(newPackage != null){
                    if(Functions.addPacket(newPackage)){
                        newPackage.setPackageId(Functions.getPackageid());
                        int id = Functions.getPackageid() + 1;
                        Functions.setPackageid(id);
                        bottomNav.getOrCreateBadge(R.id.nav_package_list).setNumber(id - 1);
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
