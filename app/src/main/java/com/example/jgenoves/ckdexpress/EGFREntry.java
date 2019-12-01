package com.example.jgenoves.ckdexpress;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class EGFREntry implements Comparable<EGFREntry> {

    private int mId;
    private double mScore;
    private Date mDate;


    private String mLocation;

    public EGFREntry() {
        mId = -1;
        mScore = 0.0;
        mDate = new Date();
        mLocation = "";
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getScore() {
        return mScore;
    }

    public void setScore(double score) {
        mScore = score;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    @Override
    public int compareTo(EGFREntry e) {


        if(mId < e.getId()){
            return -1;
        }
        else if(mId == getId()){
            return 0;
        }
        else if(mId > getId()){
            return 1;
        }
        else{
            return 0;
        }

    }







}
