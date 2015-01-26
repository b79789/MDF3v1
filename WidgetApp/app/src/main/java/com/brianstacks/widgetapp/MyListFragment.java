package com.brianstacks.widgetapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;



public class MyListFragment extends Fragment {
    public static final String TAG = "MyListFrag.TAG";
    private static final String ARG_Name = "name";
    private static final String ARG_Age = "age";
    private static final String ARG_Eye = "eye";
    ArrayList<EnteredData> enteredDataArrayList;

    public static int deletePos;

    // TODO: Rename and change types of parameters
    private String mName;
    private String mAge;
    private String mEye;
    EnteredData enteredData;

    public static MyListFragment newInstance(String name, String age,String eye) {
        MyListFragment fragment = new MyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_Name, name);
        args.putString(ARG_Age, age);
        args.putString(ARG_Eye, eye);
        fragment.setArguments(args);
        return fragment;
    }

    public MyListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_Name);
            mAge = getArguments().getString(ARG_Age);
            mEye = getArguments().getString(ARG_Eye);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_list, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        Bundle args = getArguments();
        Intent i = getActivity().getIntent();
        enteredDataArrayList = (ArrayList<EnteredData>)i.getSerializableExtra("enteredDataArrayList");
        if(args != null && args.containsKey(ARG_Name)&& args.containsKey(ARG_Age)) {
            final EnteredData enteredData = new EnteredData();
            enteredData.setName(args.getString(ARG_Name));
            enteredData.setAge(args.getString(ARG_Age));
            enteredData.setEyeColor(args.getString(ARG_Eye));
            enteredDataArrayList.add(enteredData);
            final DataAdapter dataAdapter = new DataAdapter(getActivity(), enteredDataArrayList);
            final ListView listView = (ListView)getActivity().findViewById(R.id.myList);
            listView.setAdapter(dataAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EnteredData enteredData1 = (EnteredData)parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), DetailsViewActivity.class);;
                    intent.putExtra("name",enteredData1);
                    startActivity(intent);
                    deletePos =position;
                    parent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                            //Get your item here with the position
                            enteredDataArrayList.remove(position);

                            DataAdapter arrayAdapter = (DataAdapter) listView.getAdapter();
                            arrayAdapter.notifyDataSetChanged();
                            return true;
                        }
                    });
                }
            });
            Button resetButton = (Button)getActivity().findViewById(R.id.resetList);
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enteredDataArrayList.clear();
                    dataAdapter.notifyDataSetChanged();
                }
            });
        }



    }
}
