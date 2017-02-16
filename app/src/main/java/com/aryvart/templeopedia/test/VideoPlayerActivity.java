package com.aryvart.templeopedia.test;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.genericclasses.GeneralData;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    String fname;
    String strURL;
    String strFilePath;

    ProgressDialog dialog;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_video_player);
            videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
            ctx = this;
            GeneralData.context = this;
            dialog = new ProgressDialog(VideoPlayerActivity.this);

            fname = getIntent().getStringExtra("fname");
            strURL = getIntent().getStringExtra("URL");


            SurfaceHolder videoHolder = videoSurface.getHolder();
            videoHolder.addCallback(this);
            player = new MediaPlayer();
           // strPath = getIntent().getStringExtra("vURL");
            controller = new VideoControllerView(this);
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(this, Uri.parse(strURL));
                player.setOnPreparedListener(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            GetVideo getVideo = new GetVideo(ctx, strURL, fname);
            getVideo.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setDisplay(holder);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {

        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
    }

    // End MediaPlayer.OnPreparedListener
    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
    // End VideoMediaController.MediaPlayerControlMYRND

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        // moveTaskToBack(true);
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
            try {

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

                byte[] buff = new byte[5 * 1024];

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
            }
            return sResponse;
        }

        @Override
        protected void onPostExecute(Object response) {
            dialog.dismiss();
            try {
                strFilePath = Environment.getExternalStorageDirectory() + "/" + strFname;

                Log.i("MYND", "PATH : " + strFilePath);


                if (strFilePath != null) {
                    player.setDataSource(VideoPlayerActivity.this, Uri.parse(strFilePath));

                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    player.setOnPreparedListener(VideoPlayerActivity.this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
