package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
    private TextView mAlertTitle;
    private TextView mAlertMessages;
    private Button mNavButton;
//    private Button mNotificationButton;

    private static final String TAG = "HomePageFragment";


    private FirebaseFirestore mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mPatient = Patient.get(getActivity());
        mDatabase = FirebaseFirestore.getInstance();
        mWelcome = (TextView) v.findViewById(R.id.welcome_text);
        mLineBreak_1 = (View) v.findViewById(R.id.line_break1);
        mAlertTitle = (TextView) v.findViewById(R.id.alert_title);
        mAlertMessages = (TextView) v.findViewById(R.id.alert_messages);
        mNavButton = (Button) v.findViewById(R.id.to_egfr_scores_button);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = eGFRListActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

//        mNotificationButton = (Button) v.findViewById(R.id.to_notifications_button);
//        mNotificationButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = notificationActivity.newIntent(getActivity());
//                startActivity(intent);
//            }
//        });


        fetchPatient();



        return v;
    }

    public void fetchPatient(){
        DocumentReference patientRef = mDatabase.collection("patients").document("evkuDTelJGYRSsr5hSxx");
        CollectionReference gfrScoresRef = mDatabase.collection("patients").document("evkuDTelJGYRSsr5hSxx").collection("GFRScores");

        patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String fName = document.getString("firstName");
                        String lName = document.getString("lastName");

                        mPatient.setFirstName(fName);
                        mPatient.setLastName(lName);

                        mWelcome.setText("Welcome back, \n" + mPatient.getFirstName() + ".");
                    } else {
                        mWelcome.setText("Error! Unable to retrieve data");
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        gfrScoresRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qSnapshot = task.getResult();
                    List<DocumentSnapshot> gfrScoresDocs = qSnapshot.getDocuments();
                    for(DocumentSnapshot s:gfrScoresDocs){
                        Log.d(TAG, "DocumentSnapshot data: " + s.getData());
                        EGFREntry e = new EGFREntry();

                        double score = s.getDouble("Score");
                        Date date = s.getDate("Date");

                        e.setScore(score);
                        e.setDate(date);

                        mPatient.addGFRScore(e);

                    }

                    mAlertMessages.setText("Score: " + mPatient.getMostRecentGFRScore().getScore());
                }
                else{
                    Log.d(TAG, "No such document");
                }
            }


        });


    }
}
