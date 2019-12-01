package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Document;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;
import static com.google.firebase.database.FirebaseDatabase.getInstance;


public class HomePageFragment extends Fragment {

    private Patient mPatient;
    private View mLineBreak_1;
    private TextView mWelcome;
    private TextView mSummaryTitle;
    private TextView mRecentScore;
    private TextView mCkdStage;
    private Button mNavButton;
    private TextView mSignOut;

    private static final String TAG = "HomePageFragment";


    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPatient = Patient.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        mDatabase = FirebaseFirestore.getInstance();
        mWelcome = (TextView) v.findViewById(R.id.welcome_text);
        mLineBreak_1 = (View) v.findViewById(R.id.line_break1);
        mSummaryTitle = (TextView) v.findViewById(R.id.summary_title);
        mRecentScore = (TextView) v.findViewById(R.id.recent_score);
        mCkdStage = (TextView) v.findViewById(R.id.ckd_level);
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

        loadPatientData();

//        mNotificationButton = (Button) v.findViewById(R.id.to_notifications_button);
//        mNotificationButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = notificationActivity.newIntent(getActivity());
//                startActivity(intent);
//            }
//        });



        return v;
    }

    public void loadPatientData() {

        DocumentReference patientRef = FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid());

        CollectionReference gfrScoresRef = patientRef.collection("GFRScores");

        patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {
                   DocumentSnapshot document = task.getResult();
                   if (document.exists()) {
                       String fName = document.getString("firstName");
                       String lName = document.getString("lastName");
                       String ckdS = document.getString("ckdStage");


                       mPatient.setFirstName(fName);
                       mPatient.setLastName(lName);
                       mPatient.setCKDStage(ckdS);


                       mWelcome.setText("Welcome back, " + mPatient.getFirstName() + ".");
                       mCkdStage.setText("CKD Stage: " + mPatient.getCKDStage());


                   } else {
                       Log.d(TAG, "No such user");
                   }
               } else {
                   Log.d(TAG, "get failed with ", task.getException());
               }

           }
       }
        );

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
                    mPatient.isNephDue();

                    mRecentScore.setText("Recent Score: " + mPatient.getMostRecentGFRScore().getScore());


                }
                else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });
    }



}
