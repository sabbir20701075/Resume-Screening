package com.example.resumescreening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CardViewActivity extends AppCompatActivity {

    CardView calenderCard,reactionCard,settingCard,locationCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        calenderCard = findViewById(R.id.calenderCard);
        reactionCard = findViewById(R.id.reactionCard);
        settingCard = findViewById(R.id.settingCard);
        locationCard = findViewById(R.id.locationCard);
        calenderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, DatePicker.class);
                startActivity(intent);
            }
        });
        reactionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardViewActivity.this, Rating.class);
                startActivity(intent);
            }
        });
        settingCard.setOnClickListener(new View.OnClickListener() {
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

    }
}
