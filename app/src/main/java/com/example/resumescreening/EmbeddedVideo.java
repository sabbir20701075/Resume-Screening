package com.example.resumescreening;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmbeddedVideo extends AppCompatActivity {

    private int likeCount = 0;
    private int dislikeCount = 0;
    private WebView webView;
    private ImageView likeIcon;
    private ImageView dislikeIcon;
    private Spinner mSpinner;
    private TextView likeCountTextView, dislikeCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_video);

        // Initialize views
        webView = findViewById(R.id.webView);
        likeIcon = findViewById(R.id.like_icon);
        dislikeIcon = findViewById(R.id.dislike_icon);
        likeCountTextView = findViewById(R.id.like_count_text_view);
        dislikeCountTextView = findViewById(R.id.dislike_count_text_view);
        mSpinner = findViewById(R.id.spinner_1);

        // Set click listeners for icons
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementLikeCount();
            }
        });

        dislikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDislikeCount();
            }
        });

        // Load video in WebView
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/tUesv5u5bvA\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        // Initialize Spinner and Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    private void incrementLikeCount() {
        likeCount++;
        likeCountTextView.setText("Likes: " + likeCount);
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }

    private void incrementDislikeCount() {
        dislikeCount++;
        dislikeCountTextView.setText("Dislikes: " + dislikeCount);
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
    }
}
