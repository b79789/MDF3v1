package com.brianstacks.mdf3week1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

/**
 * Created by Brian Stacks
 * on 1/8/15
 * for FullSail.edu.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    MediaPlayer mPlayer;
    public static final int NOTIFICATION_ID = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;
    private static final int STANDARD_NOTIFICATION = 0x01001;
    private static final int EXPANDED_NOTIFICATION = 0x01002;
    NotificationManager mManager;
    String title;
    String artist;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;

        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(this,Uri.parse(uri2));
        artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.heavens_small);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heavens));
        builder.setContentTitle(artist);
        builder.setContentText(title);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        mPlayer = MediaPlayer.create(this,Uri.parse(uri2));

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.stop();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }
}
