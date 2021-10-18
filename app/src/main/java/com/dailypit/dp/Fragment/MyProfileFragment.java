package com.dailypit.dp.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dailypit.dp.Activity.AboutUs;
import com.dailypit.dp.Activity.Address;
import com.dailypit.dp.Activity.Contact_Us;
import com.dailypit.dp.Activity.FavouriteServiceActivity;
import com.dailypit.dp.Activity.Howitwork;
import com.dailypit.dp.Activity.IntroPage;
import com.dailypit.dp.Activity.MemberShip;
import com.dailypit.dp.Activity.PrivacyAndPolicies;
import com.dailypit.dp.Activity.TermAndCondition;
import com.dailypit.dp.Activity.UpdateUserProfile;
import com.dailypit.dp.Activity.YourActivityMembershipPlanDetails;
import com.dailypit.dp.Interface.IOnBackPressed;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlan;
import com.dailypit.dp.Model.Policy.AppDetailsResponse;
import com.dailypit.dp.Model.Profile.UpdateUserImageProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.Model.logoutResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class MyProfileFragment extends Fragment {

    LinearLayout add_addressLayout, myFavouriteLayout, becomePartnerLayout,myMemberShipeLayout;
    TextView txt_userName, txt_userMobile, txt_Name, user_Mobile, txt_email, txt_editProfile, txt_defaultAddress,
            txt_termAndCondition, txt_privacyPoliciew, txt_About, txt_Contact, txt_logOut,txt_howitWork;
    ProgressBar mainProgressbar;
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    List<UserAddressListResponseData> userAddressListResponseData = new ArrayList<>();
    ImageView iv_profile_photo, iv_camera_icon;
    String convertedimage = "", selfiphoto = "";
    YourPreference yourPrefrence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        add_addressLayout = view.findViewById(R.id.add_addressLayout);
        txt_userName = view.findViewById(R.id.txt_userName);
        txt_userMobile = view.findViewById(R.id.txt_userMobile);
        txt_Name = view.findViewById(R.id.txt_Name);
        user_Mobile = view.findViewById(R.id.user_Mobile);
        txt_email = view.findViewById(R.id.txt_email);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        txt_editProfile = view.findViewById(R.id.txt_editProfile);
        txt_defaultAddress = view.findViewById(R.id.txt_defaultAddress);
        iv_profile_photo = view.findViewById(R.id.iv_profile_photo);
        iv_camera_icon = view.findViewById(R.id.iv_camera_icon);
        txt_termAndCondition = view.findViewById(R.id.txt_termAndCondition);
        txt_privacyPoliciew = view.findViewById(R.id.txt_privacyPoliciew);
        txt_About = view.findViewById(R.id.txt_About);
        txt_Contact = view.findViewById(R.id.txt_Contact);
        txt_logOut = view.findViewById(R.id.txt_logOut);
        txt_howitWork = view.findViewById(R.id.txt_howitWork);
        myFavouriteLayout = view.findViewById(R.id.myFavouriteLayout);
        becomePartnerLayout = view.findViewById(R.id.becomePartnerLayout);
        myMemberShipeLayout = view.findViewById(R.id.myMemberShipeLayout);

        txt_howitWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Howitwork.class);
                startActivity(intent);
            }
        });

        add_addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Address.class);
                startActivity(intent);
            }
        });

        myMemberShipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMemberShipPlan();
            }
        });

        becomePartnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.dailypit.dailypitpartner");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        myFavouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavouriteServiceActivity.class);
                startActivity(intent);
            }
        });

        txt_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogout();
            }
        });

        txt_termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermAndCondition.class);
                startActivity(intent);

            }
        });

        iv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selfiphoto != null) {
                    ShowalertSelfiPhoto();
                } else {
                    openbottomsheet();
                }
            }
        });

        txt_privacyPoliciew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyAndPolicies.class);
                startActivity(intent);
            }
        });

        txt_About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });

        txt_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Contact_Us.class);
                startActivity(intent);
            }
        });

        txt_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateUserProfile.class);
                intent.putExtra("userName", txt_Name.getText().toString());
                intent.putExtra("userMobile", user_Mobile.getText().toString());
                intent.putExtra("userEmail", txt_email.getText().toString());
                startActivity(intent);
            }
        });

        iv_camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbottomsheet();
            }
        });

        return view;

    }

    private void getLogout() {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", user_id);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<logoutResponse> call = serviceInterface.getLogout(map);
        call.enqueue(new Callback<logoutResponse>() {
            @Override
            public void onResponse(Call<logoutResponse> call, retrofit2.Response<logoutResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
                        yourPrefrence.saveData("USERID", "");
                        disconnectFromFacebook();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                        googleSignInClient.signOut();
                        Intent intent = new Intent(getContext(), IntroPage.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<logoutResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    private void ShowalertSelfiPhoto() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View convertView = inflater.inflate(R.layout.images, null);
        ImageView image = convertView.findViewById(R.id.image);

        Glide.with(this)
                .load(IMAGE + selfiphoto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(image);
        alertDialog.setView(convertView);
        alertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getContext())) {
            getUserProfile();
            getUserAddressList();
            getAppDetails();
        } else {
            Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
        }
    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserProfileResponse> call = serviceInterface.getUserProfile(map);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userProfileResponseData = response.body().getData();
                        txt_userName.setText(userProfileResponseData.get(0).getName());
                        txt_userMobile.setText(userProfileResponseData.get(0).getMobile());
                        txt_Name.setText(userProfileResponseData.get(0).getName());
                        user_Mobile.setText(userProfileResponseData.get(0).getMobile());
                        txt_email.setText(userProfileResponseData.get(0).getEmail());
                        setUaerImage(userProfileResponseData.get(0).getProfilePhoto());
                        selfiphoto = userProfileResponseData.get(0).getProfilePhoto();

                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void setUaerImage(String profilePhoto) {
        if (getActivity() != null) {
            String logintype = yourPrefrence.getData("logintype");
            if (logintype.equalsIgnoreCase("mobile")) {
                Glide.with(getActivity()).load(IMAGE + profilePhoto)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(iv_profile_photo);
            } else {
                Glide.with(getActivity()).load(profilePhoto)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(iv_profile_photo);
            }
        }
    }

    private void getUserAddressList() {
        yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        String logintype = yourPrefrence.getData("logintype");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserAddressListResponse> call = serviceInterface.getUserAddress(map);
        call.enqueue(new Callback<UserAddressListResponse>() {
            @Override
            public void onResponse(Call<UserAddressListResponse> call, retrofit2.Response<UserAddressListResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userAddressListResponseData = response.body().getData();
                        for (int i = 0; i < userAddressListResponseData.size(); i++) {
                            if (userAddressListResponseData.get(i).getDefaults().equals("1")) {
                                String hfNumber = userAddressListResponseData.get(i).getHfNumber();
                                String address = userAddressListResponseData.get(i).getAddress();
                                String ldMark = userAddressListResponseData.get(i).getLandmark();
                                String state = userAddressListResponseData.get(i).getState();
                                String pin = userAddressListResponseData.get(i).getPincode();
                                txt_defaultAddress.setText(hfNumber + ", " + address + ", " + ldMark + ", " + state + ", " + pin);
                            }
                        }
                    } else {
                        txt_defaultAddress.setText("Please Add Address");
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void getAppDetails() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<AppDetailsResponse> call = serviceInterface.getAppDetails();
        call.enqueue(new Callback<AppDetailsResponse>() {
            @Override
            public void onResponse(Call<AppDetailsResponse> call, retrofit2.Response<AppDetailsResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
                        yourPrefrence.saveData("TERMANDCONDITION", response.body().getTermCondition());
                        yourPrefrence.saveData("PRIVACYANDPOLICY", response.body().getPrivacyPolicy());
                        yourPrefrence.saveData("ABOUTUS", response.body().getAbout());
                        yourPrefrence.saveData("HELPLINENUMBER", response.body().getHelpline_number());
                        yourPrefrence.saveData("VIDEOPATH", response.body().getWorkProcedureVideo());
                        yourPrefrence.saveData("VIDEOCONTENT", response.body().getWorkProcedure());
                    }

                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppDetailsResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void openbottomsheet() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater inflater = getLayoutInflater();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                View convertView = inflater.inflate(R.layout.camera, null);
                LinearLayout camera = convertView.findViewById(R.id.camera);
                LinearLayout gallery = convertView.findViewById(R.id.gallery);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(intent, 105);
                        }
                        alertDialog.dismiss();
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 0);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(convertView);
                alertDialog.show();
            } else
                requestPermission();
        } catch (Exception e) {
            requestPermission();
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA, READ_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getContext(), getContext().getString(R.string.Permission_Granted), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.Permission_Denied), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //adhar upload
        if (data != null && requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
                    int width = 280;
                    int height = Math.round(width / aspectRatio);

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                   // convertedimage = ConvertBitmapToString(resizedBitmap);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);


                    convertedimage = ConvertBitmapToString(getResizedBitmap(bitmap, 700));

                    if (Helper.INSTANCE.isNetworkAvailable(getContext())) {
                        updateUserProfile(convertedimage);

                    } else {
                        Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        //take adhar
        if (data != null && requestCode == 105) {
            if (resultCode == RESULT_OK) {
                // Uri targetUri = data.getData();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
               // convertedimage = ConvertBitmapToString(bitmap);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                convertedimage = ConvertBitmapToString(getResizedBitmap(bitmap, 700));

                if (Helper.INSTANCE.isNetworkAvailable(getContext())) {
                    updateUserProfile(convertedimage);
                } else {
                    Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
                }
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String ConvertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }

    private void updateUserProfile(String convertedimage) {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("profile_photo", convertedimage);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UpdateUserImageProfileResponse> call = serviceInterface.updateUserImageProfile(map);
        call.enqueue(new Callback<UpdateUserImageProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateUserImageProfileResponse> call, retrofit2.Response<UpdateUserImageProfileResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Glide.with(getActivity()).load(IMAGE + response.body().getProfilePhoto()).into(iv_profile_photo);
                        selfiphoto = response.body().getProfilePhoto();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateUserImageProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void isMemberShipPlan() {
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CheckMemberShipPlan> call = serviceInterface.idMemberShipPlan(map);
        call.enqueue(new Callback<CheckMemberShipPlan>() {
            @Override
            public void onResponse(Call<CheckMemberShipPlan> call, retrofit2.Response<CheckMemberShipPlan> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Intent intent = new Intent(getActivity(), YourActivityMembershipPlanDetails.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MemberShip.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckMemberShipPlan> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }
}