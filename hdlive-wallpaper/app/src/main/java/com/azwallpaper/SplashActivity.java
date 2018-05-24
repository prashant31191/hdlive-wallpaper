package com.azwallpaper;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int KEEP_TIME = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getSupportActionBar().hide();
        try {

            setContentView(R.layout.activity_splash);
            handler = new Handler();

            ImageView ivSplash = findViewById(R.id.ivSplash);


            Glide.with(SplashActivity.this)
                    .load(App.splash_url)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivSplash);



            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "----------startActivityIntent----------");
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, KEEP_TIME);

            Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1); //To Enable
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
