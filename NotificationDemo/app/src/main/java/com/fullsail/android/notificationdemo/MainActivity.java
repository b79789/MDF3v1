package com.fullsail.android.notificationdemo;

import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	
	private static final int STANDARD_NOTIFICATION = 0x01001;
	private static final int EXPANDED_NOTIFICATION = 0x01002;
	
	private NotificationManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.standard).setOnClickListener(this);
		findViewById(R.id.expanded).setOnClickListener(this);
		
		mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.standard) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setSmallIcon(R.drawable.ic_notification);
			builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			builder.setContentTitle("Standard Title");
			builder.setContentText("Standard Message");
			builder.setAutoCancel(true);
			mManager.notify(STANDARD_NOTIFICATION, builder.build());
			
		} else if(v.getId() == R.id.expanded) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setSmallIcon(R.drawable.ic_notification);
			builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			builder.setContentTitle("Expanded Title");
			builder.setContentText("Expanded Message");
			builder.setStyle(new NotificationCompat.BigTextStyle()
				.bigText("This is a much longer string that will be able to be viewed in full in a larger notification")
				.setSummaryText("Expanded Summary"));
			builder.addAction(android.R.drawable.ic_menu_delete, "Sample Action", null);
			mManager.notify(EXPANDED_NOTIFICATION, builder.build());
		}
	}
}
