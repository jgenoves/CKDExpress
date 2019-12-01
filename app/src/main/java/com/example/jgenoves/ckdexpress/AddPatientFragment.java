package com.example.jgenoves.ckdexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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

import javax.annotation.Nonnegative;

public class AddPatientFragment extends Fragment implements Validator.ValidationListener {

    private static final String DIALOG_DATE = "DialogDate";

    private TextView mAddPatientTitle;

    @NotEmpty
    private EditText mFirstName;

    @NotEmpty
    private EditText mLastName;

    @NotEmpty
    private Button mDatePicker;

    @NotEmpty
    @Email
    private EditText mEmail;

    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText mTempPassword;


    private Button mAddPatient;
    private Validator validator;

    private String firstName = null;
    private String lastName = null;
    @NotEmpty
    private Date dateOfBirth = new Date();
    private String email = null;
    private String tempPass = null;
    private Patient mPatient;

    private static final int REQUEST_DATE = 0;




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        mPatient = Patient.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_patient, container, false);


        mPatient.resetPatient();

        mAddPatientTitle = (TextView) v.findViewById(R.id.add_patient_title);
        mFirstName = (EditText) v.findViewById(R.id.add_patient_firstname);
        mLastName = (EditText) v.findViewById(R.id.add_patient_lastname);
        mDatePicker = (Button) v.findViewById(R.id.add_patient_dob);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(dateOfBirth);
                dialog.setTargetFragment(AddPatientFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mEmail = (EditText) v.findViewById(R.id.add_patient_email);
        mTempPassword = (EditText) v.findViewById(R.id.add_patient_temporary_pass);

        mAddPatient = (Button) v.findViewById(R.id.final_add_patient_btn);
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });



        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            dateOfBirth = date;
            mDatePicker.setText(dateOfBirth.toString());
        }
    }

    @Override
    public void onValidationSucceeded(){
        addPatient();
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

    private void addPatient() {
        firstName = mFirstName.getText().toString();
        lastName = mLastName.getText().toString();
        email = mEmail.getText().toString();
        tempPass = mTempPassword.getText().toString();

        mPatient.setFirstName(firstName);
        mPatient.setLastName(lastName);

        mPatient.setEmail(email);
        mPatient.setPassword(tempPass);

        for(int i = 0; i < 3; i ++){
            EGFREntry e = new EGFREntry();
            mPatient.addGFRScore(e);
        }

        Intent intent = AddGFRScoreActivity.newIntent(getActivity(),0);
        startActivity(intent);



    }
}
