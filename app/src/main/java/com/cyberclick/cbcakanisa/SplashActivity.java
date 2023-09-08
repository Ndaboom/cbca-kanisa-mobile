package com.cyberclick.cbcakanisa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    ImageView vlogo;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        vlogo = findViewById(R.id.icon);

        vlogo.animate().alpha(3000).setDuration(0);

        handler = new Handler();

        handler.postDelayed(() -> {
            Intent secondForm = new Intent(SplashActivity.this,HomeActivity.class);
            startActivity(secondForm);
            finish();
        },4000);
    }
}