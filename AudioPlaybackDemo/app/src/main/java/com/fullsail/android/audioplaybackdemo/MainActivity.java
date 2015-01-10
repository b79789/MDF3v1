package com.fullsail.android.audioplaybackdemo;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity implements OnPreparedListener {
	
	private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";
	
	MediaPlayer mPlayer;
	boolean mActivityResumed;
	boolean mPrepared;
	int mAudioPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPrepared = mActivityResumed = false;
		mAudioPosition = 0;
		
		if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_POSITION)) {
			mAudioPosition = savedInstanceState.getInt(SAVE_POSITION, 0);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(mPlayer == null) {
			// Easy way: mPlayer = MediaPlayer.create(this, R.raw.something_elated);
			// Easy way doesn't require a call to prepare or prepareAsync. Only works for resources.
			
			mPlayer = new MediaPlayer();
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setOnPreparedListener(this);
			
			try {
				mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/something_elated"));
			} catch(IOException e) {
				e.printStackTrace();
				
				mPlayer.release();
				mPlayer = null;
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mPlayer != null) {
			outState.putInt(SAVE_POSITION, mPlayer.getCurrentPosition());
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mActivityResumed = true;
		if(mPlayer != null && !mPrepared) {
			mPlayer.prepareAsync();
		} else if(mPlayer != null && mPrepared) {
			mPlayer.seekTo(mAudioPosition);
			mPlayer.start();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mActivityResumed = false;
		
		if(mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
			mPrepared = false;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mPlayer != null) {
			mPlayer.release();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mPrepared = true;
		
		if(mPlayer != null && mActivityResumed) {
			mPlayer.seekTo(mAudioPosition);
			mPlayer.start();
		}
	}
}
