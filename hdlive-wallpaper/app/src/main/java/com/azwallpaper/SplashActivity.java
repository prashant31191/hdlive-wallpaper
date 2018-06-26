package com.azwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utils.StaticData;
import com.utils.StringUtils;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int KEEP_TIME = 3000;
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
            TextView tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setTypeface(App.getFont_Bold());

            if (StringUtils.isValidString(App.sharePrefrences.getStringPref(StaticData.key_splash_bg))) {

            } else {
                App.sharePrefrences.setPref(StaticData.key_splash_bg, App.splash_url);
            }


            Glide.with(SplashActivity.this)
                    .load(StringUtils.setString(App.sharePrefrences.getStringPref(StaticData.key_splash_bg)))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivSplash);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "----------startActivityIntent----------");
                    //Intent intent = new Intent(SplashActivity.this, CategoryExpanActivity.class);
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, KEEP_TIME);

            Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1); //To Enable
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
