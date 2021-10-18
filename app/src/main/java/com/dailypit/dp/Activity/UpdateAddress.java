package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.StateAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Interface.StateListner;
import com.dailypit.dp.Model.Address.UpdateAddressResponse;
import com.dailypit.dp.Model.State.StateResponse;
import com.dailypit.dp.Model.State.StateResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.GPSTracker;
import com.dailypit.dp.Utils.GpsUtils;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateAddress extends AppCompatActivity implements StateListner {
    ImageView back_img,homeicon,Workicon,OtherIcon;
    TextView hometext,Worktext,OtherText,state;
    EditText houseno,address,landmark,pincode;
    CheckBox checkBoxForDefoultAddress;
    LinearLayout hometype,worktype,onthertype,updateAddressLayout,gps_locationLayout;
    ProgressBar mainProgressbar;
    String addressType, defauls,stateid;
    SpinnerDialog spinnerDialog;
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> stateID = new ArrayList<>();
    List<StateResponseData> stateList = new ArrayList<>();
    RecyclerView rv_simple_list;
    String address_id,address_type,home_number,fulladdress,land_mark,state_s,pin_code,defaults;
    private static LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        gps_locationLayout = findViewById(R.id.gps_locationLayout);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gps_locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(UpdateAddress.this)
                        .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                turngpspon();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {

//                                    Intent a = new Intent(Intent.ACTION_MAIN);
//                                    a.addCategory(Intent.CATEGORY_HOME);
//                                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(a);
//                                    finishAffinity();


                                    return;
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
               }
        });


        houseno = findViewById(R.id.houseno);
        address = findViewById(R.id.address);
        landmark = findViewById(R.id.landmark);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        hometype = findViewById(R.id.hometype);
        worktype = findViewById(R.id.worktype);
        onthertype = findViewById(R.id.onthertype);
        checkBoxForDefoultAddress = findViewById(R.id.checkBoxForDefoultAddress);
        updateAddressLayout = findViewById(R.id.updateAddressLayout);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        hometext = findViewById(R.id.hometext);
        Worktext = findViewById(R.id.Worktext);
        OtherText = findViewById(R.id.OtherText);
        homeicon = findViewById(R.id.homeicon);
        Workicon = findViewById(R.id.Workicon);
        OtherIcon = findViewById(R.id.OtherIcon);

        address_id = getIntent().getExtras().getString("addressId");
        address_type = getIntent().getExtras().getString("addressType");
        home_number = getIntent().getExtras().getString("homeNumber");
        fulladdress = getIntent().getExtras().getString("address");
        land_mark = getIntent().getExtras().getString("landMark");
        state_s = getIntent().getExtras().getString("state");
        pin_code = getIntent().getExtras().getString("pinCode");
        defaults = getIntent().getExtras().getString("default");

        houseno.setText(home_number);
        address.setText(fulladdress);
        landmark.setText(land_mark);
        state.setText(state_s);
        pincode.setText(pin_code);
        addressType = address_type;
        defauls = defaults;

        if (defauls.equals("1")){
            checkBoxForDefoultAddress.setChecked(true);
        } else {
            checkBoxForDefoultAddress.setChecked(false);
        }

        hometype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = "Home";

                hometype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.tab_background));
                worktype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));
                onthertype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));

                hometext.setTextColor(Color.WHITE);
                Worktext.setTextColor(Color.BLACK);
                OtherText.setTextColor(Color.BLACK);

                homeicon.setColorFilter(Color.argb(255, 255, 255, 255));
                Workicon.setColorFilter(Color.argb(255, 145, 145, 145));
                OtherIcon.setColorFilter(Color.argb(255, 145, 145, 145));

            }
        });

        worktype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = "Work";
                hometype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));
                worktype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.tab_background));
                onthertype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));
                hometext.setTextColor(Color.BLACK);
                Worktext.setTextColor(Color.WHITE);
                OtherText.setTextColor(Color.BLACK);

                homeicon.setColorFilter(Color.argb(255, 145, 145, 145));
                Workicon.setColorFilter(Color.argb(255, 255, 255, 255));
                OtherIcon.setColorFilter(Color.argb(255, 145, 145, 145));
            }
        });

        onthertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressType = "Other";
                hometype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));
                worktype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.grey_line_background));
                onthertype.setBackground(UpdateAddress.this.getResources().getDrawable(R.drawable.tab_background));

                hometext.setTextColor(Color.BLACK);
                Worktext.setTextColor(Color.BLACK);
                OtherText.setTextColor(Color.WHITE);

                homeicon.setColorFilter(Color.argb(255, 145, 145, 145));
                Workicon.setColorFilter(Color.argb(255, 145, 145, 145));
                OtherIcon.setColorFilter(Color.argb(255, 255, 255, 255));
            }
        });

        checkBoxForDefoultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxForDefoultAddress.isChecked()){
                    defauls = "1";
                } else {
                    defauls = "0";
                }
            }
        });

        updateAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (houseno.getText().toString().equalsIgnoreCase("")) {
                    houseno.setError("*Required");
                } else if(address.getText().toString().equalsIgnoreCase("")){
                    address.setError("*Required");
                } else if(state.getText().toString().equalsIgnoreCase("")){
                    state.setError("*Required");
                } else if(pincode.getText().toString().equalsIgnoreCase("")){
                    pincode.setError("*Required");
                } else if(pincode.getText().toString().length() != 6) {
                    pincode.setError("Required 6 Digit Number");
                } else {
                    if (Helper.INSTANCE.isNetworkAvailable(UpdateAddress.this)){
                        addNewAddress();
                    } else {
                        Helper.INSTANCE.Error(UpdateAddress.this, getString(R.string.NOCONN));
                    }
                }
            }
        });

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.INSTANCE.isNetworkAvailable(UpdateAddress.this)){
                    getState();
                } else {
                    Helper.INSTANCE.Error(UpdateAddress.this, getString(R.string.NOCONN));
                }

            }
        });
    }

    private void turngpspon() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            //  Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            redirectionScreen();
            mainProgressbar.setVisibility(View.VISIBLE);
        } else {
            if (!hasGPSDevice(this)) {
                //   Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
                //     Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                new GpsUtils(this).turnGPSOn(new GpsUtils.OnGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        if (isGPSEnable) {
                            redirectionScreen();
                            mainProgressbar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                redirectionScreen();
                mainProgressbar.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
        }
    }

    private void redirectionScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GPSTracker mGPS = new GPSTracker(getApplicationContext());
                Double lat,lng;
                mainProgressbar.setVisibility(View.GONE);
                if (mGPS.canGetLocation) {
                    mGPS.getLocation();
                    lat = mGPS.getLatitude();
                    lng = mGPS.getLongitude();
                    getCompleteAddressString(lat,lng);
                } else {
                    Toast.makeText(mGPS, "location not Available", Toast.LENGTH_SHORT).show();
                }

            }
        }, 1000);
    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


