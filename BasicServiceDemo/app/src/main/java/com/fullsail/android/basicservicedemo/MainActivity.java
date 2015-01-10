package com.fullsail.android.basicservicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.service_starter).setOnClickListener(this);
		findViewById(R.id.service_stopper).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.service_starter) {
			Intent intent = new Intent(this, SimpleService.class);
			startService(intent);
		} else if(v.getId() == R.id.service_stopper) {
			Intent intent = new Intent(this, SimpleService.class);
			stopService(intent);
		}
	}
}
