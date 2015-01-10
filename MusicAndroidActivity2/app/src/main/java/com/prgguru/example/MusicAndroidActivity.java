package com.prgguru.example;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MusicAndroidActivity extends Activity {

	static MediaPlayer mPlayer;
	Button buttonPlay;
	Button buttonStop;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		buttonPlay = (Button) findViewById(R.id.play);
		buttonPlay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
                String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
				// Even you can refer resource in res/raw directory
				//Uri myUri = Uri.parse("android.resource://com.prgguru.example/" + R.raw.hosannatamil); 
				Uri myUri1 = Uri.parse(uri1);
				mPlayer = new MediaPlayer();
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try {
					mPlayer.setDataSource(getApplicationContext(), myUri1);
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (SecurityException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IllegalStateException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mPlayer.prepare();
				} catch (IllegalStateException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				}
				mPlayer.start();
			}
		});
		
		buttonStop = (Button) findViewById(R.id.stop);
		buttonStop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mPlayer!=null && mPlayer.isPlaying()){
					mPlayer.stop();
				}
			}
		});
	}
	
	protected void onDestroy() {
		super.onDestroy();
		// TODO Auto-generated method stub
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

}
