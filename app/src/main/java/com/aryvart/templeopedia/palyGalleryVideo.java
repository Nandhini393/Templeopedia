package com.aryvart.templeopedia;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.aryvart.templeopedia.genericclasses.GeneralData;

/**
 * Created by android1 on 11/8/16.
 */
public class palyGalleryVideo extends Activity {

    private static final int PICK_VIDEO_REQUEST = 1001;
    String strPath;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mFirstSurface;
    private SurfaceHolder mActiveSurface;
    private Uri mVideoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);
        SurfaceView sfview = (SurfaceView) findViewById(R.id.sfView);
        strPath = getIntent().getStringExtra("vURL");
        GeneralData.context = this;
        doStartStop(sfview);

        sfview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d("SFV", " surface created!");
                mFirstSurface = surfaceHolder;
                if (mVideoUri != null) {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(),
                            mVideoUri, mFirstSurface);
                    mActiveSurface = mFirstSurface;
                    mMediaPlayer.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d("SFV", "surface destroyed!");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoUri = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void doStartStop(View view) {
        if (mMediaPlayer == null) {
            mVideoUri = Uri.parse(strPath);
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void doSwitchSurface(View view) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.setDisplay(mFirstSurface);
        }
    }
}
