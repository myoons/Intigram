package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void splashAnimation() {
        Animation animation_text = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_splash_textview);
        TextView splashTextView = findViewById(R.id.splashTextView);
        splashTextView.startAnimation(animation_text);

        Animation animation_image = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_splash_imageview);
        final ImageView splashImageView = findViewById(R.id.splashImageView);
        splashImageView.startAnimation(animation_image);
        animation_text.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashImageView.setElevation(20);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_splash_out_top,R.anim.anim_splash_in_down);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimation();
    }
}
