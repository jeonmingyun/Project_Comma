//package com.org.ticketzone.app_mem.service;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.firebase.messaging.RemoteMessage;
//import com.org.ticketzone.app_mem.R;
//import com.org.ticketzone.app_mem.activity.MainActivity;
//
//public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
//    private static final String TAG = "FirebaseMsgService";
//
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
////        if (remoteMessage.getNotification() != null) {
////            sendNotification(remoteMessage.getNotification().getBody());
////        }
//
//        sendNotification(remoteMessage.getData().get("message"));
//    }
//
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//        String channelId = getString(R.string.default_notification_channel_id);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentTitle("Hello!!")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}
//
//
