package com.example.questioner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SplashScreenActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.my);
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

        mWebView.loadUrl("file:///android_asset/show.html");
        //we will check later if user is already logged in or not.

        new CountDownTimer(5000,2000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = getSharedPreferences("QUIZOPEDIA",MODE_PRIVATE);
                if(sharedPreferences.getString("USER","").isEmpty()){
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                }else{
                    startActivity(new Intent(SplashScreenActivity.this,TopicActivity.class));
                }
                SplashScreenActivity.this.finish();
            }
        }.start();

    }
}