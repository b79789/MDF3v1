package com.brianstacks.mappingphotos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EnterDataFragment extends Fragment implements LocationListener{

    public static final String TAG = "EnterDataFragment.TAG";
    private OnFragmentInteractionListener mListener;
    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    LocationManager mManager;
    Uri mImageUri;
    ImageView mImageView;
    Button mButton;
    Button addButton;
    EnteredData enteredData;


    public static EnterDataFragment newInstance() {

        return new EnterDataFragment();
    }

    public EnterDataFragment() {
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
        return inflater.inflate(R.layout.fragment_enter_data, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        Button addButton;
        if (enteredData == null){
            enteredData = new EnteredData();
        }else {
            enteredData= (EnteredData)getActivity().getIntent().getSerializableExtra("enteredData");
        }
        mManager = (LocationManager)getActivity().getSystemService(MainActivity.LOCATION_SERVICE);
        final EditText e1 = (EditText)getActivity().findViewById(R.id.e1);
        final EditText e2 = (EditText)getActivity().findViewById(R.id.e2);
        mButton = (Button)getActivity().findViewById(R.id.takePicButton);
        mImageView = (ImageView)getActivity().findViewById(R.id.myPic);
        e1.requestFocus();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputUri());
                startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageUri = getOutputUri();
                if(mImageUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                }
            }
        });
        addButton = (Button)getActivity().findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (e1.getText().toString().equals("")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Must enter name");
                    builder1.setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else if (e2.getText().toString().equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Must enter age");
                    builder1.setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if (mImageView.getDrawable()==null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Must take pic");
                    builder1.setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else {

                    enteredData.setName(e1.getText().toString());
                    enteredData.setAge(e2.getText().toString());

                    enableGps();
                    mListener.onFragmentInteraction(enteredData);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        enableGps();
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != MainActivity.RESULT_CANCELED) {
            if(data == null) {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
                addImageToGallery(mImageUri);
            } else {
                mImageView.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
                addImageToGallery((Uri)data.getParcelableExtra("data"));
            }
            enteredData.setPic(mImageUri.getPath());
        }

    }


    private void enableGps() {
        if(mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                enteredData.setLat(loc.getLatitude());
                enteredData.setLon(loc.getLongitude());


            }

        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enable GPS in the system settings.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }

                    })
                    .show();
        }
    }
    private Uri getOutputUri() {
        String imageName = new SimpleDateFormat("MMddyyyy_HHmmss")
                .format(new Date(System.currentTimeMillis()));
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Creating our own folder in the default directory.
        File appDir = new File(imageDir, "CameraExample");
        appDir.mkdirs();
        File image = new File(appDir, imageName + ".jpg");
        try {
            image.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(image);
    }

    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addButton =(Button)getActivity().findViewById(R.id.get_my_location_button);
        addButton.setVisibility(View.INVISIBLE);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        addButton =(Button)getActivity().findViewById(R.id.get_my_location_button);
        addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        enableGps();
    }

    @Override
    public void onPause() {
        super.onPause();

        mManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        enteredData.setLat(location.getLatitude());
        enteredData.setLon(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(EnteredData enteredData);
    }


}
