package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Adapter.AddressAdapter;
import com.dailypit.dp.Interface.DeleteAddressListner;
import com.dailypit.dp.Interface.MakeDefaltAddress;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.DeleteAddressResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.MakeAddressDefaultResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Address extends AppCompatActivity implements DeleteAddressListner,MakeDefaltAddress {

    LinearLayout addNewAddressLayout;
    RecyclerView addressRecyclerView;
    ProgressBar mainProgressbar;
    ImageView back_img;
    List<UserAddressListResponseData> userAddressListResponseData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        addNewAddressLayout = findViewById(R.id.addNewAddressLayout);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Address.this, AddNewAddress.class);
                startActivity(intent);
            }
        });

        addressRecyclerView = findViewById(R.id.addressRecyclerView);

    }

    private void getUserAddressList() {
        userAddressListResponseData.clear();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
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
                        if (addressRecyclerView.getVisibility() == View.GONE) {
                            addressRecyclerView.setVisibility(View.VISIBLE);
                        }
                        userAddressListResponseData = response.body().getData();
                        AddressAdapter adapter = new AddressAdapter(userAddressListResponseData,Address.this,Address.this, Address.this);
                        addressRecyclerView.setHasFixedSize(true);
                        addressRecyclerView.setLayoutManager(new GridLayoutManager(Address.this, 1));
                        addressRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        addressRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(Address.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void deleteId(String id) {
        if (Helper.INSTANCE.isNetworkAvailable(Address.this)){
            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String user_id = yourPrefrence.getData("USERID");
            HashMap<String, String> map = new HashMap<>();
            mainProgressbar.setVisibility(View.VISIBLE);
            map.put("address_id", String.valueOf(id));
            map.put("user_id", user_id);

            ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
            Call<DeleteAddressResponse> call = serviceInterface.deleteUserAddress(map);
            call.enqueue(new Callback<DeleteAddressResponse>() {
                @Override
                public void onResponse(Call<DeleteAddressResponse> call, retrofit2.Response<DeleteAddressResponse> response) {
                    if (response.isSuccessful()) {
                        mainProgressbar.setVisibility(View.GONE);
                        String status = response.body().getStatus().toString();
                        if (status.equals("1")) {
                            getUserAddressList();
                        } else {
                            Toast.makeText(Address.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(Address.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeleteAddressResponse> call, Throwable t) {
                    Log.d("ff", t.toString());
                    mainProgressbar.setVisibility(View.GONE);
                    }
            });

        } else {
            Helper.INSTANCE.Error(Address.this, getString(R.string.NOCONN));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(Address.this)){
            getUserAddressList();
        } else {
            Helper.INSTANCE.Error(Address.this, getString(R.string.NOCONN));
        }
    }

    @Override
    public void getDefalt(String id) {
        if (Helper.INSTANCE.isNetworkAvailable(Address.this)){
            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String user_id = yourPrefrence.getData("USERID");
            HashMap<String, String> map = new HashMap<>();
            mainProgressbar.setVisibility(View.VISIBLE);
            map.put("user_id", user_id);
            map.put("address_id", id);
            ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
            Call<MakeAddressDefaultResponse> call = serviceInterface.makeDefaultAddress(map);
            call.enqueue(new Callback<MakeAddressDefaultResponse>() {
                @Override
                public void onResponse(Call<MakeAddressDefaultResponse> call, retrofit2.Response<MakeAddressDefaultResponse> response) {
                    if (response.isSuccessful()) {
                        mainProgressbar.setVisibility(View.GONE);
                        String status = response.body().getStatus().toString();
                        if (status.equals("1")) {
                            getUserAddressList();
                        } else {
                            Toast.makeText(Address.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(Address.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MakeAddressDefaultResponse> call, Throwable t) {
                    Log.d("ff", t.toString());
                    mainProgressbar.setVisibility(View.GONE);
                }
            });

        } else {
            Helper.INSTANCE.Error(Address.this, getString(R.string.NOCONN));
        }
    }
}