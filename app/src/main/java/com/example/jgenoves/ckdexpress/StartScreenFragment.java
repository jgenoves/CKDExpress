package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobsandgeeks.saripaar.Validator;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;


public class StartScreenFragment extends Fragment {

    private Button mLoginButton;

    private Patient mPatient;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mPatient = Patient.get(getActivity());
        mPatient.setUser(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_start_screen, container, false);

        mLoginButton = (Button) v.findViewById(R.id.ss_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPatient.getUser() != null){


                    Intent intent = HomePageActivity.newIntent(getActivity());
                    startActivity(intent);

                }
                else{

                    Intent intent = LoginActivity.newIntent(getActivity());
                    startActivity(intent);
                }
            }
        });




        return v;
    }


}
