package com.aryvart.templeopedia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.genericclasses.ConnectionService;
import com.aryvart.templeopedia.genericclasses.GeneralData;

public class SplashActivity extends AppCompatActivity {

    Context context;
static SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        GeneralData gD = new GeneralData(context);
        gD.context = this;
preferences= getSharedPreferences("temp_pref",Context.MODE_PRIVATE);

        if (gD.isConnectingToInternet()) {
            Thread timerThread = new Thread() {
                public void run() {
                    try {

                        sleep(2500);
                        Intent intent = new Intent(SplashActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {

                    }
                }
            };
            timerThread.start();
        } else {

            GeneralData.showAlertDialog(context, "No Internet Connection. Do you wish to turn it on?");


        }


    }


}
