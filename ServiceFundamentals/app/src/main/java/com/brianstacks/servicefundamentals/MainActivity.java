package com.brianstacks.servicefundamentals;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ServiceConnection{

    private AudioService audioSrv;
    private Intent playIntent;
    private boolean musicBound=false;
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        tv = (TextView)findViewById(R.id.trackText);
        Button pauseButton = (Button)findViewById(R.id.pauseButton);
        Button playButton = (Button)findViewById(R.id.playButton);
        Button stopButton = (Button)findViewById(R.id.stopButton);
        Button skipForwardB = (Button)findViewById(R.id.skipForward);
        Button skipBackB = (Button)findViewById(R.id.skipBack);
        AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) service;
        //get service
        audioSrv = binder.getService();
        tv.setText("Brian");
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
                Toast.makeText(getApplicationContext(),"paused//",Toast.LENGTH_SHORT).show();;
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onStop();
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
                audioSrv.onSkipbnack();
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        unbindService(this);
    }

}
