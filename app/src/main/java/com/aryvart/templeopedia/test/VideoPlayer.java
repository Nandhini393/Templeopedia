package com.aryvart.templeopedia.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;

import com.aryvart.templeopedia.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoPlayer extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
        OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaPlayerControl {
    String url = "your video url";
    MediaPlayer mMediaPlayer;
    SurfaceView mSurfaceView;
    SurfaceHolder holder;

    ProgressBar progressBar1;
    MediaController mcontroller;
    Handler handler;

    String strURL, fname;
    ProgressDialog dialog;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        strURL = getIntent().getStringExtra("URL");
        fname = getIntent().getStringExtra("fname");


        dialog = new ProgressDialog(VideoPlayer.this);

        ctx = this;


        GetVideo getVideo = new GetVideo(ctx, strURL, fname);
        getVideo.execute();


    }

    private void playVideo(String strFilePath) {
        try {

            Log.i("MYRND", "PlayVideo3......." + strFilePath);

            SurfaceView v = (SurfaceView) findViewById(R.id.videoSurface);
            handler = new Handler();
            holder = v.getHolder();
            holder.addCallback(this);


            mcontroller = new MediaController(this);
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(strFilePath));
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            Log.i("MYRND", "PlayVideo3......." + strFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mcontroller.show();
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mMediaPlayer.setDisplay(holder);
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        progressBar1.setVisibility(View.GONE);
        mcontroller.setMediaPlayer(this);
        mcontroller.setAnchorView(findViewById(R.id.videoSurfaceContainer));
        mcontroller.setEnabled(true);

        handler.post(new Runnable() {
            public void run() {
                mcontroller.show();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO Auto-generated method stub
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mMediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    class GetVideo extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        Context cont;
        String str_typ;
        String strFname;

        public GetVideo(Context context, String str_temp_id, String strFileName) {

            this.cont = context;
            this.temp_id = str_temp_id;
            this.strFname = strFileName;

        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading.....");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] param) {
           /* try {

                URL url = new URL(temp_id);
                long startTime = System.currentTimeMillis();


                //Open a connection to that URL.
                URLConnection ucon = url.openConnection();

                //this timeout affects how long it takes for the app to realize there's a connection problem
                //ucon.setReadTimeout(TIMEOUT_CONNECTION);
                //ucon.setConnectTimeout(TIMEOUT_SOCKET);


                //Define InputStreams to read from the URLConnection.                // uses 3KB download buffer
                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                FileOutputStream outStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + strFname);

                byte[] buff = new byte[50 * 1024];

                //Read bytes (and store them) until there is nothing more to read(-1)
                int len;
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                }

                //clean up
                outStream.flush();
                outStream.close();
                inStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }*/


            try {
                final int TIMEOUT_CONNECTION = 5000;//5sec
                final int TIMEOUT_SOCKET = 30000;//30sec


                URL url = new URL(temp_id);

                Log.i("MYRND", "PlayVideo1.......url : " + url);

                long startTime = System.currentTimeMillis();


                //Open a connection to that URL.
                URLConnection ucon = url.openConnection();

                //this timeout affects how long it takes for the app to realize there's a connection problem
                ucon.setReadTimeout(TIMEOUT_CONNECTION);
                ucon.setConnectTimeout(TIMEOUT_SOCKET);


                //Define InputStreams to read from the URLConnection.
                // uses 3KB download buffer
                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                String strPath = Environment.getExternalStorageDirectory() + "/" + strFname;
                FileOutputStream outStream = new FileOutputStream(strPath);
                byte[] buff = new byte[50 * 1024];

                //Read bytes (and store them) until there is nothing more to read(-1)
                int len;
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                }

                //clean up
                outStream.flush();
                outStream.close();
                inStream.close();
                sResponse = strPath;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return sResponse;
        }

        @Override
        protected void onPostExecute(Object response) {
            dialog.dismiss();
            try {
                Log.i("MYRND", "PlayVideo1.......");
                ;
                Log.i("MYRND", "PlayVideo2......." + sResponse);

                playVideo(sResponse);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}