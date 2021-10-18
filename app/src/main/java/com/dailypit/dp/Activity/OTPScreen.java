package com.dailypit.dp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.OTPResponse;
import com.dailypit.dp.Model.ResendOTPResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class OTPScreen extends AppCompatActivity {

    TextView txt_timeCounter, txt_resendOTP, txt_mobileDisc;
    ImageView img_back;
    EditText ed1, ed2, ed3, ed4;
    String number, otp, name, referralBy;
    ProgressBar mainProgressbar;
    RelativeLayout otp_verifyRelativeLayout;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        otp_verifyRelativeLayout = findViewById(R.id.otp_verifyRelativeLayout);

        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_timeCounter = findViewById(R.id.txt_timeCounter);
        txt_resendOTP = findViewById(R.id.txt_resendOTP);
        txt_mobileDisc = findViewById(R.id.txt_mobileDisc);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_timeCounter.setText(millisUntilFinished / 1000 + " Sec");
            }

            public void onFinish() {
                txt_resendOTP.setVisibility(View.VISIBLE);
            }

        }.start();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                        } else {
                            token = "";
                        }
                    }
                });

        number = getIntent().getExtras().getString("userNumber");
        name = getIntent().getExtras().getString("userName");
        referralBy = getIntent().getExtras().getString("referralBy");
        txt_mobileDisc.setText("Enter code that send to " + number);

       // Otpverify();

        ed1.addTextChangedListener(new GenericTextWatcher(ed2, ed1));
        ed2.addTextChangedListener(new GenericTextWatcher(ed3, ed1));
        ed3.addTextChangedListener(new GenericTextWatcher(ed4, ed2));
        ed4.addTextChangedListener(new GenericTextWatcher(ed4, ed3));

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ed1.setBackground(ContextCompat.getDrawable(OTPScreen.this, R.drawable.blackcircle));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ed2.setBackground(ContextCompat.getDrawable(OTPScreen.this, R.drawable.blackcircle));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ed3.setBackground(ContextCompat.getDrawable(OTPScreen.this, R.drawable.blackcircle));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ed4.setBackground(ContextCompat.getDrawable(OTPScreen.this, R.drawable.blackcircle));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.INSTANCE.isNetworkAvailable(OTPScreen.this)) {
                    resendOTP();
                } else {
                    Helper.INSTANCE.Error(OTPScreen.this, getString(R.string.NOCONN));
                }
            }
        });

        otp_verifyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithOTP();
            }
        });

    }


    private void resendOTP() {
        getResendOTP();
    }

    private void getResendOTP() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("mobile", number);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<ResendOTPResponse> call = serviceInterface.getResendOTP(map);
        call.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(Call<ResendOTPResponse> call, retrofit2.Response<ResendOTPResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_resendOTP.setVisibility(View.GONE);
                        new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                txt_timeCounter.setText(millisUntilFinished / 1000 + " Sec");
                            }

                            public void onFinish() {
                                txt_resendOTP.setVisibility(View.VISIBLE);
                            }
                        }.start();
                    } else {
                        Toast.makeText(OTPScreen.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(OTPScreen.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    public class GenericTextWatcher implements TextWatcher {
        private EditText etPrev;
        private EditText etNext;

        public GenericTextWatcher(EditText etNext, EditText etPrev) {
            this.etPrev = etPrev;
            this.etNext = etNext;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (text.length() == 1)
                etNext.requestFocus();
            else if (text.length() == 0)
                etPrev.requestFocus();

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }
    }

    public void verify() {
        otp = ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString();
        if (otp.length() != 4) {
            Toast.makeText(this, "Required 4 Digit OTP", Toast.LENGTH_SHORT).show();
        } else {
            signUpUrl();
        }

    }

    private void signUpUrl() {
        if (Helper.INSTANCE.isNetworkAvailable(OTPScreen.this)) {
            Otpverify();
        } else {
            Helper.INSTANCE.Error(OTPScreen.this, getString(R.string.NOCONN));
        }
    }

    private void Otpverify() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("name", name);
        map.put("mobile", number);
        map.put("otp", otp);
        map.put("device_id", token);
        map.put("referral_by", referralBy);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<OTPResponse> call = serviceInterface.getOTP(map);
        call.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, retrofit2.Response<OTPResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("logintype", "mobile");
                        yourPrefrence.saveData("USERID", response.body().getId());
                        yourPrefrence.saveData("REFERALCODE", response.body().getReferralCode());
                        Intent intent = new Intent(OTPScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OTPScreen.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(OTPScreen.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void signUpWithOTP() {
        if (token != null && !token.equalsIgnoreCase("")) {
            verify();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                signUpWithOTP();
                            } else {
                                token = "";
                            }
                        }
                    });
        }
    }

}