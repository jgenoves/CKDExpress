package com.example.jgenoves.ckdexpress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LookUpPatientFragment extends Fragment {

    private TextView mLookUpTitle;
    private EditText mEnterPatientId;
    private Button mGo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_patient_id, container, false);

        mLookUpTitle = (TextView) v.findViewById(R.id.lookup_patient_title);
        mEnterPatientId = (EditText) v.findViewById(R.id.enter_patient_id);
        mGo = (Button) v.findViewById(R.id.look_up_patient_btn);
        mGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });


        return v;
    }
}
