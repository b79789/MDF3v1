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
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicControlsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicControlsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicControlsFragment extends Fragment implements ServiceConnection {
    public static final String TAG = "MusicControlsFragment.TAG";
    TextView tv;
    private AudioService audioSrv;

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
        // created an intent object that goes to the audio service class
        Intent objIntent = new Intent(getActivity(), AudioService.class);
        // started the service
        getActivity().startService(objIntent);
        getActivity().bindService(objIntent, this, Context.BIND_AUTO_CREATE);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent objIntent = new Intent(getActivity(), AudioService.class);

        getActivity().unbindService((ServiceConnection) objIntent);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Intent objIntent = new Intent(getActivity(), AudioService.class);

        getActivity().bindService(objIntent, this, Context.BIND_AUTO_CREATE);

        tv = (TextView)getActivity().findViewById(R.id.trackText);
        Button pauseButton = (Button)getActivity().findViewById(R.id.pauseButton);
        Button playButton = (Button)getActivity().findViewById(R.id.playButton);
        Button stopButton = (Button)getActivity().findViewById(R.id.stopButton);
        Button skipForwardB = (Button)getActivity().findViewById(R.id.skipForward);
        Button skipBackB = (Button)getActivity().findViewById(R.id.skipBack);
        Button exitButton = (Button)getActivity().findViewById(R.id.exitApp);
        RadioButton loopButton = (RadioButton) getActivity().findViewById(R.id.loopButton);
        RadioButton randomButton = (RadioButton)getActivity().findViewById(R.id.randButton);

        AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) service;
        //get service
        audioSrv = binder.getService();
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
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.onPlay();
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
                Intent objIntent = new Intent(getActivity().getApplicationContext(), AudioService.class);
                getActivity().stopService(objIntent);

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
        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Is the button now checked?
                RadioButton bchecked = (RadioButton)getActivity().findViewById(R.id.loopButton);
                        if (bchecked.isChecked()){
                            audioSrv.mPlayer.isLooping();
                        }else {
                            audioSrv.mPlayer.isPlaying();
                        }

                }

        });
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioSrv.randomPlay(getView());
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        getActivity().unbindService(this);
    }

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
