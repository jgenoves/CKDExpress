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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class AdminHomePageFragment extends Fragment {

    private TextView mWelcomeText;
    private Button mAddPatient;
    private Button mViewPatient;
    private TextView mSignOut;

    private Patient mPatient;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_admin_home, container, false);

        mPatient = Patient.get(getActivity());


        mWelcomeText = (TextView) v.findViewById(R.id.admin_welcome_text);

        mAddPatient = (Button) v.findViewById(R.id.add_patient_btn);
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = AddPatientActivity.newIntent(getActivity());
                startActivity(i);
            }
        });

        mViewPatient = (Button) v.findViewById(R.id.view_patient_btn);
        mViewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = LookUpPatientActivity.newIntent(getActivity());
                startActivity(i);
            }
        });

        mSignOut = (TextView) v.findViewById(R.id.sign_out_adm);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient.get(getActivity()).resetPatient();
                getActivity().finish();
            }
        });

        getAdminProfile();





        return v;
    }


    public void getAdminProfile(){
        DocumentReference patientRef = FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid());
        patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {
                   DocumentSnapshot document = task.getResult();
                   if (document.exists()) {

                           String fName = document.getString("firstName");
                           String lName = document.getString("lastName");

                           mPatient.setFirstName(fName);
                           mPatient.setLastName(lName);

                           mWelcomeText.setText("Welcome back, \n" + mPatient.getFirstName() + mPatient.getLastName() + ".");
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
