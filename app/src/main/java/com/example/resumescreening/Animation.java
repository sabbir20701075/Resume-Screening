package com.example.resumescreening;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Animation extends AppCompatActivity {
    private static final String HTML_FILE_PATH = "file:///android_asset/animation.html";
    private WebView webView;
    private Button animateButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        // Initialize WebView
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load animation.html file into the WebView
        webView.loadUrl(HTML_FILE_PATH);

        // Initialize the button
        animateButton = findViewById(R.id.animateButton);

        // Set up button click listener
        animateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the animation using JavaScript from the HTML
                webView.loadUrl("javascript:startAnimation()");
            }
        });
    }
}
