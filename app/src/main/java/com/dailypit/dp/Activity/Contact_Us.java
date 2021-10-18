package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.HelpRequestResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Contact_Us extends AppCompatActivity {
    ImageView back_img;
    EditText ed_message;
    TextView txt_request,ed_mobileNumber;
    ProgressBar mainProgressbar;
    String helpLineNumber,helpNumber;
    LinearLayout helpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        helpNumber = yourPrefrence.getData("HELPLINENUMBER");
        helpLineNumber = "tel:"+yourPrefrence.getData("HELPLINENUMBER");

        back_img = findViewById(R.id.back_img);
        ed_message = findViewById(R.id.ed_message);
        txt_request = findViewById(R.id.txt_request);

       ed_mobileNumber = findViewById(R.id.ed_mobileNumber);
       ed_mobileNumber.setText(helpNumber);

        ed_mobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(helpLineNumber));
                startActivity(intent);
            }
        });

        mainProgressbar = findViewById(R.id.mainProgressbar);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.INSTANCE.isNetworkAvailable(Contact_Us.this)){
                    String message = ed_message.getText().toString();
                    if(message.isEmpty()){
                        ed_message.setError("*Required");
                    } else {
                        getConttect(message);
                    }

                } else {
                    Helper.INSTANCE.Error(Contact_Us.this, getString(R.string.NOCONN));
                }
            }
        });

    }

    private void getConttect(String message) {

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("message", message);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<HelpRequestResponse> call = serviceInterface.helpRequest(map);
        call.enqueue(new Callback<HelpRequestResponse>() {
            @Override
            public void onResponse(Call<HelpRequestResponse> call, retrofit2.Response<HelpRequestResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Toast.makeText(Contact_Us.this, "Your request has been sent successfully", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(Contact_Us.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpRequestResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }
}