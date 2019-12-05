package com.example.jgenoves.ckdexpress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobsandgeeks.saripaar.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class ViewPatientFragment extends Fragment {


    private Patient mPatient;

    private TextView mPatientInfoTitle;
    private TextView mPatientNameTitle;
    private TextView mPatietnDOBTitle;
    private TextView mCKDStageTitle;
    private TextView mBaseGFRScoreTitle;
    private TextView mRecentScoreTitle;
    private TextView mLastCheckupTitle;

    private TextView mPatientInfo;
    private TextView mPatientName;
    private TextView mPatientDOB;
    private TextView mCKDStage;
    private TextView mBaseGFRScore;
    private TextView mRecentScore;
    private TextView mLastCheckup;

    private Button mPatientGFRScores;
    private Button mPatientAddGFRScore;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mPatient = Patient.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_view_patient, container, false);

        mPatientNameTitle = (TextView) v.findViewById(R.id.patient_name_title);
        mPatietnDOBTitle = (TextView) v.findViewById(R.id.patient_dob_title);
        mCKDStageTitle = (TextView) v.findViewById(R.id.patient_ckd_stage_title);
        mBaseGFRScoreTitle = (TextView) v.findViewById(R.id.patient_base_gfr_title);
        mRecentScoreTitle = (TextView) v.findViewById(R.id.patient_recent_score_title);
        mLastCheckupTitle = (TextView) v.findViewById(R.id.patient_last_checkup_title);

        mPatientName = (TextView) v.findViewById(R.id.patient_name);
        mPatientDOB = (TextView) v.findViewById(R.id.patient_dob);
        mCKDStage = (TextView) v.findViewById(R.id.patient_ckdstage);
        mBaseGFRScore = (TextView) v.findViewById(R.id.patient_base_gfr);
        mRecentScore = (TextView) v.findViewById(R.id.patient_recent_score);
        mLastCheckup = (TextView) v.findViewById(R.id.patient_last_checkup);

        mPatientAddGFRScore = (Button) v.findViewById(R.id.patient_add_gfr_score);
        mPatientGFRScores = (Button) v.findViewById(R.id.patient_gfr_scores);


        loadPatientData();



        return v;


    }

    public void loadPatientData(){
        DocumentReference patientRef = FirebaseFirestore.getInstance().collection("patients").document(mPatient.getTempUserId());

        //Wipe patient ID from singleton. No need for it anymore
        mPatient.setTempUserId("");


        patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String fName = document.getString("firstName");
                        String lName = document.getString("lastName");
                        String ckdS = document.getString("ckdStage");
                        double baseGfr = document.getDouble("baseGFR");
                        Date dob = document.getDate("dob");

                        baseGfr = Math.round(baseGfr);

                        mPatient.setDOB(dob);
                        mPatient.setBaseGFRLevel(baseGfr);
                        mPatient.setFirstName(fName);
                        mPatient.setLastName(lName);
                        mPatient.setCKDStage(ckdS);

                        DocumentReference doc = document.getReference();
                        CollectionReference gfrScoresRef = doc.collection("GFRScores");

                        gfrScoresRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    mPatient.resetScores();
                                    QuerySnapshot qSnapshot = task.getResult();
                                    List<DocumentSnapshot> gfrScoresDocs = qSnapshot.getDocuments();
                                    for (DocumentSnapshot s : gfrScoresDocs) {
                                        Log.d(TAG, "DocumentSnapshot data: " + s.getData());
                                        EGFREntry e = new EGFREntry();

                                        double id = s.getDouble("id");
                                        int i = (int) id;
                                        double score = s.getDouble("gfrScore");
                                        Date date = s.getDate("date");
                                        String location = s.getString("location");

                                        e.setId(i);
                                        e.setScore(score);
                                        e.setDate(date);
                                        e.setLocation(location);

                                        mPatient.addGFRScore(e);

                                    }

                                    mPatient.isACheckupDue();
                                    mPatient.isNephDue();



                                    Date date = mPatient.getDOB();
                                    SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");
                                    String dob = formatter.format(date);
                                    String last_checkup = formatter.format(mPatient.getFirstGFRScore().getDate());

                                    mPatientName.setText(mPatient.getFirstName()+" "+mPatient.getLastName());
                                    mPatientDOB.setText(dob);
                                    mCKDStage.setText(mPatient.getCKDStage());
                                    mRecentScore.setText(""+mPatient.getFirstGFRScore().getScore());
                                    mBaseGFRScore.setText(""+mPatient.getBaseGFRLevel());
                                    mLastCheckup.setText(last_checkup);


                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }


                        });


                    } else {
                        Log.d(TAG, "No such user");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });


    }






}


