package com.fullsail.android.intentservicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	public static final String EXTRA_RECEIVER = "com.fullsail.android.intentservicedemo.MainActivity.EXTRA_RECEIVER";
	public static final String DATA_RETURNED = "MainActivity.DATA_RETURNED";
	
	public static final int RESULT_DATA_RETURNED = 0x0101010;
	
	TextView mResultView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mResultView = (TextView)findViewById(R.id.returned_text);
		findViewById(R.id.start_service).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, BackgroundService.class);
		intent.putExtra(EXTRA_RECEIVER, new DataReceiver());
		startService(intent);
	}
	
	private final Handler mHandler = new Handler();
	
	public class DataReceiver extends ResultReceiver {

		public DataReceiver() {
			super(mHandler);
		}
		
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if(resultData != null && resultData.containsKey(DATA_RETURNED)) {
				mResultView.setText(resultData.getString(DATA_RETURNED, ""));
			}
		}
	}
}
