package com.example.jgenoves.ckdexpress;

import android.app.Service;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.example.jgenoves.ckdexpress.Patient;

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
        mPatient = Patient.get(getActivity());

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


        if(!mPatient.isCheckupDue() && !mPatient.isNephVisitDue()){
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("No Current Notifications");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("Everything is looking good!");

            mNotificationMessages.setTextColor(getResources().getColor(R.color.color_text_darknavy));
            mNotificationMessages.setTextSize(20);

            mNotificationTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mNotificationTitle.setTextColor(getResources().getColor(R.color.color_good_green));


//            NotificationManager notif=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notify=new Notification.Builder
//                    (getApplicationContext()).setContentTitle(tittle).setContentText(body).
//                    setContentTitle(subject).setSmallIcon(R.drawable.abc).build();
//
//            notify.flags |= Notification.FLAG_AUTO_CANCEL;
//            notif.notify(0, notify);

        }else if(mPatient.isCheckupDue()){
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("Schedule a checkup soon!");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("It's been a while since you've had a checkup. " +
                    "Schedule a checkup with your doctor soon so you can get updated GFR" +
                    "results!");

            mNotificationMessages.setTextSize(20);
            mNotificationMessages.setTextColor(getResources().getColor(R.color.color_text_darknavy));
            mNotificationTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            //TODO:: make any/all of these colors statics in XML
            mNotificationTitle.setTextColor(getResources().getColor(R.color.color_warning_yellow));

        }else if (mPatient.isNephVisitDue()) {
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("Schedule a visit with your Nephrologist!");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("Your GFR levels have changed a bit too much!" +
                    " You should contact your doctor immediately to set up a visit " +
                    "as soon as possible");

            mNotificationMessages.setTextSize(20);
            mNotificationMessages.setTextColor(getResources().getColor(R.color.color_text_darknavy));
            mNotificationTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mNotificationTitle.setTextColor(Color.RED);
        } else{
            //there has been an error with the gfr levelsmNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
            mNotificationTitle.setText("Notification Error");
            mNotificationMessages = (TextView) v.findViewById(R.id.notification_messages);
            mNotificationMessages.setText("You shoudln't ever see this, if you do" +
                    " that means there is an error on our end");

            mNotificationMessages.setTextSize(20);
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
