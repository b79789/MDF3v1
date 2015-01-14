package com.brianstacks.servicefundamentals;

/**
 * Created by Brian Stacks
 * on 1/5/15
 * for FullSail.edu.
 */



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements MusicControlsFragment.OnFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager mgr = getFragmentManager();
        MusicControlsFragment musicControlsFragment = (MusicControlsFragment) mgr.findFragmentByTag(MusicControlsFragment.TAG);
        FragmentTransaction trans = mgr.beginTransaction();
        if (musicControlsFragment == null){
            MusicControlsFragment fragment = new MusicControlsFragment();
            trans.add(R.id.fragment_container, fragment);
            trans.commit();
        }


    }



    @Override
    public void onFragmentInteraction() {

    }
}
