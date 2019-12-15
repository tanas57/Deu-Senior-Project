package net.muslu.seniorproject.Reader.Barcode;

import android.Manifest;
import android.content.DialogInterface;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.Result;

import net.muslu.seniorproject.Algorithm.AlgorithmType;
import net.muslu.seniorproject.Algorithm.GeneticAlgorithm;
import net.muslu.seniorproject.Api.AddressHelper;
import net.muslu.seniorproject.Api.JSON.GeneticAlgorithmData;
import net.muslu.seniorproject.Api.PackageByBarcode;
import net.muslu.seniorproject.R;
import net.muslu.seniorproject.TempData;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {
    AlgorithmType returnedType;
    String []selectRoute ;
    private ZXingScannerView zXingScannerView;
    String[] perms = {Manifest.permission.CAMERA};
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_camera);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.camera_content_frame);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

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

                            break;
                        case R.id.nav_camera:

                            break;
                        case R.id.nav_package_list:

                            break;
                        case R.id.nav_route:
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
                                  
                                    //zXingScannerView.startCamera();

                                }
                            })  ;
                            AlertDialog myDiaolog = mBuilder.create();
                            myDiaolog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            myDiaolog.show();

                            zXingScannerView.setLaserEnabled(false);
                            zXingScannerView.stopCamera();

                            myDiaolog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    zXingScannerView.startCamera();
                                }
                            });

                            break;
                    }

                    return true;
                }
            };


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

        // new BarcodeRead.Background().execute(apiUrl, rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zXingScannerView.resumeCameraPreview(CameraActivity.this);
            }
        }, 333);
    }
}
