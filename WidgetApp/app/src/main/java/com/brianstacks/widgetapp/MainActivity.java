package com.brianstacks.widgetapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements EnterDataFragment.OnFragmentInteractionListener{

    ArrayList<EnteredData> enteredDataArrayList;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Helper helper = new Helper(this);
        String jsonString = helper.readFromFile(this,"enteredData");


            enteredDataArrayList = new ArrayList<>();
            getIntent().putExtra("enteredDataArrayList", enteredDataArrayList);
            FragmentManager mgr = getFragmentManager();
            FragmentTransaction trans = mgr.beginTransaction();
            MyListFragment listFrag =  new MyListFragment();
            trans.replace(R.id.fragment_container, listFrag, MyListFragment.TAG);
            trans.commit();
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


    public void addDetailsClick(View v) {
        FragmentManager mgr = getFragmentManager();
        FragmentTransaction trans = mgr.beginTransaction();
        EnterDataFragment enterDataFragment = EnterDataFragment.newInstance();
        trans.replace(R.id.fragment_container, enterDataFragment, EnterDataFragment.TAG);
        trans.commit();
    }


    @Override
    public void onFragmentInteraction2(EnteredData enteredData) {


        Helper helper = new Helper(this);
        MyListFragment listFrag = (MyListFragment) getFragmentManager().findFragmentByTag(MyListFragment.TAG);
        for (int i=0;i<enteredDataArrayList.size();i++){
            // Creating root JSONObject
            JSONObject json = new JSONObject();
            // Creating a JSONArray
            JSONArray jsonArray = new JSONArray();
            //Creating the element to populate the array
            JSONObject element = new JSONObject();
            try {
                element.put("name",enteredData.getName());
                element.put("age",enteredData.getAge());
                // Put it in the array
                jsonArray.put(element);
                // Put the array in the root JSONObject
                json.put("enteredData", jsonArray);
                // Get the JSON String
                String s = json.toString();
                // Get formatted and indented JSON String
                String s2 = json.toString(4);
                // 4 is the number of spaces to indent the string
                Log.v("listArray",s2);
                helper.writeToFile(this, "enteredData", s2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (listFrag == null) {

            listFrag = MyListFragment.newInstance(enteredData.getName(), enteredData.getAge());
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
