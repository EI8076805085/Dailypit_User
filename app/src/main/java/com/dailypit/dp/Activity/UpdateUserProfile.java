package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Profile.UserUpdateProfile;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdateUserProfile extends AppCompatActivity {

    ImageView back_img;
    EditText txt_editName,txt_editMobile,txt_editEmail;
    LinearLayout updateProfileLayout;
    String user_name,user_mobile,user_email;
    ProgressBar mainProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        user_name = getIntent().getExtras().getString("userName");
        user_mobile = getIntent().getExtras().getString("userMobile");
        user_email = getIntent().getExtras().getString("userEmail");

        txt_editName = findViewById(R.id.txt_editName);
        txt_editMobile = findViewById(R.id.txt_editMobile);
        txt_editEmail = findViewById(R.id.txt_editEmail);
        updateProfileLayout = findViewById(R.id.updateProfileLayout);
        mainProgressbar = findViewById(R.id.mainProgressbar);

        txt_editName.setText(user_name);
        txt_editMobile.setText(user_mobile);
        txt_editEmail.setText(user_email);

        if (user_mobile.equals("")||user_mobile.equalsIgnoreCase("null"))
        {
            txt_editMobile.setEnabled(true);
        }

        if (user_email.equals("")||user_email.equalsIgnoreCase("null"))
        {
            txt_editEmail.setEnabled(true);
        }

        updateProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_editName.getText().toString().equalsIgnoreCase("")) {
                    txt_editName.setError("*Required");
                } else if(txt_editMobile.getText().toString().equalsIgnoreCase("")){
                    txt_editMobile.setError("*Required");
                } else if(txt_editMobile.getText().toString().length() != 10) {
                    txt_editMobile.setError("Required 10 Digit Number");
                } else if(txt_editEmail.getText().toString().equalsIgnoreCase("")){
                    txt_editEmail.setError("*Required");
                } else if (!txt_editEmail.getText().toString().matches(emailPattern)) {
                    txt_editEmail.setError("Invalid email Id");
                } else {
                    if (Helper.INSTANCE.isNetworkAvailable(UpdateUserProfile.this)){
                        updateUserProfile();
                    } else {
                        Helper.INSTANCE.Error(UpdateUserProfile.this, getString(R.string.NOCONN));
                    }
                }
            }
        });

    }

    private void updateUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        map.put("name", txt_editName.getText().toString());
        map.put("mobile", txt_editMobile.getText().toString());
        map.put("email", txt_editEmail.getText().toString());

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserUpdateProfile> call = serviceInterface.updateUserProfile(map);
        call.enqueue(new Callback<UserUpdateProfile>() {
            @Override
            public void onResponse(Call<UserUpdateProfile> call, retrofit2.Response<UserUpdateProfile> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        onBackPressed();
                    } else {
                        Toast.makeText(UpdateUserProfile.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(UpdateUserProfile.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserUpdateProfile> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }


}