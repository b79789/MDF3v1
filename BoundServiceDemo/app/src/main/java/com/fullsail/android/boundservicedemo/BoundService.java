package com.fullsail.android.boundservicedemo;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;

public class BoundService extends Service implements MediaPlayer.OnPreparedListener{

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";
    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;
    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public class BoundServiceBinder extends Binder {
		public BoundService getService() {
			return BoundService.this;
		}
	}
	
	BoundServiceBinder mBinder;
	
	@Override
	public void onCreate() {
		super.onCreate();
        mBinder = new BoundServiceBinder();
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        /*String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;

        mPlayer = MediaPlayer.create(this, Uri.parse(uri2));
        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;*/
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        /*String uri1 = "android.resource://" + getPackageName() + "/raw/turnthepage";
        String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtimerock;
        String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;

        if(mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);

            try {
                mPlayer.setDataSource(this, Uri.parse(uri2));
            } catch(IOException e) {
                e.printStackTrace();

                mPlayer.release();
                mPlayer = null;
            }
        }*/
		return super.onStartCommand(intent, flags, startId);


	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "Service Unbound", Toast.LENGTH_SHORT).show();
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "Service Bound", Toast.LENGTH_SHORT).show();
		return mBinder;
	}
	
	public void showText(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}