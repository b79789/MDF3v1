package com.fullsail.android.boundservicedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

import com.fullsail.android.boundservicedemo.BoundService.BoundServiceBinder;

public class MainActivity extends Activity implements OnClickListener, ServiceConnection {
	
	BoundService mService;
	boolean mBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.service_starter).setOnClickListener(this);
		findViewById(R.id.service_stopper).setOnClickListener(this);
		findViewById(R.id.service_binder).setOnClickListener(this);
		findViewById(R.id.service_unbinder).setOnClickListener(this);
		findViewById(R.id.show_text).setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, BoundService.class);
		if(v.getId() == R.id.service_starter) {
			startService(intent);
		} else if(v.getId() == R.id.service_stopper) {
			stopService(intent);
		} else if(v.getId() == R.id.service_binder) {
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        } else if(v.getId() == R.id.service_unbinder) {
			if(mBound) {
				unbindService(this);
				mBound = false;
				mService = null;
			}
		} else if(v.getId() == R.id.show_text) {
			if(mService != null) {
				mService.showText("Some text!");
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(mBound) {
			unbindService(this);
			
			// Backup in case the service was started and not stopped.
			// Not needed if only binding was used.
			Intent intent = new Intent(this, BoundService.class);
			stopService(intent);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		BoundServiceBinder binder = (BoundServiceBinder)service;
		mService = binder.getService();
		mBound = true;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
		mBound = false;
	}
}
