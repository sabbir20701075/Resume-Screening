package com.example.resumescreening;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /* ***** Create Thread that will sleep for 5 seconds*** */
        Thread thread = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    // After 5 seconds redirect to another intent
                    Intent intent=new Intent(SplashActivity.this,LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}