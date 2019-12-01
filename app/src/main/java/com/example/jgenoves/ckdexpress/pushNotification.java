//package com.example.jgenoves.ckdexpress;
//
//import android.os.Build;
//import androidx.core.app.NotificationCompat;
//import android.app.NotificationManager;
//import android.app.NotificationChannel;
//
//public class pushNotification extends NotificationCompat {
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//
//    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle(textTitle)
//            .setContentText(textContent)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//}
