package com.fullsail.android.intentservicedemo;

import java.util.Random;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

public class BackgroundService extends IntentService {
	
	Random mRandom;

	public BackgroundService() {
		super("BackgroundService");
		
		mRandom = new Random(System.currentTimeMillis());
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.hasExtra(MainActivity.EXTRA_RECEIVER)) {
			ResultReceiver receiver = (ResultReceiver)intent.getParcelableExtra(MainActivity.EXTRA_RECEIVER);
			Bundle result = new Bundle();
			result.putString(MainActivity.DATA_RETURNED, "Random number: " + mRandom.nextInt(1000));
			receiver.send(MainActivity.RESULT_DATA_RETURNED, result);
		}
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
}