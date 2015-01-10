package com.brianstacks.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Brian Stacks
 * on 1/9/15
 * for FullSail.edu.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{

    private static final String LOGCAT = null;
    MediaPlayer mediaPlayer;
    private static final int NOTIFY_ID=1;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;



    public void onCreate() {
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
        mediaPlayer = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this,"Started",Toast.LENGTH_LONG).show();
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {


        Intent notIntent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pIntent = PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, notIntent, 0);
        builder.setContentIntent(pIntent);
        startForeground(NOTIFY_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMusicPlayer(){
        //set player properties
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }
    public void Go(){
        mediaPlayer.start();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }
    //skip to next
    public void playNext(){

    }
}
