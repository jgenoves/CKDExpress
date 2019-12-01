package com.example.jgenoves.ckdexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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


public class AddGFRScoreFragment extends Fragment implements Validator.ValidationListener {

    private TextView mAddGFRScoreTitle;

    @NotEmpty
    @Nonnegative
    @Max(100)
    private EditText mAddScoreValue;

    private Button mDatePicker;

    @NotEmpty
    private EditText mScoreLocation;
    private Button mNext;

    private Patient mPatient;

    private Validator validator;

    @NotEmpty
    private Date date = new Date();

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private AddGFRScoreActivity thisActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        mPatient = Patient.get(getActivity());
        thisActivity = (AddGFRScoreActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_gfr_score, container, false);

        mAddGFRScoreTitle = (TextView) v.findViewById(R.id.add_gfr_score_title);
        mAddScoreValue = (EditText) v.findViewById(R.id.add_score_value);
        mDatePicker = (Button) v.findViewById(R.id.add_score_date);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(AddGFRScoreFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mScoreLocation = (EditText) v.findViewById(R.id.add_score_location);
        mNext = (Button) v.findViewById(R.id.add_gfr_score_btn);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        switch(thisActivity.getId()){
            case -1:
                mAddGFRScoreTitle.setText("Add GFR Score: ");
                mNext.setText("Add");
                break;
            case 0:
                mAddGFRScoreTitle.setText("Add GFR Score #1: ");
                mNext.setText("Next");
                break;
            case 1:
                mAddGFRScoreTitle.setText("Add GFR Score #2: ");
                mNext.setText("Next");
                break;
            case 2:
                mAddGFRScoreTitle.setText("Add GFR Score #3: ");
                mNext.setText("Finish");
                break;
        }

        return v;
    }

    @Override
    public void onValidationSucceeded(){

        EGFREntry e = new EGFREntry();
        double score = new Double(mAddScoreValue.getText().toString()).doubleValue();

        e.setScore(score);
        e.setDate(date);
        e.setLocation(mScoreLocation.getText().toString());

        if(thisActivity.getId() == -1){
            Map<String, Object> scoreMap = new HashMap<>();
            scoreMap.put("id", e.getId());
            scoreMap.put("gfrScore", e.getScore());
            scoreMap.put("date", e.getDate());
            scoreMap.put("location", e.getLocation());

            UUID id = UUID.randomUUID();
            FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid()).collection("GFRScores").document(id.toString())
                    .set(score)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Score added!.", Toast.LENGTH_LONG).show();
                                Intent intent = new AdminHomePageActivity().newIntent(getActivity());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getActivity(), "Failed!.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
        else if(thisActivity.getId() == 0){
            mPatient.changeGFRScore(e, 0);
            Intent intent = AddGFRScoreActivity.newIntent(getActivity(), 1);
            startActivity(intent);
        }
        else if(thisActivity.getId() == 1){

            mPatient.changeGFRScore(e, 1);
            Intent intent = AddGFRScoreActivity.newIntent(getActivity(), 2);
            startActivity(intent);

        }
        else if(thisActivity.getId() == 2){
            mPatient.changeGFRScore(e, 2);

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mPatient.getEmail(), mPatient.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        mPatient.setUser(task.getResult().getUser());

                        Map<String, Object> patient = new HashMap<>();
                        patient.put("firstName",  mPatient.getFirstName());
                        patient.put("lastName", mPatient.getLastName());
                        patient.put("dob", mPatient.getDOB());
                        patient.put("status", "patient");

                        FirebaseFirestore.getInstance().collection("patients").document(Patient.get(getActivity()).getUser().getUid())
                                .set(patient)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            for(EGFREntry e:mPatient.getGfrScores()){
                                                Map<String, Object> score = new HashMap<>();
                                                score.put("id", e.getId());
                                                score.put("gfrScore", e.getScore());
                                                score.put("date", e.getDate());
                                                score.put("location", e.getLocation());

                                                UUID id = UUID.randomUUID();
                                                FirebaseFirestore.getInstance().collection("patients").document(mPatient.getUser().getUid()).collection("GFRScores").document(id.toString())
                                                        .set(score)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    return;

                                                                } else {
                                                                    return;
                                                                }
                                                            }
                                                        });
                                            }
                                            Toast.makeText(getActivity(), "Patient added!.", Toast.LENGTH_LONG).show();
                                            Intent intent = new AdminHomePageActivity().newIntent(getActivity());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getActivity(), "Error! Cannot add patient.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });




                    }
                    else{
                        Toast.makeText(getActivity(), "Error! Cannot create patient account.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            this.date = date;
            mDatePicker.setText(this.date.toString());
        }
    }

}
