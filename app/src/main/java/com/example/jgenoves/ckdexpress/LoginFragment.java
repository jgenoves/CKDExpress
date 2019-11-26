package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
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


    FirebaseAuth mFirebaseAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        user = null;

        if(user != null){
            Intent intent = HomePageActivity.newIntent(getActivity());
            startActivity(intent);
        }


        mEmailText = (EditText) v.findViewById(R.id.login_email);
        mPasswordText = (EditText) v.findViewById(R.id.login_password);
        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser("genovese.jordan9@gmail.com", "Volkl175");
            }
        });

        return v;
    }

    public void loginUser(String email, String password){
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Patient p = Patient.get(getActivity());
                    p.setUserId(task.getResult().getUser().getUid());
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

