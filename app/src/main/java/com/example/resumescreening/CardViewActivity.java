package com.example.resumescreening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;


public class CardViewActivity extends AppCompatActivity {

    CardView calenderCard,feedbackCard,videoCard,locationCard,logoutCard,uploadCard,weatherCard,animationCard,graphqlCard;
    private ImageView amazonImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        calenderCard = findViewById(R.id.calenderCard);
        feedbackCard = findViewById(R.id.feedbackCard);
        videoCard = findViewById(R.id.videoCard);
        locationCard = findViewById(R.id.locationCard);
        animationCard = findViewById(R.id.animationCard);
        uploadCard = findViewById(R.id.uploadCard);
        logoutCard = findViewById(R.id.logoutCard);
        weatherCard = findViewById(R.id.weatherCard);
        graphqlCard = findViewById(R.id.graphqlCard);



        amazonImage = findViewById(R.id.amazonImage); // Get reference to the ImageView

        // Set an OnClickListener on the ImageView
        amazonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ResumeClassifier activity or any other function
               startResumeClassifier();

            }
        });

        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, Weather.class);
                startActivity(intent);
            }
        });
        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, Image.class);
                startActivity(intent);
            }
        });
        graphqlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, Country.class);
                startActivity(intent);
            }
        });

        calenderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, DatePicker.class);
                startActivity(intent);
            }
        });
        feedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CardViewActivity", "Feedback card clicked");
                Intent intent = new Intent(CardViewActivity.this, RateActivity.class);
                startActivity(intent);
            }
        });
        animationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CardViewActivity", "Animation card clicked");
                Intent intent = new Intent(CardViewActivity.this, Animation.class);
                startActivity(intent);
            }
        });
        videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, EmbeddedVideo.class);
                startActivity(intent);
            }
        });



        locationCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent mapIntent = new Intent(CardViewActivity.this, Map.class);
                startActivity(mapIntent);
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the session from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // Remove all stored data
                editor.apply();   // Apply the changes

                // Redirect to the LogInActivity
                Intent logoutIntent = new Intent(CardViewActivity.this, LogInActivity.class);
                startActivity(logoutIntent);
                finish();  // Close the current activity
            }
        });

    }
    private void startResumeClassifier() {
        // You might want to start a new activity or call a function directly
        Intent intent = new Intent(this, HandwrittenDigit.class); // Replace with your actual activity name
        startActivity(intent);
    }
}
