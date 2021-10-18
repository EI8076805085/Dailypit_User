package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

public class PrivacyAndPolicies extends AppCompatActivity {

    ImageView back_img;
    TextView txt_term,txt_Details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_policies);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        back_img = findViewById(R.id.back_img);

        txt_term = findViewById(R.id.txt_term);
        txt_Details = findViewById(R.id.txt_Details);
        txt_Details.setText("Dailypit Privacy - Policy");

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String term = yourPrefrence.getData("PRIVACYANDPOLICY");
        txt_term.setText(HtmlCompat.fromHtml(term, HtmlCompat.FROM_HTML_MODE_LEGACY));


    }
}