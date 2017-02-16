package com.aryvart.templeopedia.genericclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;

/**
 * Created by Admin on 11-03-2016.
 */
public class AsyntaskHandler extends AsyncTask {
    ProgressDialog progressDialog;
    String strResp;
    String strURL = "http://aryvartdev.com/projects/utician/register.php ";
    Context context;
    String strUrlParameters, strBaseURL;

    ImageView imvProduct;

    String strNeedJsonArforCheckout;

    SharedPreferences prefs;
    int nUserId;

    String strUSERNAME, strMOBILENO, strUSERID;


    public AsyntaskHandler(Context context) {
        this.context = context;
    }


    public AsyntaskHandler(Context context, String strURL, String strUrlParameters) {
        this.context = context;
        this.strBaseURL = strURL;
        this.strUrlParameters = strUrlParameters;


    }


    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            //To Call the Generic API calls to call the HTTP POST/GET methods.

            // APIcalls loginCall = new APIcalls("http://aryvartdev.com/projects/utician/register.php", "fullname=" + URLEncoder.encode("kalai", "UTF-8") + "username=" + URLEncoder.encode("kalaivani", "UTF-8") + "&email=" + URLEncoder.encode("27kalaivani@gmail.com", "UTF-8") + "&password=" + URLEncoder.encode("123456", "UTF-8") + "&usertype=" + URLEncoder.encode("client", "UTF-8"));

            InetAddress address = InetAddress.getByName(strBaseURL);

            APIcalls generalApiCall = new APIcalls(address.toString(), strUrlParameters);
            strResp = generalApiCall.Process_for_post();

        } catch (Exception e) {
            Log.i("SR", "doInBack_Exception" + e.getMessage());
            e.printStackTrace();
        }
        return strResp;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.i("SR", "onPostExecute_strResp : " + strResp);

        try {
            try {
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject jsobjResponse = new JSONObject(strResp);

            Log.e("JSVal", jsobjResponse.toString());

            Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show();


         /*   if (jsobjResponse.getString("status").equalsIgnoreCase("true")) {


              Log.e("JSVal",jsobjResponse.toString());

                Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show();


            } else {


            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


