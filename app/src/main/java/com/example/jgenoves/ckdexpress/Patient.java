package com.example.jgenoves.ckdexpress;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Patient {

    private static Patient sPatient;


    private String mEmail;
    private String mPassword;

    private String mFirstName;
    private String mLastName;
    private String mCKDStage;
    private List<EGFREntry> mGfrScores;
    private double mBaseGFRLevel;
    private boolean mCheckupDue;
    private boolean mNephVisitDue;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mUser;

    public static Patient get(Context context){
        if(sPatient == null){
            sPatient = new Patient(context);
        }
        return sPatient;
    }



    public Patient(Context context){
        mEmail = "`x";
        mPassword = "x";

        mFirstName = "";
        mLastName= "";
        mCKDStage="";
        mGfrScores = new ArrayList<EGFREntry>();
        mBaseGFRLevel = 0;
        mCheckupDue = false;
        mNephVisitDue = false;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();



    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }


    public FirebaseUser getUser() {
        return mUser;
    }

    public void setUser(FirebaseUser user) {
        mUser = user;
    }


    public String getCKDStage() {
        return mCKDStage;
    }

    public void setCKDStage(String CKDStage) {
        mCKDStage = CKDStage;
    }


    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
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

    public EGFREntry getMostRecentGFRScore(){
        return mGfrScores.get(mGfrScores.size()-1);
    }

    public void resetScores(){
        mGfrScores = new ArrayList<EGFREntry>();
    }

    public void resetPatient(){
        mFirebaseAuth.signOut();
        sPatient = null;

    }




}
