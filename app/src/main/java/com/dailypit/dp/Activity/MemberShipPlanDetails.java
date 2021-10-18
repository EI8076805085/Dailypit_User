package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.AddressAdapter;
import com.dailypit.dp.Adapter.MemberShipSlider;
import com.dailypit.dp.Adapter.PlanDetailsAdapter;
import com.dailypit.dp.Fragment.MemberShipPlanFragment;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.MemberShip.AddMemberResponse;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;
import com.github.demono.AutoScrollViewPager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MemberShipPlanDetails extends AppCompatActivity implements PaymentResultListener {

    ImageView back_img;
    RecyclerView membershipPlayRecyclerView;
    TextView txt_validityMonth,txtPlanCharge;
    ProgressBar mainProgressbar;
    List<PlanDetailsData> planDetailsData = new ArrayList<>();
    List<BannerResponseData> bannerResponses = new ArrayList<>();
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    AutoScrollViewPager viewPager;
    String planCharg,planId;
    String userName, userEmail, userMobile;
    LinearLayout getMembershipLayout;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_plan_details);

        viewPager = findViewById(R.id.viewPager);
        back_img = findViewById(R.id.back_img);
        membershipPlayRecyclerView = findViewById(R.id.membershipPlayRecyclerView);
        txt_validityMonth = findViewById(R.id.txt_validityMonth);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        txtPlanCharge = findViewById(R.id.txtPlanCharge);
        getMembershipLayout = findViewById(R.id.getMembershipLayout);

        Intent intent = getIntent();
        String validity = intent.getStringExtra("validity");
        planCharg = intent.getStringExtra("planCharg");
        planId = intent.getStringExtra("planId");

        txt_validityMonth.setText(validity+" Membership Plan");
        txtPlanCharge.setText("Buy for just Rs "+planCharg);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getPlanDetails();
        getBottomBanner();
        getUserProfile();

        getMembershipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startpayment(planCharg);
            }
        });

    }

    public void startpayment(String money ) {
        Checkout checkout = new Checkout();
        final Activity activity = this;
        price = Integer.parseInt(String.valueOf(money));

        try {
            JSONObject options = new JSONObject();
            options.put("name", userName);
            options.put("currency", "INR");
            options.put("prefill.email", userEmail);
            options.put("prefill.contact", userMobile);
            options.put("amount", price * 100);
            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }
    }


    private void getBottomBanner() {
        bannerResponses.clear();
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<BannerResponse> call = serviceInterface.getBanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, retrofit2.Response<BannerResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    bannerResponses = response.body().getData();
                    if (status.equals("1")) {
                        initBottombanner(bannerResponses);
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

    private void initBottombanner(List<BannerResponseData> bottomBannerResponses) {
        viewPager.setAdapter(new MemberShipSlider(getApplicationContext(), bottomBannerResponses));
        viewPager.startAutoScroll();
    }

    private void getPlanDetails() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PlanDetailsResponse> call = serviceInterface.getPlanDiscount();
        call.enqueue(new Callback<PlanDetailsResponse>() {
            @Override
            public void onResponse(Call<PlanDetailsResponse> call, retrofit2.Response<PlanDetailsResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        planDetailsData = response.body().getData();
                        bindData(planDetailsData);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlanDetailsResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindData(List<PlanDetailsData> planDetailsData) {
        PlanDetailsAdapter adapter = new PlanDetailsAdapter(planDetailsData,getApplicationContext());
        membershipPlayRecyclerView.setHasFixedSize(true);
        membershipPlayRecyclerView.setLayoutManager(new GridLayoutManager(MemberShipPlanDetails.this, 1));
        membershipPlayRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (razorpayPaymentID.isEmpty()) {
            Toast.makeText(MemberShipPlanDetails.this, "Transaction failed", Toast.LENGTH_SHORT).show();
        } else {
            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String userId = yourPrefrence.getData("USERID");
            paymentsuccess(razorpayPaymentID,userId);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(MemberShipPlanDetails.this);
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserProfileResponse> call = serviceInterface.getUserProfile(map);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userProfileResponseData = response.body().getData();
                        for (int i = 0; i < userProfileResponseData.size(); i++) {
                            userEmail = userProfileResponseData.get(i).getEmail();
                            userMobile = userProfileResponseData.get(i).getMobile();
                            userName = userProfileResponseData.get(i).getName();
                        }
                    }
                } else {
                    Toast.makeText(MemberShipPlanDetails.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

    public  void paymentsuccess(String razorpayPaymentID, String userId) {
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("plan_id", planId);
        map.put("amount", planCharg);
        map.put("transaction_id", razorpayPaymentID);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<AddMemberResponse> call = serviceInterface.addMemberShip(map);
        call.enqueue(new Callback<AddMemberResponse>() {
            @Override
            public void onResponse(Call<AddMemberResponse> call, retrofit2.Response<AddMemberResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Intent intent = new Intent(MemberShipPlanDetails.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddMemberResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }


}