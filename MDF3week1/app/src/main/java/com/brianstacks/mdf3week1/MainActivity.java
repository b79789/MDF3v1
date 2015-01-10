package com.brianstacks.mdf3week1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playClick(View view){

        Intent intent = new Intent(this, MusicService.class);
        startService(intent); // Creates, starts.
    }

    public void pauseClick(View view){

    }

    public void stopClick(View view){

        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }

}
