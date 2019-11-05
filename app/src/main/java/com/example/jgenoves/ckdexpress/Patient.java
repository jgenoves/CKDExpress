package com.example.jgenoves.ckdexpress;

import java.util.Date;
import java.util.List;

public class Patient {


    private String mName;

    private List<EGFREntry> mGfrScores;
    private double mBaseGFRLevel;

    private boolean mCheckupDue;
    private boolean mNephVisitDue;


    public Patient(){
        mName = "";
        mGfrScores = null;
        mBaseGFRLevel = 0.0;
        mCheckupDue = false;
        mNephVisitDue = false;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<EGFREntry> getGfrScores() {
        return mGfrScores;
    }

    public void setGfrScores(List<EGFREntry> gfrScores) {
        mGfrScores = gfrScores;
    }

    public double getBaseGFRLevel() {
        return mBaseGFRLevel;
    }

    public void setBaseGFRLevel(double baseGFRLevel) {
        mBaseGFRLevel = baseGFRLevel;
    }

    public boolean isCheckupDue() {
        return mCheckupDue;
    }

    public void setCheckupDue(boolean checkupDue) {
        mCheckupDue = checkupDue;
    }

    public boolean isNephVisitDue() {
        return mNephVisitDue;
    }

    public void setNephVisitDue(boolean nephVisitDue) {
        mNephVisitDue = nephVisitDue;
    }





}
