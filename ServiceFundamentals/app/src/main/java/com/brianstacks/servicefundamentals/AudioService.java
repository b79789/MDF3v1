package com.brianstacks.servicefundamentals;

import android.annotation.TargetApi;
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
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.io.IOException;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */

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

        return false;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOGCAT, "Service Started!");
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        //getApplicationContext().sendBroadcast(intent);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.heavens_small);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heavens));
        builder.setContentTitle(artist);
        builder.setContentText(title);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(final Intent intent, int flags, int startId) {

        mPlayer = new MediaPlayer();
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        if (intent != null) {
            final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
            final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
            final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
            final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
            final String[] tracks = {uri1, uri2, uri3, uri4};
            Integer[] imageIDs = {R.drawable.likearock, R.drawable.oldtimerock, R.drawable.bs, R.drawable.bs4};
            metaRetriever.setDataSource(this, Uri.parse(uri1));
            artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
          /*  if (id == 1) {
                metaRetriever.setDataSource(this, Uri.parse(uri1));
            } else if (id == 2) {
                metaRetriever.setDataSource(this, Uri.parse(uri2));
            } else if (id == 3) {
                metaRetriever.setDataSource(this, Uri.parse(uri3));
            } else if (id == 4) {
                metaRetriever.setDataSource(this, Uri.parse(uri4));
            }*/
            Song song1 = new Song();
            song1.setmArtist("Bob Seger and the Silver Bullet Band");
            song1.setmTitle("Her Strut");
            song1.setmUri(uri1);
            song1.setmArt(imageIDs[0]);
            Song song2 = new Song();
            song2.setmArtist("Bob Seger and the Silver Bullet Band");
            song2.setmTitle("Turn the Page");
            song2.setmUri(uri2);
            song2.setmArt(imageIDs[1]);
            Song song3 = new Song();
            song3.setmArtist("Bob Seger and the Silver Bullet Band");
            song3.setmTitle("Old-Time Rock-N-Roll");
            song3.setmUri(uri3);
            song3.setmArt(imageIDs[2]);
            Song song4 = new Song();
            song4.setmArtist("Bob Seger and the Silver Bullet Band");
            song4.setmTitle("Like a Rock");
            song4.setmUri(uri4);
            song4.setmArt(imageIDs[3]);
            Log.v("Song1", song3.getmArtist() + " " + song3.getmTitle());
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

           /* mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentTrack = (currentTrack + 1) % tracks.length;
                    Uri nextTrack = Uri.parse(tracks[currentTrack]);

                    mp.reset();
                    try {
                        mp.setDataSource(getApplicationContext(), nextTrack);
                        id = 2;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.prepareAsync();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            currentTrack = (currentTrack + 2) % tracks.length;
                            Uri nextTrack2 = Uri.parse(tracks[currentTrack]);
                            mp.reset();
                            try {
                                mp.setDataSource(getApplicationContext(), nextTrack2);
                                id = 3;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mp.prepareAsync();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    currentTrack = (currentTrack + 3) % tracks.length;
                                    Uri nextTrack3 = Uri.parse(tracks[currentTrack]);
                                    mp.reset();
                                    try {
                                        mp.setDataSource(getApplicationContext(), nextTrack3);
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
                                            onStartCommand(intent, 0, 0);

                                        }
                                    });
                                }
                            });
                        }
                    });
                }

            });*/


        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mPlayer.release();
        stopForeground(true);
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
        mPlayer.stop();
        mPlayer.start();
    }

    public void onSkipback() {
        mPlayer.seekTo(currentTrack - 1);
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

        mPlayer.selectTrack(getRandomNumber(tracks.length));


    }

    public void loopPlayTrue() {
        mPlayer.start();
        mPlayer.setLooping(true);

    }

    public void loopPlayFalse() {
        mPlayer.setLooping(false);

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        final String uri1 = "android.resource://" + getPackageName() + "/" + R.raw.herstrut;
        final String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.turnthepage;
        final String uri3 = "android.resource://" + getPackageName() + "/" + R.raw.oldtime;
        final String uri4 = "android.resource://" + getPackageName() + "/" + R.raw.likearock;
        final String[] tracks = {uri1, uri2, uri3, uri4};
        Integer[] imageIDs = {R.drawable.likearock, R.drawable.oldtimerock, R.drawable.bs, R.drawable.bs4};
        metaRetriever.setDataSource(this, Uri.parse(uri1));
        artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        currentTrack = (currentTrack + 1) % tracks.length;
        Uri nextTrack = Uri.parse(tracks[currentTrack]);

        mp.reset();
        try {
            mp.setDataSource(getApplicationContext(), nextTrack);
            id = 2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
    }

    public class AudioServiceBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

}