package com.brianstacks.servicefundamentals;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements MusicControlsFragment.OnFragmentInteractionListener{

    private AudioService audioSrv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager mgr = getFragmentManager();
        FragmentTransaction trans = mgr.beginTransaction();
        MusicControlsFragment musicControlsFragment =  MusicControlsFragment.newInstance();
        trans.replace(R.id.fragment_container, musicControlsFragment, MusicControlsFragment.TAG);
        trans.commit();

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction() {

    }
}
