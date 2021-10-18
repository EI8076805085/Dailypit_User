package com.dailypit.dp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.SignUpResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ImageView img_back;
    LinearLayout txt_sendOTP;
    TextView txt_alreadyHaveAccount, txt_login;
    EditText ed_mobileNumber, ed_userName;
    ProgressBar mainProgressbar;
    Integer RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    RelativeLayout google_loginRelativeLayout, facebook_loginRelativeLayout;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String socialid, socialname, email = "", photo, referrerUrl;
    InstallReferrerClient referrerClient;
    ReferrerDetails response;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        img_back = findViewById(R.id.img_back);
        ed_mobileNumber = findViewById(R.id.ed_mobileNumber);
        ed_userName = findViewById(R.id.ed_userName);
        txt_sendOTP = findViewById(R.id.txt_sendOTP);
        txt_alreadyHaveAccount = findViewById(R.id.txt_alreadyHaveAccount);
        txt_login = findViewById(R.id.txt_login);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        signInButton = findViewById(R.id.sign_in_button);
        loginButton = findViewById(R.id.login_button);
        google_loginRelativeLayout = findViewById(R.id.google_loginRelativeLayout);
        facebook_loginRelativeLayout = findViewById(R.id.facebook_loginRelativeLayout);

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.

                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();

                            referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

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


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        txt_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_userName.getText().toString().equalsIgnoreCase("")) {
                    ed_userName.setError("*Required");
                } else if (ed_mobileNumber.getText().toString().equalsIgnoreCase("")) {
                    ed_mobileNumber.setError("*Required");
                } else if (ed_mobileNumber.getText().toString().length() != 10) {
                    ed_mobileNumber.setError("Required 10 Digit Number");
                } else {
                    if (Helper.INSTANCE.isNetworkAvailable(SignUp.this)) {
                        doSignUp();
                    } else {
                        Helper.INSTANCE.Error(SignUp.this, getString(R.string.NOCONN));
                    }
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGooogle();
            }
        });

        google_loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGooogle();
            }
        });

        facebook_loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());

                }

            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("res", object.toString());
                        Log.d("res_obj", response.toString());
                        try {

                            socialid = object.getString("id");
                            try {
                                URL profile_pic = new URL("https://graph.facebook.com/" + socialid + "/picture?width=200&height=150");
                                Log.i("profile_pic", profile_pic + "");

                                String f_name = object.getString("first_name");
                                String l_name = object.getString("last_name");
                                socialname = f_name + " " + l_name;

                                try {
                                    email = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                photo = String.valueOf(profile_pic);
                                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                                yourPrefrence.saveData("name", socialname);
                                yourPrefrence.saveData("email", email);
                                yourPrefrence.saveData("logintype", "facebook");
                                yourPrefrence.saveData("profile", "" + photo);

                                signUpUrl("facebook", socialname, email, photo, socialid);

                                if (email == null) {

                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(SignUp.this, "Cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Toast.makeText(SignUp.this, "Somthing went wronge" + exception, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void signInWithGooogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, (RC_SIGN_IN));

    }

    private void doSignUp() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("login_type", "mobile");
        map.put("mobile", ed_mobileNumber.getText().toString());

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<SignUpResponse> call = serviceInterface.doSignup(map);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Intent intent = new Intent(SignUp.this, OTPScreen.class);
                        intent.putExtra("userName", ed_userName.getText().toString());
                        intent.putExtra("userNumber", ed_mobileNumber.getText().toString());
                        intent.putExtra("referralBy", referrerUrl);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + connectionResult, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignUp.this);
            if (acct != null) {
                String socialname = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String email = acct.getEmail();
                String socialid = acct.getId();
                String photo = String.valueOf(acct.getPhotoUrl());

                signUpUrl("google", socialname, email, photo, socialid);

            } else {
                Toast.makeText(getApplicationContext(), "Somthing went wronge", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void doGoogleSignIn(String google, String personName, String personEmail, String personPhoto, String personId) {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("login_type", google);
        map.put("name", personName);
        map.put("email", personEmail);
        map.put("profile_photo", personPhoto);
        map.put("social_id", personId);
        map.put("device_id", token);
        map.put("referral_by", referrerUrl);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<SignUpResponse> call = serviceInterface.doSignup(map);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("logintype", "google");
                        yourPrefrence.saveData("REFERALCODE", response.body().getReferralCode());
                        yourPrefrence.saveData("USERID", response.body().getId());
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String socialid = object.getString("id");
                            String photo = "https://graph.facebook.com/" + socialid + "/picture?type=normal";
                            String socialname = first_name + last_name;

                            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                            yourPrefrence.saveData("name", socialname);
                            yourPrefrence.saveData("email", email);
                            yourPrefrence.saveData("logintype", "facebook");
                            yourPrefrence.saveData("profile", "" + photo);

                            signUpUrl("facebook", socialname, email, photo, socialid);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void signWithFacebook(String facebook, String name, String email, String image_url, String id) {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("login_type", facebook);
        map.put("name", name);
        map.put("email", email);
        map.put("profile_photo", image_url);
        map.put("social_id", id);
        map.put("device_id", token);
        map.put("referral_by", referrerUrl);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<SignUpResponse> call = serviceInterface.doSignup(map);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    Log.d("Response", String.valueOf(response.body()));
                    if (status.equals("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("logintype", "facebook");
                        yourPrefrence.saveData("REFERALCODE", response.body().getReferralCode());
                        yourPrefrence.saveData("USERID", response.body().getId());
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void signUpUrl(String facebook, String name, String email, String image_url, String id) {

        if (token != null && !token.equalsIgnoreCase("")) {
            if (facebook.equals("facebook")) {
                signWithFacebook("facebook", name, email, image_url, id);
            } else {
                doGoogleSignIn("google", name, email, image_url, id);
            }

        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                signUpUrl(facebook, name, email, image_url, id);
                            } else {
                                token = "";
                            }
                        }
                    });
        }
    }


}