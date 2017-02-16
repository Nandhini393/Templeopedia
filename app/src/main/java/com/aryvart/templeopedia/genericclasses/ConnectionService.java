package com.aryvart.templeopedia.genericclasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Oscar on 20/03/15.
 */
public class ConnectionService extends Service {

    // Constant
    public static String TAG_INTERVAL = "interval";
    public static String TAG_URL_PING = "url_ping";
    public static String TAG_ACTIVITY_NAME = "activity_name";

    private int interval;
    //private String url_ping;
    private String activity_name;
    private final String USER_AGENT = "Mozilla/5.0";


    private Timer mTimer = null;

    ConnectionServiceCallback mConnectionServiceCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface ConnectionServiceCallback {
        void hasInternetConnection();

        void hasNoInternetConnection();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        interval = intent.getIntExtra(TAG_INTERVAL, 10);
        // url_ping = intent.getStringExtra(TAG_URL_PING);
        activity_name = intent.getStringExtra(TAG_ACTIVITY_NAME);

        try {
            mConnectionServiceCallback = (ConnectionServiceCallback) Class.forName(activity_name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new CheckForConnection(), 0, interval * 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    class CheckForConnection extends TimerTask {
        @Override
        public void run() {
            isNetworkAvailable();
        }
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        boolean isConnect = false;
        try {
            isConnect = false;
            URL obj = new URL("http://www.aryvartdev.com");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                mConnectionServiceCallback.hasInternetConnection();
                isConnect = true;
            } else {
                mConnectionServiceCallback.hasNoInternetConnection();
                isConnect = false;
            }
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
        }

        return isConnect;
    }

}

