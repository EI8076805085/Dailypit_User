package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.dailypit.dp.MainActivity;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity implements Animation.AnimationListener{

    Animation animMoveToTop;
    ImageView imageIcon;
    AppEventsLogger logger;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        logger = AppEventsLogger.newLogger(this);

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        logAchieveLevelEvent("Test");

       // printHashKey();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.dailypit.dp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        imageIcon = findViewById(R.id.icon);
        animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.transition.move);
        animMoveToTop.setAnimationListener(Splash.this);
        imageIcon.startAnimation(animMoveToTop);
        redirectionScreen();

    }

    private void redirectionScreen() {
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2 * 1000);
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String id = yourPrefrence.getData("USERID");

                    if (id.equalsIgnoreCase("")||id.equalsIgnoreCase(null))
                    {
                        startActivity(new Intent(getApplicationContext(), IntroPage.class));
                        finish();
                    }
                    else {
                       // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void logAchieveLevelEvent (String level) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, level);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACHIEVED_LEVEL, params);

    }

}


