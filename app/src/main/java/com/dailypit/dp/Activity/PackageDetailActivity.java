package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.MemberShipSlider;
import com.dailypit.dp.Adapter.MembershipServicePlanAdapter;
import com.dailypit.dp.Adapter.PackageAdapter;
import com.dailypit.dp.Adapter.PackagePlanListAdapter;
import com.dailypit.dp.Adapter.PackageServiceCountAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.MemberShip.AddMemberResponse;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponse;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponseData;
import com.dailypit.dp.Model.Packageg.PackageResponse;
import com.dailypit.dp.Model.Packageg.PackageResponseData;
import com.dailypit.dp.Model.Packageg.PackageServicesList;
import com.dailypit.dp.Model.Packageg.PurchasePackageResponse;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PackageDetailActivity extends AppCompatActivity implements PaymentResultListener {

    AutoScrollViewPager viewPager;
    List<BannerResponseData> bannerResponses = new ArrayList<>();
    List<PackagePlanlistResponseData> packagePlanlistResponseData = new ArrayList<>();
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    List<String> contentList = new ArrayList<String>();
    ProgressBar mainProgressbar;
    RecyclerView packagePlayListRecyclerView,packagePlayRecyclerView;
    ImageView back_img;
    int price;
    String package_id,packageAmount = "";
    TextView txt_packageName,txtPackageCharge;
    LinearLayout packageLayout,getMembershipLayout;
    String userName, userEmail, userMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        viewPager = findViewById(R.id.viewPager);
        packagePlayListRecyclerView = findViewById(R.id.packagePlayListRecyclerView);
        packagePlayRecyclerView = findViewById(R.id.packagePlayRecyclerView);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        back_img = findViewById(R.id.back_img);
        txt_packageName = findViewById(R.id.txt_packageName);
        txtPackageCharge = findViewById(R.id.txtPackageCharge);
        packageLayout = findViewById(R.id.packageLayout);
        getMembershipLayout = findViewById(R.id.getMembershipLayout);

        getBanner();
        getUserProfile();

        Intent intent = getIntent();
        package_id = intent.getStringExtra("Package_id");
        getPackageDetails(package_id);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getMembershipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String user_id = yourPrefrence.getData("USERID");
                if(packageAmount.equalsIgnoreCase("") || packageAmount.equalsIgnoreCase("0")){
                    Toast.makeText(PackageDetailActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                } else if(user_id.equalsIgnoreCase("") || user_id.equalsIgnoreCase("null")){
                    Toast.makeText(PackageDetailActivity.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                } else {
                    startpayment(packageAmount);
                }
            }
        });

    }

    private void startpayment(String packageAmount) {
        Checkout checkout = new Checkout();
        final Activity activity = this;
        price = Integer.parseInt(String.valueOf(packageAmount));

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

    private void getPackageDetails(String package_id) {
        contentList.clear();
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("package_id", package_id);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PackagePlanlistResponse> call = serviceInterface.getPackageDetails(map);
        call.enqueue(new Callback<PackagePlanlistResponse>() {
            @Override
            public void onResponse(Call<PackagePlanlistResponse> call, retrofit2.Response<PackagePlanlistResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_packageName.setText(response.body().getPackageName() +" Package");
                        txtPackageCharge.setText("Buy for just "+"\u20B9"+" "+response.body().getPackageAmount() +" only!");
                        packageAmount = response.body().getPackageAmount();
                        packageLayout.setBackgroundColor(Color.parseColor(response.body().getColorCode()));

                        String content = response.body().getKeyPoint();
                        List<String> itemNameParentList = Arrays.asList(content);
                        for (int i = 0; i < itemNameParentList.size(); i++) {
                            List<String> itemNameList  = Arrays.asList(itemNameParentList.get(i).split(","));
                            for (int j = 0; j < itemNameList.size(); j++) {
                                contentList.add(itemNameList.get(j));
                            }
                        }

                        PackagePlanListAdapter adapter = new PackagePlanListAdapter(contentList, getApplicationContext());
                        packagePlayListRecyclerView.setHasFixedSize(true);
                        packagePlayListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        packagePlayListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        packagePlanlistResponseData = response.body().getServiceDetails();
                        bindPackageg(packagePlanlistResponseData);

                    }
                }
            }

            @Override
            public void onFailure(Call<PackagePlanlistResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    private void bindPackageg(List<PackagePlanlistResponseData> packagePlanlistResponseData){

        PackageServiceCountAdapter adapter = new PackageServiceCountAdapter(packagePlanlistResponseData,getApplicationContext());
        packagePlayRecyclerView.setHasFixedSize(true);
        packagePlayRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        packagePlayRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getBanner() {
        bannerResponses.clear();
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<BannerResponse> call = serviceInterface.getBanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, retrofit2.Response<BannerResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
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
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void initBottombanner(List<BannerResponseData> bottomBannerResponses) {
        viewPager.setAdapter(new MemberShipSlider(getApplicationContext(), bottomBannerResponses));
        viewPager.startAutoScroll();
    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(PackageDetailActivity.this);
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
                    Toast.makeText(PackageDetailActivity.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (razorpayPaymentID.isEmpty()) {
            Toast.makeText(PackageDetailActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
        } else {
            paymentsuccess(razorpayPaymentID);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
    }

    private void paymentsuccess(String razorpayPaymentID) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("USERID");
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("package_id", package_id);
        map.put("amount", packageAmount);
        map.put("transaction_id", razorpayPaymentID);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PurchasePackageResponse> call = serviceInterface.getPackagePlan(map);
        call.enqueue(new Callback<PurchasePackageResponse>() {
            @Override
            public void onResponse(Call<PurchasePackageResponse> call, retrofit2.Response<PurchasePackageResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        Intent intent = new Intent(PackageDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchasePackageResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

}