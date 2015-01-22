package com.brianstacks.widgetapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements EnterDataFragment.OnFragmentInteractionListener{

    ArrayList<EnteredData> enteredDataArrayList;
    public static final String fileName = "enteredData";
    // Assuming we're in an activity.
    public static final String ACTION_CUSTOM = "com.brianstacks.android.ACTION_CUSTOM";
    CustomReceiver mReceiver;
    FragmentManager mgr = getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (fileExists(this, fileName)){
            FileInputStream fis = null;
            try {
                fis = this.openFileInput(fileName);
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
            enteredDataArrayList = simpleClass;
            if (simpleClass != null) {
                Log.v("arraySize", String.valueOf(simpleClass.size()));
            }
            getIntent().putExtra("enteredDataArrayList", enteredDataArrayList);
            FragmentTransaction trans = mgr.beginTransaction();
            MyListFragment listFrag =  MyListFragment.newInstance("","","");
            trans.replace(R.id.fragment_container, listFrag, MyListFragment.TAG);
            trans.commit();


            Intent broadcast = new Intent(ACTION_CUSTOM);
            sendBroadcast(broadcast);

        }else {
            enteredDataArrayList = new ArrayList<>();
            getIntent().putExtra("enteredDataArrayList", enteredDataArrayList);
            FragmentTransaction trans = mgr.beginTransaction();
            MyListFragment listFrag =  new MyListFragment();
            trans.replace(R.id.fragment_container, listFrag, MyListFragment.TAG);
            trans.commit();
        }



    }

    public static void widgetAdd(){
    }

    public class CustomReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Intent handled here.
            Log.i("TAG", "Intent recieved: " + intent.getAction());

            if (intent.getAction() == ACTION_CUSTOM) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Toast.makeText(getApplicationContext(),"Worked from activity",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mReceiver = new CustomReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CUSTOM);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return !(file == null || !file.exists());
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


    public  void addDetailsClick(View v) {

        FragmentTransaction trans = mgr.beginTransaction();
        EnterDataFragment enterDataFragment = EnterDataFragment.newInstance();
        trans.replace(R.id.fragment_container, enterDataFragment, EnterDataFragment.TAG);
        trans.commit();
    }


    @Override
    public void onFragmentInteraction2(EnteredData enteredData) {

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
                os.writeObject(enteredDataArrayList);
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


        MyListFragment listFrag = (MyListFragment) getFragmentManager().findFragmentByTag(MyListFragment.TAG);
        if (listFrag == null) {

            listFrag = MyListFragment.newInstance(enteredData.getName(), enteredData.getAge(),enteredData.getEyeColor());
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, listFrag, MyListFragment.TAG)
                    .commit();

        } else {
            Toast.makeText(this,"Listfrag is not null",Toast.LENGTH_SHORT).show();
            DataAdapter dataAdapter = new DataAdapter(this, enteredDataArrayList);
            ListView myList = (ListView)findViewById(R.id.myList);
            myList.setAdapter(dataAdapter);
        }
    }

}
