package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by mlynx on 24/07/2016.
 */
public class NetworkStatReceiver extends BroadcastReceiver {
    final static String ADSCONNECTION_INTENTFILTER = "com.example.app.ADSCONNECTION";
    final static String ADSCONNECTION_VALUE = "com.example.app.ADSCONNECTION_VALUE";
    LocalBroadcastManager adslocalBroadCast;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadCast","Received!!");
        adslocalBroadCast = LocalBroadcastManager.getInstance(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            boolean isConnected = activeNetwork.isConnected();
            if(isConnected){
                sendConnectionChangeState(true);
            }else {
                sendConnectionChangeState(false);
            }
        }else {
            sendConnectionChangeState(false);
        }
    }
    public void sendConnectionChangeState(boolean state) {
        Intent intent = new Intent(ADSCONNECTION_INTENTFILTER);
        intent.putExtra(ADSCONNECTION_VALUE, state);
        adslocalBroadCast.sendBroadcast(intent);
    }
}
