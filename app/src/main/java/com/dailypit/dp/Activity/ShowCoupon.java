package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.ShowCouponAdapter;
import com.dailypit.dp.Adapter.StateAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.Coupon.CouponResponse;
import com.dailypit.dp.Model.Coupon.CouponResponseData;
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

public class ShowCoupon extends AppCompatActivity {
    RecyclerView couponRecyclerView;
    List<CouponResponseData> couponList = new ArrayList<>();
    ProgressBar mainProgressbar;
    TextView txt_CouponStatus,txt_popup,txt_btnPopup;
    String serviceSubCategoryID;
    ImageView back_img;
    String stateName="";
    List<UserAddressListResponseData> userAddressListResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coupon);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        txt_CouponStatus = findViewById(R.id.txt_CouponStatus);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        couponRecyclerView = findViewById(R.id.couponRecyclerView);
        back_img = findViewById(R.id.back_img);

        getAddressList();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getCoupons(String stateName) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        serviceSubCategoryID = yourPrefrence.getData("subCategoryID");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_category_id", serviceSubCategoryID);
        map.put("state_name", stateName);

        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CouponResponse> call = serviceInterface.getCoupons(map);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, retrofit2.Response<CouponResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);

                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_CouponStatus.setVisibility(View.VISIBLE);
                        couponList = response.body().getData();
                        ShowCouponAdapter adapter = new ShowCouponAdapter(couponList,ShowCoupon.this);
                        couponRecyclerView.setHasFixedSize(true);
                        couponRecyclerView.setLayoutManager(new GridLayoutManager(ShowCoupon.this, 1));
                        couponRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        final Dialog dialog = new Dialog(ShowCoupon.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_popup);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        txt_popup = dialog.findViewById(R.id.txt_popup);
                        txt_btnPopup = dialog.findViewById(R.id.txt_btnPopup);
                        txt_btnPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });

                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    }

                 } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(ShowCoupon.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void getAddressList() {
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
                        userAddressListResponseData = response.body().getData();
                        for (int i = 0 ; i< userAddressListResponseData.size(); i++) {
                            if(response.body().getData().get(i).getDefaults().equals("1")){
                                stateName = response.body().getData().get(i).getState();
                                getCouponList(stateName);
                            }
                        }
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(ShowCoupon.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getCouponList(String stateName) {
        if (Helper.INSTANCE.isNetworkAvailable(ShowCoupon.this)){
            if (stateName.equalsIgnoreCase("")||stateName.equalsIgnoreCase(null)){
                Toast.makeText(this, "Please Add Address ", Toast.LENGTH_SHORT).show();
            } else {
                getCoupons(stateName);
            }
        } else {
            Helper.INSTANCE.Error(ShowCoupon.this, getString(R.string.NOCONN));
        }
    }

}