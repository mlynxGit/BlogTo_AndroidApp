package com.example.app;


import android.content.Intent;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

/**
 * Created by mlynx on 25/07/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("Body"));

        //Calling method to generate notification
        sendNotification(remoteMessage.getData());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(Map<String, String> Data) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(NotificationService.POST_AUTHOR,Data.get("PostAuthor"));
        intent.putExtra(NotificationService.POST_THUMBNAIL_IMG_URL,Data.get("PostThumbnail"));
        intent.putExtra(NotificationService.POST_TITLE,Data.get("PostTitle"));
        intent.putExtra(NotificationService.POST_URL,Data.get("PostURL"));
        startService(intent);
//###############################################


    }


}
