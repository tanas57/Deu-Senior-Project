package net.muslu.seniorproject.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.muslu.seniorproject.Functions;
import net.muslu.seniorproject.Models.Cargoman;
import net.muslu.seniorproject.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private String getUser, getPassword;
    private Gson gson;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        gson = new GsonBuilder().create();
        username = findViewById(R.id.reg_name_et);
        password = findViewById(R.id.reg_password_et);

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("İnternet bağlantınızı kontrol ediniz..")
                .setCancelable(false)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = builder.create();

                if (Functions.isConnected(getApplicationContext())) {
                    getUser = username.getText().toString();
                    getPassword = password.getText().toString();
                    new Background().execute();
                } else {
                    alert.show();
                }
            }
        });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected class Background extends AsyncTask<String, String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            if(getUser.length() > 2 && getPassword.length() > 2){
                String apiUrl = Functions.API_URL + "/cargoman/" + getUser + "/" + getPassword;
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
            else return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.w("LOGIN JSON RESULT", s);
            if(!s.contains("error")){

                Cargoman cargoman = gson.fromJson(s, Cargoman.class);
                if(cargoman == null){
                    Toast.makeText(getApplicationContext(), "Kullanıcı adı veya şifre yanlış", Toast.LENGTH_LONG).show();
                }else{
                    Functions.setCargoman(cargoman);
                    Intent myIntent = new Intent(LoginActivity.this,MainPage.class);
                    startActivity(myIntent);
                }
            }
            else
                Toast.makeText(getApplicationContext(), "Kullanıcı adı veya şifre yanlış", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
