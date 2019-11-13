package com.example.jgenoves.ckdexpress;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Patient {

    private static Patient sPatient;


    private String mFirstName;
    private String mLastName;
    private List<EGFREntry> mGfrScores;
    private double mBaseGFRLevel;

    private boolean mCheckupDue;
    private boolean mNephVisitDue;

    public static Patient get(Context context){
        if(sPatient == null){
            sPatient = new Patient(context);
        }
        return sPatient;
    }


    public Patient(Context context){
        mFirstName = "Jordan";
        mLastName= "Genovese";
        mGfrScores = new ArrayList<EGFREntry>();
        mBaseGFRLevel = 50;
        mCheckupDue = false;
        mNephVisitDue = false;


        for(int i = 0; i < 10; i++){
            EGFREntry e = new EGFREntry();
            e.setScore(50);
            mGfrScores.add(e);
        }
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String name) {
        mFirstName = name;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String name) {
        mLastName = name;
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



    public void addGFRScore(EGFREntry e){
        mGfrScores.add(e);
    }

    public EGFREntry getGFRScore(UUID id){
        for(EGFREntry e:mGfrScores){
            if(e.getId().equals(id)){
                return e;
            }

        }
        return null;
    }




}
