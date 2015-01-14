package com.brianstacks.servicefundamentals;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicControlsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicControlsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicControlsFragment extends Fragment {
    public static final String TAG = "MusicControlsFragment.TAG";
    TextView tv;
    private AudioService audioSrv;
    boolean mBound = false;
    private OnFragmentInteractionListener mListener;


    public static MusicControlsFragment newInstance() {
        MusicControlsFragment fragment = new MusicControlsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MusicControlsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_controls, container, false);
    }
    @Override
    public void onActivityCreated(final Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        tv = (TextView)getActivity().findViewById(R.id.trackText);
        Button pauseButton = (Button)getActivity().findViewById(R.id.pauseButton);
        Button playButton = (Button)getActivity().findViewById(R.id.playButton);
        Button stopButton = (Button)getActivity().findViewById(R.id.stopButton);
        Button skipForwardB = (Button)getActivity().findViewById(R.id.skipForward);
        Button skipBackB = (Button)getActivity().findViewById(R.id.skipBack);
        Button exitButton = (Button)getActivity().findViewById(R.id.exitApp);
        final ToggleButton loopButton = (ToggleButton) getActivity().findViewById(R.id.loopButton);
        ToggleButton randomButton = (ToggleButton)getActivity().findViewById(R.id.randButton);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("com.android.activity.SEND_DATA"));
        final String uri1 = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.herstrut;
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(this.getActivity(), Uri.parse(uri1));
        String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        tv.setText("Artist :" + artist + System.getProperty("line.separator") + "Title: " + title);
        tv.setAllCaps(true);
        tv.setTextColor(Color.GREEN);
        tv.setTypeface(null, Typeface.BOLD);
        loopButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //Boolean loopGoing = audioSrv.mPlayer.isLooping();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    //loopGoing=true;
                } else {

                    // The toggle is disabled
                    //loopGoing=false;
                }

            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioSrv ==null){
                    // created an intent object that goes to the audio service class
                    Intent objIntent = new Intent(getActivity().getApplicationContext(), AudioService.class);
                    // mContext is defined upper in code, I think it is not necessary to explain what is it
                    getActivity().getApplicationContext().bindService(objIntent, mConnection, Context.BIND_AUTO_CREATE);
                    getActivity().getApplicationContext().startService(objIntent);

                }else {
                    audioSrv.onPlay();
                }

            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onPause();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioSrv.onStop();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        skipForwardB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioSrv.onSkipForward();
            }
        });
        skipBackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onSkipback();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(getActivity().getApplicationContext(), AudioService.class);
                getActivity().stopService(objIntent);
                getActivity().finish();
                System.exit(0);
            }
        });



    }




    @Override
    public void onStop() {
        super.onStop();
        if (mBound ){
            getActivity().getApplicationContext().unbindService(mConnection);

        }


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) service;
            //get service
            audioSrv = binder.getService();
            mBound=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            audioSrv=null;
            mBound=false;
        }
    };



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("artist");
            String message2 = intent.getStringExtra("title");
            Log.d("receiver", "Got message: " + message + "2:  " + message2);
        }
    };


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction();

    }

}
