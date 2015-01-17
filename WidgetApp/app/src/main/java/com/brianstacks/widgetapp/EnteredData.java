package com.brianstacks.widgetapp;

import java.io.Serializable;

/**
 * Created by Brian Stacks
 * on 12/9/14
 * for FullSail.edu.
 */
public class EnteredData implements Serializable {

    private static final long serialVersionUID = 8733333333330552888L;
    private String mName;
    private String mAge;
    private String mRace;

    public EnteredData(){
        mName="";
        mAge="";
        mRace="";
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
    public String getRace() {
        return mRace;
    }
    public void setRace(String race) {
        mRace=race;
    }

}
