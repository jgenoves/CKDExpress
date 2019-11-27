package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
                if(task.isSuccessful()){
                    mPatient.setUser(task.getResult().getUser());
                    Intent intent = HomePageActivity.newIntent(getActivity());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "Login Failed! Please try again.", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}

