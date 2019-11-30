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

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Url;


import java.util.List;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment implements Validator.ValidationListener{

    @NotEmpty
    @Email
    private EditText mEmailText;

    @NotEmpty
    @Password(min = 6)
    private EditText mPasswordText;


    private Button mLoginButton;

    private Patient mPatient;

    private Validator validator;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
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
        mPasswordText = (EditText) v.findViewById(R.id.login_password);
        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });



        return v;
    }

    @Override
    public void onValidationSucceeded(){
        System.out.println(mEmailText.getText().toString());
        loginUser(mEmailText.getText().toString(), mPasswordText.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void loginUser(String email, String password){
        mPatient.resetPatient();
        mPatient.getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
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
                                       getActivity().finish();
                                       Intent intent = AdminHomePageActivity.newIntent(getActivity());
                                       startActivity(intent);

                                   } else {
                                       getActivity().finish();
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
