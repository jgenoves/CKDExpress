package com.example.jgenoves.ckdexpress;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;

public class Patient {

    private static Patient sPatient;



    private Date mDOB;

    private String mEmail;
    private String mPassword;

    private String mFirstName;
    private String mLastName;
    private String mCKDStage;
    private ArrayList<EGFREntry> mGfrScores;
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

        mDOB = null;

        mEmail = "";
        mPassword = "";

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

    public Date getDOB() {
        return mDOB;
    }

    public void setDOB(Date DOB) {
        mDOB = DOB;
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

    public ArrayList<EGFREntry> getGfrScores() {
        return mGfrScores;
    }

    public void setGfrScores(ArrayList<EGFREntry> gfrScores) {
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

    public ArrayList<EGFREntry> sortListMostRecentScoreFirst(){
        ArrayList<EGFREntry> copy = mGfrScores;
        Collections.sort(copy, Collections.reverseOrder());
        return copy;
    }

    public void addGFRScore(EGFREntry e){
        mGfrScores.add(e);
    }

    public void changeGFRScore(EGFREntry newEntry, int id){
        mGfrScores.get(id).setScore(newEntry.getScore());
        mGfrScores.get(id).setDate(newEntry.getDate());
        mGfrScores.get(id).setLocation(newEntry.getLocation());
    }

    public EGFREntry getGFRScore(int id){
        for(EGFREntry e:mGfrScores){
            if(e.getId() == (id)){
                return e;
            }

        }
        return null;
    }

    public EGFREntry getMostRecentGFRScore(){
        return mGfrScores.get(mGfrScores.size()-1);
    }

    public EGFREntry getFirstGFRScore(){
        return mGfrScores.get(0);
    }

    public void resetScores(){
        mGfrScores = new ArrayList<EGFREntry>();
    }

    public void resetPatient(){
        mFirebaseAuth.signOut();

        mDOB = null;

        mEmail = "";
        mPassword = "";

        mFirstName = "";
        mLastName= "";
        mCKDStage="";
        mGfrScores = new ArrayList<EGFREntry>();
        mBaseGFRLevel = 0;
        mCheckupDue = false;
        mNephVisitDue = false;
        mUser = mFirebaseAuth.getCurrentUser();

    }






    public void whatYouNeedPatient() {
        EGFREntry egfrEntry1 = mGfrScores.get(mGfrScores.size()-3);;
        EGFREntry egfrEntry2 = mGfrScores.get(mGfrScores.size()-2);;
        EGFREntry egfrEntry3 = mGfrScores.get(mGfrScores.size()-1);;

        mBaseGFRLevel = (egfrEntry1.getScore() + egfrEntry2.getScore() + egfrEntry3.getScore())/3;

        //if EGFR < 30 they are not included in the system and go striaght to neph
        if (egfrEntry3.getScore() < 30) {
            if(egfrEntry3.getScore() < 15){
                mCKDStage = "Stage 5";
            }else{
                mCKDStage = "Stage 4";
            }
        }
        else{
            //Determine what stage CKD they are in
            if (mBaseGFRLevel > 90) {
                mCKDStage = "Stage 1";
            }
            else if (mBaseGFRLevel >= 60 && mBaseGFRLevel <= 90){
                mCKDStage = "Stage 2";
            }
            else if (mBaseGFRLevel >= 45 && mBaseGFRLevel < 60){
                //its actually 3a, but seems easier to just use 1-6 and not 3a and 3b
                mCKDStage = "Stage 3a";
            }
            else{
                //is actually 3B, 4 and 5 are not included in the system
                mCKDStage = "Stage 3b";
            }

        }



    }

    public void isNephDue(){
        EGFREntry lastEgfrEntry = mGfrScores.get(0);

        if (mCKDStage.equals("Stage 1")){
            if ((mBaseGFRLevel - lastEgfrEntry.getScore() ) >= 10){
                mNephVisitDue = true;
            }
        }
        else if (mCKDStage.equals("Stage 2")){
            if ((mBaseGFRLevel - lastEgfrEntry.getScore() ) >= 7.5){
                mNephVisitDue = true;
            }
        }
        //3a
        else if (mCKDStage.equals("Stage 3a")){
            if ((mBaseGFRLevel - lastEgfrEntry.getScore() ) >= 10){
                mNephVisitDue = true;
            }
        }
        //3b
        else if (mCKDStage.equals("Stage 3b")){
            if ((mBaseGFRLevel - lastEgfrEntry.getScore() ) >= 5){
                mNephVisitDue = true;
            }
        }
        else if(mCKDStage.equals("Stage 4") || mCKDStage.equals("Stage 5")){
            mNephVisitDue = true;
        }
    }

    public void isACheckupDue(){
        EGFREntry lastEgfrEntry = mGfrScores.get(0);;
        Date today = new Date();
        long timeSince = today.getTime() - lastEgfrEntry.getDate().getTime();
        if (mCKDStage.equals("Stage 1")|| mCKDStage.equals("Stage 2")){
            if (timeSince > 15552000000L){
                mCheckupDue = true;
            }
        }
        //really 3a
        else if (mCKDStage.equals("Stage 3a")){
            if (timeSince > 7776000000L){
                mCheckupDue = true;
            }
        }
        //really 3b
        else if (mCKDStage.equals("Stage 3b")) {
            if (timeSince > 5184000000L){
                mCheckupDue = true;
            }
        }
    }




}
