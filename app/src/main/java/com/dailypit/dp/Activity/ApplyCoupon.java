package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.AddressAdapter;
import com.dailypit.dp.Adapter.ApplyCouponAdapter;
import com.dailypit.dp.Interface.ApplyCouponListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.Coupon.CouponResponse;
import com.dailypit.dp.Model.Coupon.CouponResponseData;
import com.dailypit.dp.Model.Coupon.HiddenCouponResponse;
import com.dailypit.dp.Model.Coupon.HiddenCouponResponseData;
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

public class ApplyCoupon extends AppCompatActivity implements ApplyCouponListner {

    RecyclerView couponRecyclerView;
    ProgressBar mainProgressbar;
    List<CouponResponseData> couponList = new ArrayList<>();
    List<HiddenCouponResponseData> hiddenCouponResponseData = new ArrayList<>();
    String hiddenCouponDiscription,subCategory;
    TextView txt_CouponStatus,txt_popup,txt_btnPopup,txt_hiddencouponDiscription,txt_manual_applyCoupon;
    ImageView back_img,img_check,img_cancel;
    LinearLayout wronge_promocodeLayout;
    EditText txt_couponCode;
    String stateName="";
    List<UserAddressListResponseData> userAddressListResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        couponRecyclerView = findViewById(R.id.couponRecyclerView);
        back_img = findViewById(R.id.back_img);
        img_check = findViewById(R.id.img_check);
        img_cancel = findViewById(R.id.img_cancel);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_couponCode = findViewById(R.id.txt_couponCode);
        txt_hiddencouponDiscription = findViewById(R.id.txt_hiddencouponDiscription);
        txt_manual_applyCoupon = findViewById(R.id.txt_manual_applyCoupon);
        subCategory = getIntent().getStringExtra("subCategory");

        getAddressList();

        txt_couponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.length() == 11){

                    if (stateName.equalsIgnoreCase("")||stateName.equalsIgnoreCase(null)){
                        Toast.makeText(ApplyCoupon.this, "Please Add Address ", Toast.LENGTH_SHORT).show();
                    } else {
                        applyHiddenCoupon(txt_couponCode.getText().toString(),stateName);
                    }
                }

                if(cs.length() != 11){
                    txt_hiddencouponDiscription.setText("This coupon code is not available!");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_couponCode.setText("");
                txt_hiddencouponDiscription.setText("");
                img_cancel.setVisibility(View.GONE);
            }
        });

    }

    private void applyHiddenCoupon(String code, String stateName) {

        HashMap<String, String> map = new HashMap<>();
        map.put("coupon_code", code);
        map.put("sub_category_id", subCategory);
        map.put("state_name", stateName);

        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<HiddenCouponResponse> call = serviceInterface.getHiddenCoupons(map);
        call.enqueue(new Callback<HiddenCouponResponse>() {
            @Override
            public void onResponse(Call<HiddenCouponResponse> call, retrofit2.Response<HiddenCouponResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_hiddencouponDiscription.setText(HtmlCompat.fromHtml("Coupon Available!", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        txt_hiddencouponDiscription.setTextColor(Color.parseColor("#059100"));
                        applyHiddenCouponWithCode(response.body().getData());
                    } else {
//                        img_cancel.setVisibility(View.VISIBLE);
//                        wronge_promocodeLayout.setVisibility(View.VISIBLE);
                        img_cancel.setVisibility(View.VISIBLE);
                        txt_hiddencouponDiscription.setText("This coupon code is not available! ");
                        txt_hiddencouponDiscription.setTextColor(Color.RED);

                    }
                }
            }

            @Override
            public void onFailure(Call<HiddenCouponResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void applyHiddenCouponWithCode(List<HiddenCouponResponseData> data) {

        txt_manual_applyCoupon.setVisibility(View.VISIBLE);

        txt_manual_applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ApplyCoupon.this, ProceedPay.class);
                    intent.putExtra("flag","1");
                    intent.putExtra("coupon_id",data.get(0).getId());
                    intent.putExtra("coupon_code",data.get(0).getCouponCode());
                    intent.putExtra("coupon_discription",data.get(0).getDescriptions());
                    intent.putExtra("coupon_discount",data.get(0).getDiscount());
                    intent.putExtra("discount_typee",data.get(0).getDiscountType());
                    startActivity(intent);
                    finish();
            }
        });
    }

    private void getCoupons(String stateName) {

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_category_id", subCategory);
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
                        couponList = response.body().getData();
                        ApplyCouponAdapter adapter = new ApplyCouponAdapter(couponList, ApplyCoupon.this,ApplyCoupon.this);
                        couponRecyclerView.setHasFixedSize(true);
                        couponRecyclerView.setLayoutManager(new GridLayoutManager(ApplyCoupon.this, 1));
                        couponRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {

//                        final Dialog dialog = new Dialog(ApplyCoupon.this);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setContentView(R.layout.custom_popup);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        txt_popup = dialog.findViewById(R.id.txt_popup);
//                        txt_btnPopup = dialog.findViewById(R.id.txt_btnPopup);
//                        txt_btnPopup.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(ApplyCoupon.this, ProceedPay.class);
//                                intent.putExtra("flag","2");
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//
//                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                        dialog.show();

                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(ApplyCoupon.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
                 }
        });
    }

    @Override
    public void serviceClick() {
        final Dialog dialog = new Dialog(ApplyCoupon.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coupon_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

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
                    Toast.makeText(ApplyCoupon.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
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
        if (Helper.INSTANCE.isNetworkAvailable(ApplyCoupon.this)){
            if (stateName.equalsIgnoreCase("")||stateName.equalsIgnoreCase(null)) {
                Toast.makeText(ApplyCoupon.this, "Please Add Address ", Toast.LENGTH_SHORT).show();
            } else {
                getCoupons(stateName);
            }
        } else {
            Helper.INSTANCE.Error(ApplyCoupon.this, getString(R.string.NOCONN));
        }
    }
}