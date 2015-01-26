package com.brianstacks.mappingphotos;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class MainActivity extends FragmentActivity implements EnterDataFragment.OnFragmentInteractionListener{

    Button addButton;
    Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainMapFragment frag = new MainMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.layout_container, frag).commit();
        addButton =(Button)findViewById(R.id.get_my_location_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction trans = getFragmentManager().beginTransaction();
                EnterDataFragment enterDataFragment = EnterDataFragment.newInstance();
                trans.replace(R.id.layout_container, enterDataFragment, EnterDataFragment.TAG);
                trans.commit();

            }
        });

        viewButton = (Button)findViewById(R.id.showInfo);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoViewFragment infoViewFragment = InfoViewFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, infoViewFragment, InfoViewFragment.TAG)
                        .commit();
            }
        });


    }

    @Override
    public void onFragmentInteraction(final EnteredData enteredData) {

        FileOutputStream fos = null;
        try {
            fos = this.openFileOutput("enteredData", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (os != null) {
                os.writeObject(enteredData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final MainMapFragment frag = new MainMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.layout_container, frag).commit();


    }


}
