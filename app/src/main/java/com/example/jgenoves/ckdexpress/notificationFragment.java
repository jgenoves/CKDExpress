package com.example.jgenoves.ckdexpress;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import org.w3c.dom.Text;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class notificationFragment extends Fragment {
    /*TODO:: Base notifications are pretty much done now! just a little bit of fixing uo
    *  and making things look better, still need to implement the */
    private Patient mPatient;
    private View mLineBreak_1;
    private TextView mWelcome;
    private TextView mNotificationTitle;
    private TextView mNotificationMessages;
    private Button mNavButton;
    private Button mNotificationButton;

    //this will be set to 1, 2, 3 for testing purposes
    //this will eventually be grabbed from the patient class
    private int testGFR;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//
//        mPatient = Patient.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //need to mkae up fragment notification
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        testGFR = 1;

//        mLineBreak_1 = (View) v.findViewById(R.id.line_break1);
//
//        mWelcome = (TextView) v.findViewById(R.id.welcome_text);
//        mWelcome.setText("Welcome back, \n" + mPatient.getFirstName() + ".");


        if(testGFR == 1){
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("No Current Notifications");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("Everything is looking good!");

            mNotificationMessages.setTextColor(Color.DKGRAY);
            mNotificationTitle.setTextColor(Color.GREEN);


//            NotificationManager notif=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notify=new Notification.Builder
//                    (getApplicationContext()).setContentTitle(tittle).setContentText(body).
//                    setContentTitle(subject).setSmallIcon(R.drawable.abc).build();
//
//            notify.flags |= Notification.FLAG_AUTO_CANCEL;
//            notif.notify(0, notify);

        }else if(testGFR == 2){
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("Schedule an appointment with your doctor soon");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("It looks like your GFR level has changed a bit" +
                    " too much, you should schedule an appointment with your" +
                    " doctor within the next month");

            mNotificationMessages.setTextColor(Color.DKGRAY);
            //TODO:: make any/all of these colors statics in XML
            mNotificationTitle.setTextColor(Color.rgb(199, 79, 23));

        }else if (testGFR == 3) {
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("See your doctor ASAP");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("Your GFR levels have changed a bit too much!" +
                    " You should contact your doctor immediately to make sure " +
                    " everything is alright!");

            mNotificationMessages.setTextColor(Color.DKGRAY);
            mNotificationTitle.setTextColor(Color.rgb(207, 33, 36));
        } else{
            //there has been an error with the gfr levelsmNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("Notification Error");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("You shoudln't ever see this, if you do" +
                    " that means there is an error on our end");

            mNotificationMessages.setTextColor(Color.CYAN);
            mNotificationTitle.setTextColor(Color.CYAN);
        }


//        mNavButton = (Button) v.findViewById(R.id.to_egfr_scores_button);
//        mNavButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                Intent intent = eGFRListActivity.newIntent(getActivity());
//                startActivity(intent);
//
//
//            }
//        });

//        mNotificationButton = (Button) v.findViewById(R.id.to_notifications_button);
//        mNotificationButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = notificationActivity.newIntent(getActivity());
//                startActivity(intent);
//            }
//        });

        return v;
    }
}
