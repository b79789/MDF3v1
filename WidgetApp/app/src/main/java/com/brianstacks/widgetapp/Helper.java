package com.brianstacks.widgetapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Brian Stacks
 * on 12/5/14
 * for FullSail.edu.
 */
public class Helper {

    private Boolean mOnline;


    public Helper(Context context) {


}

    public boolean getNetInfo() {
        return mOnline;
    }

    public void writeToFile(Context _c, String _filename, String _data) {
        File external = _c.getExternalFilesDir(null);
        File file = new File(external, _filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            // Write bytes to the stream
            fos.write(_data.getBytes());
            // Close the stream to save the file.
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(Context context, String _filename) {
        File external = context.getExternalFilesDir(null);
        File file = new File(external, _filename);

        try {
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader inReader = new InputStreamReader(fin);
            BufferedReader reader = new BufferedReader(inReader);

            // Reading data from our file using the reader
            // and storing it our string buffer.
            StringBuffer buffer = new StringBuffer();
            String text = null;
            // Make sure a line of text is available to be read.
            while ((text = reader.readLine()) != null) {
                buffer.append(text + "\n");
            }
            // Close the reader and underlying stream.
            reader.close();
            // Convert the buffer to a string.
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
