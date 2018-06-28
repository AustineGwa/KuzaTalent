package com.dannextech.apps.kuzatalent;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView myImageView= (ImageView) findViewById(R.id.imageView);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        final TextView tvLoading = (TextView) findViewById(R.id.tvLoading);

        //Adding a fade in effect to the image view
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        myImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                tvLoading.setVisibility(View.VISIBLE);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        }, 6000);
    }
}
