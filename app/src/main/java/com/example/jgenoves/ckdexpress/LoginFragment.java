package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;

    private Patient mPatient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mPatient = Patient.get(getActivity());

        if(mPatient.getUser() != null){
            Intent intent = HomePageActivity.newIntent(getActivity());
            startActivity(intent);
        }


        mEmailText = (EditText) v.findViewById(R.id.login_email);
        mEmailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPatient.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPasswordText = (EditText) v.findViewById(R.id.login_password);
        mPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPatient.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(mPatient.getEmail(), mPatient.getPassword());
            }
        });



        return v;
    }


    public void loginUser(String email, String password){
        mPatient.resetPatient();
        mPatient.getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mPatient.setEmail("x");
                mPatient.setPassword("x");
                if (task.isSuccessful()) {
                    mPatient.setUser(task.getResult().getUser());
                    DocumentReference patientRef = FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid());
                    patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           if (task.isSuccessful()) {
                               DocumentSnapshot document = task.getResult();
                               if (document.exists()) {
                                   if (document.getString("status") != null && document.get("status").equals("admin")) {
                                       Intent intent = AdminHomePageActivity.newIntent(getActivity());
                                       startActivity(intent);

                                   } else {

                                       Intent intent = HomePageActivity.newIntent(getActivity());
                                       startActivity(intent);

                                   }
                               } else {
                                   Log.d(TAG, "No such user");
                               }
                           } else {
                               Log.d(TAG, "get failed with ", task.getException());
                           }

                       }
                   }
                    );

                } else {
                    Toast.makeText(getActivity(), "Login Failed! Please try again.", Toast.LENGTH_LONG).show();
                }}});
    }
}
