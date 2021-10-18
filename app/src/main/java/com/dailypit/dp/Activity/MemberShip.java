package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.Activity;
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
import com.dailypit.dp.Fragment.MemberShipPlanFragment;
import com.dailypit.dp.Fragment.PackageFragment;
import com.dailypit.dp.Interface.MemberShipPlayListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.MemberShip.AddMemberResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;
import com.github.demono.AutoScrollViewPager;
import com.google.android.material.tabs.TabLayout;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class MemberShip extends AppCompatActivity implements PaymentResultListener {

    AutoScrollViewPager viewPager;
    List<BannerResponseData> bannerResponses = new ArrayList<>();
    LinearLayout memberShipLayout,packageLayout;
    ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship);

        viewPager = findViewById(R.id.viewPager);
        memberShipLayout = findViewById(R.id.memberShipLayout);
        packageLayout = findViewById(R.id.packageLayout);
        back_img = findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        openMemberShipFragment();
        getBottomBanner();

        memberShipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemberShipFragment();
                memberShipLayout.setBackground(MemberShip.this.getResources().getDrawable(R.color.dark_yellow));
                packageLayout.setBackground(MemberShip.this.getResources().getDrawable(R.color.light_gray));
            }
        });

        packageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPackageFragment();
                memberShipLayout.setBackground(MemberShip.this.getResources().getDrawable(R.color.light_gray));
                packageLayout.setBackground(MemberShip.this.getResources().getDrawable(R.color.dark_yellow));
            }
        });
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

    private void openMemberShipFragment() {
        Bundle args = new Bundle();
        Fragment fragmentt = new MemberShipPlanFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragmentDAta, fragmentt);
        transaction.commit();
    }

    private void openPackageFragment() {
        Bundle args = new Bundle();
        Fragment fragmentt = new PackageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragmentDAta, fragmentt);
        transaction.commit();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (razorpayPaymentID.isEmpty()) {
            Toast.makeText(MemberShip.this, "Transaction failed", Toast.LENGTH_SHORT).show();
        } else {
            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
            String userId = yourPrefrence.getData("USERID");
            MemberShipPlanFragment memberShipPlanFragment = new MemberShipPlanFragment();
            memberShipPlanFragment.paymentsuccess(razorpayPaymentID,userId);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
    }
}