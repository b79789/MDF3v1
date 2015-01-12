package com.brianstacks.servicefundamentals;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;



public class MainActivity extends ActionBarActivity implements ServiceConnection{

    private AudioService audioSrv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // created an intent object that goes to the audio service class
        Intent objIntent = new Intent(this, AudioService.class);
        // started the service
        startService(objIntent);
        bindService(objIntent, this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        tv = (TextView)findViewById(R.id.trackText);
        Button pauseButton = (Button)findViewById(R.id.pauseButton);
        Button playButton = (Button)findViewById(R.id.playButton);
        Button stopButton = (Button)findViewById(R.id.stopButton);
        Button skipForwardB = (Button)findViewById(R.id.skipForward);
        Button skipBackB = (Button)findViewById(R.id.skipBack);
        Button exitButton = (Button)findViewById(R.id.exitApp);
        AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) service;
        //get service
        audioSrv = binder.getService();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("com.android.activity.SEND_DATA"));
        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(this, Uri.parse(uri1));
        String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        tv.setText("Artist :" + artist + System.getProperty("line.separator") + "Title: " + title);
        tv.setAllCaps(true);
        tv.setTextColor(Color.GREEN);
        tv.setTypeface(null, Typeface.BOLD);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onPlay();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onPause();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioSrv.onStop();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent objIntent = new Intent(getApplicationContext(), AudioService.class);
                stopService(objIntent);

            }
        });
        skipForwardB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onSkipForward();
            }
        });
        skipBackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onSkipback();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getApplicationContext(), AudioService.class);
                stopService(objIntent);
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        unbindService(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("artist");
            String message2 = intent.getStringExtra("title");
            Log.d("receiver", "Got message: " + message + "2:  "+message2);
        }
    };
}
