package com.example.jgenoves.ckdexpress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddPatientFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate";

    private TextView mAddPatientTitle;
    private EditText mFirstName;
    private EditText mLastName;
    private Button mDatePicker;
    private EditText mEmail;
    private EditText mTempPassword;
    private Button mAddPatient;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_patient, container, false);

        mAddPatientTitle = (TextView) v.findViewById(R.id.add_patient_title);
        mFirstName = (EditText) v.findViewById(R.id.add_patient_firstname);
        mLastName = (EditText) v.findViewById(R.id.add_patient_lastname);
        mDatePicker = (Button) v.findViewById(R.id.add_patient_dob);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mEmail = (EditText) v.findViewById(R.id.add_patient_email);
        mTempPassword = (EditText) v.findViewById(R.id.add_patient_temporary_pass);

        mAddPatient = (Button) v.findViewById(R.id.final_add_patient_btn);

        return v;

    }

}
