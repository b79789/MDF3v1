package com.brianstacks.servicefundamentals;

/**
 * Created by Brian Stacks
 * on 1/13/15
 * for FullSail.edu.
 */
public class Song {
    private String mTitle;
    private String mArtist;
    private int mArt;
    private String mUri;

    public Song(){
        mTitle="";
        mArtist="";
        mArt=0;
    }

    public String getmTitle(){
        return this.mTitle;
    }

    public void setmTitle(String title){
        this.mTitle=title;
    }

    public String getmArtist(){
        return this.mArtist;
    }

    public void setmArtist(String artist){
        this.mArtist=artist;
    }

    public int getmArt()
    {
         return this.mArt;
    }

    public void setmArt(int art){
        this.mArt=art;
    }

    public String getmUri(){
        return this.mUri;
    }

    public void setmUri(String uri){
        this.mUri=uri;
    }
}