//
//    private void getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            Location locationGPS = getLastKnownLocation();
//            if (locationGPS != null) {
//                double lat = locationGPS.getLatitude();
//                double longi = locationGPS.getLongitude();
//                getCompleteAddressString(lat,longi);
//            } else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


//    private Location getLastKnownLocation() {
//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        List<String> providers = locationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return null;
//            }
//            Location l = locationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//        }
//        return bestLocation;
//    }


    private void getCompleteAddressString(Double LATITUDE, Double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            String fullAddress = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            String[] Amount = fullAddress.split(",");
            String add1 = Amount[1];
            String add2 = Amount[2];
            String add3 = Amount[3];

            address.setText(add1+", "+add2+", "+add3);
//            landmark.setText(knownName);
            state.setText(stateName);
            pincode.setText(postalCode);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }



    private void getState() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<StateResponse> call = serviceInterface.getState();
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, retrofit2.Response<StateResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    stateList = response.body().getData();
                    openStateDialog(stateList);
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(UpdateAddress.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
                Toast.makeText(UpdateAddress.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openStateDialog(List<StateResponseData> stateList) {
        final Dialog dialog = new Dialog(UpdateAddress.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.simple_list_data);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        rv_simple_list = dialog.findViewById(R.id.rv_simple_list);

        StateAdapter adapter = new StateAdapter(stateList, UpdateAddress.this,this,dialog);
        rv_simple_list.setHasFixedSize(true);
        rv_simple_list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_simple_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void addNewAddress() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");

        map.put("user_id", userId);
        map.put("address_id", address_id);
        map.put("type", addressType);
        map.put("hf_number", houseno.getText().toString());
        map.put("address", address.getText().toString());
        map.put("landmark", landmark.getText().toString());
        map.put("state", state.getText().toString());
        map.put("pincode", pincode.getText().toString());
        map.put("defaults", defauls);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UpdateAddressResponse> call = serviceInterface.updateAddress(map);
        call.enqueue(new Callback<UpdateAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateAddressResponse> call, retrofit2.Response<UpdateAddressResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        onBackPressed();
                    } else {
                        Toast.makeText(UpdateAddress.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(UpdateAddress.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
                Toast.makeText(UpdateAddress.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void stateClickListner(String name) {
        state.setText(name);
    }
}