package com.aryvart.templeopedia.genericclasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryvart.templeopedia.R;
import com.google.android.gms.maps.GoogleMap;

public class GeneralData {
    public static GoogleMap googleMapGeneral;
    public static String strAddress;
    public static String strLatitutde;
    public static String strLongitude;


    public static GoogleMap prov_googleMapGeneral;
    public static String pro_searchAddress;
    public static String pro_searchLatitutde;
    public static String pro_searchLongitude;

    //public static String SERVER_IP="http://192.168.1.116/templeopedia/";
    public static String SERVER_IP = "http://www.aryvartdev.com/templeopedia/";


    public static int ncount = 0;
    public static String str_lang = "en";

    public static Context context;
    public static String str_LastReqURL;

    public GeneralData(Context context) {
        this.context = context;

    }

    public GeneralData() {

    }


    public boolean isValidEmail(CharSequence target) {
        boolean isvalid = false;
        try {
            if (target == null) {
                return isvalid;
            } else {
                isvalid = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }

    public boolean isConnectingToInternet() {

        boolean isConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    isConnected = true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network", "NETWORKNAME: " + anInfo.getTypeName());
                            isConnected = true;
                        }
                    }
                }
            }
        }

        Log.i("HHJ", "IsConected : " + isConnected);

        return isConnected;
    }

    public static void showAlertDialog(final Context context, String strMessage) {

        try {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setMessage(strMessage);
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);

                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ( (Activity)context).finish();
                }
            });

            final android.support.v7.app.AlertDialog altDialog = builder.create();
            altDialog.show();





           /* LayoutInflater inflater = LayoutInflater.from(context);
            View dialogLayout = inflater.inflate(R.layout.user_current_loc_addr_alert, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogLayout);
            alertDialogBuilder.setCancelable(false);


            final AlertDialog alertDialog = alertDialogBuilder.create();
            //  alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();

            LinearLayout llayAlert = (LinearLayout) dialogLayout.findViewById(R.id.ll_popup);
            // TextView tv_alert_Title = (TextView) dialogLayout.findViewById(R.id.tv_alert_Title);
            TextView tv_alert_Message = (TextView) dialogLayout.findViewById(R.id.text_address);
            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
            //tv_alert_Title.setText(strTitle);
            tv_alert_Message.setText(strMessage);
*/
           /* FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, nScreenHeight);
            llayAlert.setLayoutParams(lparams);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showNoRouteAlertDialog(final Context context, String strMessage) {

        try {

            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setMessage(strMessage);
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    builder.setCancelable(true);
                }
            });

            final android.support.v7.app.AlertDialog altDialog = builder.create();
            altDialog.show();





           /* LayoutInflater inflater = LayoutInflater.from(context);
            View dialogLayout = inflater.inflate(R.layout.user_current_loc_addr_alert, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogLayout);
            alertDialogBuilder.setCancelable(false);


            final AlertDialog alertDialog = alertDialogBuilder.create();
            //  alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();

            LinearLayout llayAlert = (LinearLayout) dialogLayout.findViewById(R.id.ll_popup);
            // TextView tv_alert_Title = (TextView) dialogLayout.findViewById(R.id.tv_alert_Title);
            TextView tv_alert_Message = (TextView) dialogLayout.findViewById(R.id.text_address);
            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
            //tv_alert_Title.setText(strTitle);
            tv_alert_Message.setText(strMessage);
*/
           /* FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, nScreenHeight);
            llayAlert.setLayoutParams(lparams);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

