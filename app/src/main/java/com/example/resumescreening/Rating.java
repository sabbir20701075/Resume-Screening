package com.example.resumescreening;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Rating extends AppCompatActivity {
    RatingBar ratingBar;
    DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        // Binding Rating activity with activity_rating_bar.xml layout
        ratingBar = findViewById(R.id.ratingBar);

        // Finding the specific RatingBar with its unique ID and changing its color
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        // Initialize Firebase database reference
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback");
    }

    // Function to be called when the submit button is clicked
    public void Call(View v) {
        TextView textView = findViewById(R.id.textView2);
        textView.setText("You Rated: " + ratingBar.getRating());

        // Extract feedback text from EditText
        TextView feedbackTextView = findViewById(R.id.feedbackEditText);
        String feedback = feedbackTextView.getText().toString();

        // Save the rating and feedback to Firebase
        if (!feedback.isEmpty()) {
            feedbackRef.push().setValue(feedback);
            // Provide feedback to the user
            // Show a toast message confirming the feedback submission
            Toast.makeText(this, "Feedback sent successfully", Toast.LENGTH_SHORT).show();
            // Clear the feedback EditText
            feedbackTextView.setText("");
        } else {
            // If feedback is empty, notify the user
            Toast.makeText(this, "Please provide feedback", Toast.LENGTH_SHORT).show();
        }
    }
}
