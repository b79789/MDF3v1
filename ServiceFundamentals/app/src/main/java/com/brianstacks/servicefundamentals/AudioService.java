package com.brianstacks.servicefundamentals;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */

// created audio service that implements media player listeners
public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{
    private static final String LOGCAT = null;
    MediaPlayer mPlayer;
    private MediaPlayer mNextPlayer;
    private MediaPlayer mNextPlayer2;
    private MediaPlayer mNextPlayer3;
    public static final int NOTIFICATION_ID = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;
    private static final int STANDARD_NOTIFICATION = 0x01001;
    private static final int EXPANDED_NOTIFICATION = 0x01002;
    String title;
    String artist;

   NotificationManager mManager;

    @Override
    public IBinder onBind(Intent objIndent) {
        return new AudioServiceBinder();
    }


    public void onCreate(){
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
        mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mPlayer = new MediaPlayer();
        mNextPlayer = new MediaPlayer();
        mNextPlayer2 = new MediaPlayer();
        mNextPlayer3 = new MediaPlayer();


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(final Intent intent, int flags, int startId){
        String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String [] mp3Uris={uri1,uri2,uri3,uri4};
        Intent mainIntent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pIntent = PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, mainIntent, 0);
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(this,Uri.parse(uri2));
        artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        Bundle bundle = new Bundle();
        bundle.putString("song_title",title);
        try {
            mPlayer.setDataSource(this, Uri.parse(uri1));
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!mPlayer.isLooping()){
            Log.d(LOGCAT, "Problem in Playing Audio");
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.reset();
                try {
                    mp.setDataSource(getApplicationContext(),Uri.parse(uri2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        try {
                            mp.setDataSource(getApplicationContext(),Uri.parse(uri3));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.prepareAsync();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                try {
                                    mp.setDataSource(getApplicationContext(),Uri.parse(uri4));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mp.prepareAsync();
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.reset();
                                        mp.release();

                                    }
                                });
                            }
                        });
                    }
                });
            }

        });


        builder.setContentIntent(pIntent);
        builder.setSmallIcon(R.drawable.heavens_small);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heavens));
        builder.setContentTitle(artist);
        builder.setContentText(title);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        startForeground(NOTIFICATION_ID, builder.build());
        return startId;
    }




    public void onDestroy(){
        mPlayer.release();
        stopForeground(true);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {

        mPlayer.start();
    }

    public void onPause(){
        mPlayer.pause();
    }

    public void onPlay(){
        mPlayer.start();
    }
    public void onStop(){
        mPlayer.stop();
    }

    public void onSkipForward(){
        mPlayer.seekTo(mPlayer.getCurrentPosition()+30000);
    }

    public void onSkipbnack(){
        mPlayer.seekTo(mPlayer.getCurrentPosition()-30000);
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public class AudioServiceBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    public void setText(String string){
        title=string;
    }
}