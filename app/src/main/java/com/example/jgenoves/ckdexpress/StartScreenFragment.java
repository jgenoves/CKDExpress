package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;



public class StartScreenFragment extends Fragment {

    private Button mLoginButton;

    private Patient mPatient;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_start_screen, container, false);

        mPatient = Patient.get(getActivity());

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
