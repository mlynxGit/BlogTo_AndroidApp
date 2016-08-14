package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * Created by mlynx on 24/07/2016.
 */
public class MyWebViewClient extends WebViewClient {
    Context mcontext;
    private FrameLayout progressBarc;
    public MyWebViewClient(Context context, FrameLayout progressBarContainer) {
        mcontext = context;
        this.progressBarc=progressBarContainer;
        progressBarc.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().equals("www.google.com")) {
            // This is my web site, so do not override; let my WebView load the page
            if (!MainActivity.isInternetConnected(mcontext)) {
                view.loadUrl("file:///android_asset/index.html");
            }
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mcontext.startActivity(intent);
        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        view.loadUrl("file:///android_asset/index.html");
        super.onReceivedError(view, request, error);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBarc.setVisibility(View.GONE);
    }
}
