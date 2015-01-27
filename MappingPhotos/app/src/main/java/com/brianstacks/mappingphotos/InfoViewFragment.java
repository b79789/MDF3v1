package com.brianstacks.mappingphotos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class InfoViewFragment extends Fragment {
    public static final String TAG = "InfoViewFragment.TAG";
    public static final String fileName = "enteredData";

    private static final String Arg_Data="data";

    TextView tv1;
    TextView tv2;
    ImageView iv1;
    Button showInfoButt;

    EnteredData enteredData;
    public static InfoViewFragment newInstance() {
        return new InfoViewFragment();
    }

    public InfoViewFragment() {
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
        return inflater.inflate(R.layout.fragment_info_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        if (fileExists(getActivity(), fileName)){
            FileInputStream fis = null;
            try {
                fis = getActivity().openFileInput(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            EnteredData simpleClass = null;
            try {
                simpleClass = (EnteredData) is.readObject();
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
            enteredData = simpleClass;
            if (simpleClass != null) {
                Log.v("enteredData", enteredData.getName());
            }


        }

        enteredData = (EnteredData)getActivity().getIntent().getSerializableExtra("enteredData");

        Log.v("enteredData uri", String.valueOf(enteredData.getPic()));

        tv1 =(TextView)getActivity().findViewById(R.id.detailsTV1);
        tv2 =(TextView)getActivity().findViewById(R.id.detailsTV2);
        iv1 =(ImageView)getActivity().findViewById(R.id.detailsViewImage);
        tv1.setText(enteredData.getName());
        tv2.setText(enteredData.getAge());
        iv1.setImageBitmap(BitmapFactory.decodeFile(enteredData.getPic()));
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return !(file == null || !file.exists());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        showInfoButt =(Button)getActivity().findViewById(R.id.showInfo);
        showInfoButt.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        showInfoButt =(Button)getActivity().findViewById(R.id.showInfo);
        showInfoButt.setVisibility(View.VISIBLE);
    }

}
