package com.example.jgenoves.ckdexpress;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class EGFREntry implements Serializable {

    private UUID mId;
    private double mScore;
    private Date mDate;

    public EGFREntry() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
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




    
}
