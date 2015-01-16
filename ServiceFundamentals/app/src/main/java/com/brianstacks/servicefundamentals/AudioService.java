package com.brianstacks.servicefundamentals;
/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;



// created audio service that implements media player listeners
public class AudioService extends Service implements  MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {
    private static final String LOGCAT = null;
    int id;
    MediaPlayer mPlayer;
    public static final int NOTIFICATION_ID = 0x01001;
    String title;
    String artist;
    NotificationManager mManager;
    AudioService audioService;
    private int currentTrack = 0;

    @Override
    public IBinder onBind(Intent objIndent) {
        return new AudioServiceBinder();

    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("Unbind","Hits1");
        return false;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        //metaRetriever.setDataSource(uri1);
        String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        int numMessages = 0;
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.heavens_small);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heavens));
        builder.setContentTitle(artist);
        builder.setContentText(title);
        builder.setNumber(++numMessages);
        NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
        bigStyle.setSummaryText("This expanded notification is brought to you by StacksMobile");
        bigStyle.setBigContentTitle(artist);
        bigStyle.setSummaryText(title);
        Bitmap bigPic = BitmapFactory.decodeResource(getResources(), R.drawable.bs);
        bigStyle.bigPicture(bigPic);
        builder.setStyle(bigStyle);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v("onDestroy","Hits1");
        mPlayer.release();
        stopForeground(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(final Intent intent, int flags, int startId) {

        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        Integer[] imageIDs = {R.drawable.likearock, R.drawable.oldtimerock, R.drawable.bs, R.drawable.bs4};
        if (mPlayer == null && intent != null) {
            mPlayer = new MediaPlayer();
            try {
                Uri file = Uri.parse(tracks[this.currentTrack]);
                mPlayer.setDataSource(getApplication(), file);
                mPlayer.prepareAsync();
                id = 1;
                mPlayer.setOnPreparedListener(this);
                mPlayer.setOnCompletionListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(intent.hasExtra(MusicControlsFragment.EXTRA_RECEIVER)) {
                ResultReceiver receiver = (ResultReceiver)intent.getParcelableExtra(MusicControlsFragment.EXTRA_RECEIVER);
                Bundle result = new Bundle();
                result.putString(MusicControlsFragment.DATA_RETURNED,artist +" "+title );
                receiver.send(MusicControlsFragment.RESULT_DATA_RETURNED, result);

            }
        }else {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

            metaRetriever.setDataSource(getApplication().getApplicationContext(), Uri.parse(tracks[currentTrack+1]));
            String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            ResultReceiver receiver = (ResultReceiver)intent.getParcelableExtra(MusicControlsFragment.EXTRA_RECEIVER);
            Bundle result = new Bundle();
            result.putString(MusicControlsFragment.DATA_RETURNED,artist +" "+title );
            receiver.send(MusicControlsFragment.RESULT_DATA_RETURNED, result);
        }


        return START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        currentTrack = (currentTrack + 1) % tracks.length;
        if (currentTrack>0) {
            Uri nextTrack = Uri.parse(tracks[currentTrack]);
            mp.reset();
            try {
                mp.setDataSource(getApplicationContext(), nextTrack);
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.prepareAsync();
        }else {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(),Uri.parse(tracks[0]));
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
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





    public void onPause() {
        mPlayer.pause();
    }

    public void onPlay() {
        mPlayer.start();
    }

    public void onStop() throws IOException {
        mPlayer.stop();
    }

    public void onSkipForward() {
        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        currentTrack = (currentTrack + 1) % tracks.length;
        if (currentTrack>0){
            Uri nextTrack = Uri.parse(tracks[currentTrack]);
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(), nextTrack);
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }else {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(),Uri.parse(tracks[0]));
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onSkipback() {

        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        currentTrack = (currentTrack - 1) % tracks.length;
        if (currentTrack>0){
            Uri nextTrack = Uri.parse(tracks[currentTrack]);
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(), nextTrack);
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }else {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(),Uri.parse(tracks[0]));
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }

    }

    public int getRandomNumber(int numberOfElements) {
        java.util.Random rnd = new java.util.Random();
        return rnd.nextInt(numberOfElements);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void randomPlay() {
        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        if (currentTrack>0){
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(), Uri.parse(tracks[getRandomNumber(tracks.length)]));
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }else {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(getApplicationContext(),Uri.parse(tracks[getRandomNumber(tracks.length)]));
                id = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
        }
    }

    public void loopPlayTrue() {

        mPlayer.setLooping(true);

    }

    public void loopPlayFalse() {
        mPlayer.setLooping(false);

    }



}