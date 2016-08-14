package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    public static String POSTURL ="NOTIF_POST_PERMANT_LINK";
    WebView myWebView;
    AdView mAdView;
    BroadcastReceiver adsConnectionChangedReceiver;
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((adsConnectionChangedReceiver),
                new IntentFilter(NetworkStatReceiver.ADSCONNECTION_INTENTFILTER)
        );
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(adsConnectionChangedReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdView= (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        FrameLayout pb = (FrameLayout) findViewById(R.id.progressBarContainer);
        myWebView = (WebView) findViewById(R.id.webview);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if( getIntent().getExtras() != null)
        {
            if(getIntent().getStringExtra(POSTURL).length() > 1) {
                myWebView.loadUrl(getIntent().getStringExtra(POSTURL));
            }else {
                myWebView.loadUrl("https://www.google.com");
            }
        }else {
            myWebView.loadUrl("https://www.google.com");
        }

        myWebView.setWebViewClient(new MyWebViewClient(this,pb));
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        adsConnectionChangedReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                if (!isInternetConnected(getBaseContext())) {
                    mAdView.setVisibility(View.GONE);
                } else {
                    mAdView.setVisibility(View.VISIBLE);
                }
            }
        };
        if (!isInternetConnected(getBaseContext())) {
            myWebView.loadUrl("file:///android_asset/index.html");
            mAdView.setVisibility(View.GONE);
        } else {
            mAdView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);

    }
    public static boolean isInternetConnected(Context context) {
        boolean returnedValue = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            boolean isConnected = activeNetwork.isConnected();
            if (isConnected) {
                returnedValue = true;
            }
        }
        return returnedValue;
    }
}
