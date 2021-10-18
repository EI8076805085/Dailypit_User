package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Help extends AppCompatActivity {

    ImageView back_img;
    EditText ed_message;
    TextView txt_request,txt_requestAndCall;
    ProgressBar mainProgressbar;
    Button btn_submit;
    String Orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        back_img = findViewById(R.id.back_img);
        ed_message = findViewById(R.id.ed_message);
        txt_request = findViewById(R.id.txt_request);
        txt_requestAndCall = findViewById(R.id.txt_requestAndCall);
        mainProgressbar = findViewById(R.id.mainProgressbar);

        Orderid = getIntent().getStringExtra("Order_id");

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.INSTANCE.isNetworkAvailable(Help.this)){
                    String message = ed_message.getText().toString();
                    if(message.isEmpty()){
                        ed_message.setError("*Required");
                    } else {
                        getHelp(message);
                    }
                } else {
                    Helper.INSTANCE.Error(Help.this, getString(R.string.NOCONN));
                }
            }
        });

        txt_requestAndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.INSTANCE.isNetworkAvailable(Help.this)){
                    String message = "Request a call";
                    getHelp(message);
                } else {
                    Helper.INSTANCE.Error(Help.this, getString(R.string.NOCONN));
                }
            }
        });
    }

    private void getHelp(String message) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("message", message);
        map.put("order_id", Orderid);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<HelpRequestResponse> call = serviceInterface.helpOrderRequest(map);
        call.enqueue(new Callback<HelpRequestResponse>() {
            @Override
            public void onResponse(Call<HelpRequestResponse> call, retrofit2.Response<HelpRequestResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        final Dialog dialog = new Dialog(Help.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.location_dialog);

                        btn_submit = dialog.findViewById(R.id.btn_submit);
                        btn_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                                dialog.dismiss();
                            }
                        });

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(Help.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
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