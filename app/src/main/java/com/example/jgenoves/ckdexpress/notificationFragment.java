package com.example.jgenoves.ckdexpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class notificationFragment extends Fragment {
    //this was all copied from the homepage fragment class
    //going forward, I'll edit it so it actually fits for the class
    //TODO::Finish this class and create an XML

    private Patient mPatient;
    private View mLineBreak_1;
    private TextView mWelcome;
    private TextView mAlertTitle;
    private TextView mAlertMessages;
    private Button mNavButton;
    private Button mNotificationButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mPatient = Patient.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        mLineBreak_1 = (View) v.findViewById(R.id.line_break1);

        mWelcome = (TextView) v.findViewById(R.id.welcome_text);
        mWelcome.setText("Welcome back, \n" + mPatient.getFirstName() + ".");

        mAlertTitle = (TextView) v.findViewById(R.id.alert_title);
        mAlertMessages = (TextView) v.findViewById(R.id.alert_messages);


        mNavButton = (Button) v.findViewById(R.id.to_egfr_scores_button);
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = eGFRListActivity.newIntent(getActivity());
                startActivity(intent);


            }
        });

        mNotificationButton = (Button) v.findViewById(R.id.to_notifications_button);
        mNotificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = notificationActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        return v;
    }
}
