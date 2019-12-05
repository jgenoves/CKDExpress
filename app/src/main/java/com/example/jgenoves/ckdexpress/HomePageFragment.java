package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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


import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.google.firebase.database.FirebaseDatabase.getInstance;


public class HomePageFragment extends Fragment {

    private Patient mPatient;
    private View mLineBreak_1;
    private TextView mWelcome;
    private TextView mSummaryTitle;

    private TextView mRecentScoreTitle;
    private TextView mCkdStageTitle;
    private TextView mBaseGFRScoreTitle;

    private TextView mRecentScore;
    private TextView mCkdStage;
    private TextView mBaseGFRScore;

    private Button mNavButton;
    private Button mNotificationButton;
    private TextView mSignOut;

    private static final String TAG = "HomePageFragment";


    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPatient = Patient.get(getActivity());

//        Intent i = PollService.newIntent(getActivity());
//        getActivity().startService(i);
        PollService.setServiceAlarm(getActivity(), true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        mDatabase = FirebaseFirestore.getInstance();
        mWelcome = (TextView) v.findViewById(R.id.welcome_text);
        mLineBreak_1 = (View) v.findViewById(R.id.line_break1);
        mSummaryTitle = (TextView) v.findViewById(R.id.summary_title);
        mRecentScoreTitle = (TextView) v.findViewById(R.id.recent_score_title);
        mCkdStageTitle = (TextView) v.findViewById(R.id.ckd_stage_title);
        mBaseGFRScoreTitle = (TextView) v.findViewById(R.id.base_gfr_score_title);

        mRecentScore = (TextView) v.findViewById(R.id.recent_score);
        mCkdStage = (TextView) v.findViewById(R.id.ckd_stage);
        mBaseGFRScore = (TextView) v.findViewById(R.id.base_gfr_score);

        loadPatientData();

        mNavButton = (Button) v.findViewById(R.id.to_egfr_scores_button);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = eGFRListActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });
        mSignOut = (TextView) v.findViewById(R.id.sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient.get(getActivity()).resetPatient();
                getActivity().finish();
            }
        });


        mNotificationButton = (Button) v.findViewById(R.id.to_notifications_button);
        mNotificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = notificationActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });


        return v;
    }

    public void loadPatientData() {

        DocumentReference patientRef = FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid());


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

                       baseGfr = Math.round(baseGfr);

                       mPatient.setBaseGFRLevel(baseGfr);
                       mPatient.setFirstName(fName);
                       mPatient.setLastName(lName);
                       mPatient.setCKDStage(ckdS);


                       mWelcome.setText("Welcome back, " + mPatient.getFirstName() + ".");
                       mCkdStage.setText(mPatient.getCKDStage());
                       mBaseGFRScore.setText(Double.toString(baseGfr));

                       DocumentReference doc = document.getReference();
                       CollectionReference gfrScoresRef = doc.collection("GFRScores");

                       gfrScoresRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if(task.isSuccessful()){
                                   mPatient.resetScores();
                                   QuerySnapshot qSnapshot = task.getResult();
                                   List<DocumentSnapshot> gfrScoresDocs = qSnapshot.getDocuments();
                                   for(DocumentSnapshot s:gfrScoresDocs){
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


                                   mRecentScore.setText("" + mPatient.getFirstGFRScore().getScore());

                                   mPatient.isNephDue();

                                   if
                                   (mPatient.isNephVisitDue()){
                                       mRecentScore.setTextColor(Color.RED);
                                       Drawable error_red = getContext().getResources().getDrawable(R.drawable.ic_error_red);
                                       mRecentScore.setCompoundDrawablesWithIntrinsicBounds(null,null,error_red,null);
                                   }

                               }
                               else{
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
       }
        );


    }



}
