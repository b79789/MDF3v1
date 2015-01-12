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
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.io.IOException;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */

// created audio service that implements media player listeners
public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{
    private static final String LOGCAT = null;
    int id;
    MediaPlayer mPlayer;
    public static final int NOTIFICATION_ID = 0x01001;
    String title;
    String artist;
    NotificationManager mManager;



    @Override
    public IBinder onBind(Intent objIndent) {
        return new AudioServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.release();
        return super.onUnbind(intent);
    }

    public void onCreate(){
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(final Intent intent, int flags, int startId){
        mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mPlayer = new MediaPlayer();
        if(intent != null) {
            final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
            final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
            final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
            final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.setAction(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
            try {
                mPlayer.setDataSource(getApplicationContext(), Uri.parse(uri1));
                mPlayer.prepareAsync();
                id = 1;
                mPlayer.setOnPreparedListener(this);


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!mPlayer.isLooping()) {
                Log.d(LOGCAT, "Problem in Playing Audio");
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    mp.reset();
                    try {
                        mp.setDataSource(getApplicationContext(), Uri.parse(uri2));
                        id = 2;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.prepareAsync();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            try {
                                mp.setDataSource(getApplicationContext(), Uri.parse(uri3));
                                id = 3;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mp.prepareAsync();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.reset();
                                    try {
                                        mp.setDataSource(getApplicationContext(), Uri.parse(uri4));
                                        id = 4;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mp.prepareAsync();
                                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            mp.stop();
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
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            if (id == 1) {
                metaRetriever.setDataSource(this, Uri.parse(uri1));
            } else if (id == 2) {
                metaRetriever.setDataSource(this, Uri.parse(uri2));
            } else if (id == 3) {
                metaRetriever.setDataSource(this, Uri.parse(uri3));
            } else if (id == 4) {
                metaRetriever.setDataSource(this, Uri.parse(uri4));
            }

            artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            Intent myIntent = new Intent();
            myIntent.putExtra("artist", artist);
            myIntent.putExtra("title", title);
            myIntent.setAction("com.android.activity.SEND_DATA");
            getApplicationContext().sendBroadcast(intent);
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.drawable.heavens_small);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heavens));
            builder.setContentTitle(artist);
            builder.setContentText(title);
            builder.setAutoCancel(false);
            builder.setOngoing(true);
            startForeground(NOTIFICATION_ID, builder.build());
        }
        return 1;
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
    public void onStop() throws IOException {

            mPlayer.reset();
    }

    public void onSkipForward(){
        mPlayer.seekTo(mPlayer.getCurrentPosition()+30000);
    }

    public void onSkipback(){
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

}