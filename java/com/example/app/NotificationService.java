package com.example.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mlynx on 28/07/2016.
 */
public class NotificationService extends IntentService {
public static String POST_THUMBNAIL_IMG_URL = "POST_THUMBNAIL_IMG_URL";
    public static String POST_URL = "POST_URL";
    public static String POST_TITLE = "POST_TITLE";
    public static String POST_AUTHOR = "POST_AUTHOR";
    public NotificationService() {
        super("NotificationService");
    }

    public NotificationService(String name) {
        super(name);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        Intent Pintent = new Intent(this, MainActivity.class);
        Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Pintent.putExtra(MainActivity.POSTURL,intent.getStringExtra(POST_URL));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, Pintent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setVibrate(new long[]{1000, 500, 1000});
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle(intent.getStringExtra(POST_TITLE));
        notificationBuilder.setContentText(intent.getStringExtra(POST_AUTHOR));
        notificationBuilder.setTicker(intent.getStringExtra(POST_TITLE));
        if(intent.getStringExtra(POST_THUMBNAIL_IMG_URL).length() > 1) {
            Bitmap bitmap_image = null;
            bitmap_image = getBitmapFromURL(intent.getStringExtra(POST_THUMBNAIL_IMG_URL));
            //Bitmap bitmap_image_large_icon = Picasso.with(getBaseContext()).load(Data.get("PostThumbnail")).get();
            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
            //notificationBuilder.setLargeIcon(R.mipmap.ic_launcher);
            s.setSummaryText(intent.getStringExtra(POST_AUTHOR));
            notificationBuilder.setStyle(s);

        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
