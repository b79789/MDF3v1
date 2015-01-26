package com.brianstacks.mappingphotos;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Brian Stacks
 * on 1/24/15
 * for FullSail.edu.
 */
public class EnteredData implements Serializable {

    private static final long serialVersionUID = 453332330552888L;
    private String mName;
    private String mAge;
    private Uri mPic;


    public EnteredData(){
        mName="";
        mAge="";
        mPic=null;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getAge() {
        return mAge;
    }
    public void  setAge(String age) {
        mAge= age;
    }
    public Uri getPic() {
        return mPic;
    }
    public void setPic(Uri pic) {
        mPic = pic;
    }

}
