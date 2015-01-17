package com.brianstacks.widgetapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements EnterDataFragment.OnFragmentInteractionListener{

    ArrayList<EnteredData> enteredDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enteredDataArrayList = new ArrayList<>();
        getIntent().putExtra("enteredDataArrayList", enteredDataArrayList);
        FragmentManager mgr = getFragmentManager();
        FragmentTransaction trans = mgr.beginTransaction();
        MyListFragment listFrag =  MyListFragment.newInstance("","");
        trans.add(R.id.fragment_container, listFrag, MyListFragment.TAG);
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

        Helper helper = new Helper(this);;
        Log.v("Entered Data",enteredData.getName() +" "+enteredData.getAge());
        MyListFragment listFrag = (MyListFragment) getFragmentManager().findFragmentByTag(MyListFragment.TAG);
        helper.writeToFile(this, "name", enteredData.getName());
        helper.writeToFile(this, "age", enteredData.getAge());

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
