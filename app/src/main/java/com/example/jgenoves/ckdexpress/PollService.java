package com.example.jgenoves.ckdexpress;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.graphics.Color;
import com.example.jgenoves.ckdexpress.Patient;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    private Patient mPatient;
    private static final String TAG = "PollService";

    //set interval to 1 minute
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static Intent newIntent(Context context){
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


        if(isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.i(TAG, "received an intent: " + intent);
        System.out.println("printing stuff if its work-ing?");


        mPatient = Patient.get(this);

// MAx you are the boy. Thank you for your assistance in creating this app
        if(mPatient.isCheckupDue()) {

            //if you need to create a new notification, this is how
            int NOTIFICATION_ID = 001;
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "NotificationChannel";

            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "NotificationChannel";
                String description = "Notification";

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.setShowBadge(true);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.checkup_soon)
                    .setContentTitle("Schedule a Check Up Soon")
                    .setContentText("You need to schedule a checkup soon so you can get an updated GFR score");

            Intent resultIntent = new Intent(this, notificationActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomePageActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

        } else if(mPatient.isNephVisitDue()) {
            int NOTIFICATION_ID = 001;
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "NotificationChannel";

            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "NotificationChannel";
                String description = "Notification";

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(description);
                mChannel.setShowBadge(true);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.neph_visit_soon)
                    .setContentTitle("Schedule a Visit!")
                    .setContentText("It looks like you need to schedule an appointment with your Nephrologist as soon as possible");

            Intent resultIntent = new Intent(this, notificationActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(notificationActivity.class);
//            stackBuilder.addNextIntent(HomePageActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());


        }

    }
}
