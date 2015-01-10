package com.brianstacks.musicplayer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;


/**
 * Created by Brian Stacks
 * on 1/9/15
 * for FullSail.edu.
 */

public class MainActivity extends ActionBarActivity implements MediaController.MediaPlayerControl{

    MusicService musicService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // created action for button to start AudioService
    public void onPlayClick(View view){
        // created an intent object that goes to the audio service class
        Intent objIntent = new Intent(this, MusicService.class);
        // started the service
        startService(objIntent);
    }



    // created action for button to stop AudioService
    public void onStopClick(View view){

        Intent objIntent = new Intent(this, MusicService.class);
        stopService(objIntent);
    }

    @Override
    public void start() {

        musicService.Go();
    }

    @Override
    public void pause() {

        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
