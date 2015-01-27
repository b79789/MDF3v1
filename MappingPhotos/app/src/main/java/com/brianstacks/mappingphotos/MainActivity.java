package com.brianstacks.mappingphotos;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements EnterDataFragment.OnFragmentInteractionListener{

    Button addButton;
    Button viewButton;
    Button mapButton;
    ArrayList<EnteredData> myArrayList;
    public static final String fileName = "enteredData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (fileExists(this, fileName)){
            FileInputStream fis = null;
            try {
                fis = openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<EnteredData> simpleClass = null;
            try {
                simpleClass = (ArrayList<EnteredData>) is.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            myArrayList = simpleClass;
            if (simpleClass != null) {
                myArrayList.get(0);
            }

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

            mapButton=(Button)findViewById(R.id.showMap);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.layout_container, frag).commit();
                }
            });



        }else {
            myArrayList=new ArrayList<EnteredData>();
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

            mapButton=(Button)findViewById(R.id.showMap);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.layout_container, frag).commit();
                }
            });


        }


    }

    @Override
    public void onDestroy(){
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
                os.writeObject(myArrayList);
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

    }

    @Override
    public void onFragmentInteraction(final EnteredData enteredData) {

        getIntent().putExtra("enteredData",enteredData);
        myArrayList.add(enteredData);

        final MainMapFragment frag = new MainMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.layout_container, frag).commit();


    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return !(file == null || !file.exists());
    }
}
