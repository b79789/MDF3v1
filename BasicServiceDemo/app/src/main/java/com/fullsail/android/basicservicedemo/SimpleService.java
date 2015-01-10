package com.fullsail.android.basicservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SimpleService extends Service {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Don't allow binding.
		return null;
	}
	
}