package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Adapter.NotificationAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.Notification.NotificationResponse;
import com.dailypit.dp.Model.Notification.NotificationResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Notification extends AppCompatActivity {
    RecyclerView notification_recyclerView;
    ImageView back_img,img_dataNotFound;
    ProgressBar mainProgressbar;
    List<NotificationResponseData> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        notification_recyclerView = findViewById(R.id.notification_recyclerView);
        back_img = findViewById(R.id.back_img);
        img_dataNotFound = findViewById(R.id.img_dataNotFound);
        mainProgressbar = findViewById(R.id.mainProgressbar);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Helper.INSTANCE.isNetworkAvailable(Notification.this)){
            getNotification();
        } else {
            Helper.INSTANCE.Error(Notification.this, getString(R.string.NOCONN));
        }

    }

    private void getNotification() {
        notificationList.clear();
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<NotificationResponse> call = serviceInterface.getNotification(map);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, retrofit2.Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        notificationList = response.body().getData();
                       // Collections.reverse(notificationList);
                        NotificationAdapter adapter = new NotificationAdapter(notificationList,Notification.this);
                        notification_recyclerView.setHasFixedSize(true);
                        notification_recyclerView.setLayoutManager(new GridLayoutManager(Notification.this, 1));
                        notification_recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        img_dataNotFound.setVisibility(View.VISIBLE);
                    }
                  } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(Notification.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }
}