package com.example.questioner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class About_New extends AppCompatActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new);
        mWebView = (WebView) findViewById(R.id.my2);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //mWebView.addJavascriptInterface(new JavaScriptShareInterface(), "AndroidShareHandler");
        // mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        // mWebView.setBackgroundColor(Color.BLACK);

        mWebView.loadUrl("file:///android_asset/team.html");
    }
}