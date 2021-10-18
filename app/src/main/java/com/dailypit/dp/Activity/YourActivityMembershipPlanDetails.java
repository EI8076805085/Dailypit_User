package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dailypit.dp.Fragment.MemberShipPlanFragment;
import com.dailypit.dp.Fragment.PackageFragment;
import com.dailypit.dp.Fragment.YourMembershipFragment;
import com.dailypit.dp.Fragment.YourPackageFragment;
import com.dailypit.dp.R;

public class YourActivityMembershipPlanDetails extends AppCompatActivity {

    ImageView back_img;
    LinearLayout memberShipLayout,packageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_membership_plan_details);
        memberShipLayout = findViewById(R.id.memberShipLayout);
        packageLayout = findViewById(R.id.packageLayout);
        back_img = findViewById(R.id.back_img);

        openMemberShipFragment();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        memberShipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemberShipFragment();
                memberShipLayout.setBackground(YourActivityMembershipPlanDetails.this.getResources().getDrawable(R.color.dark_yellow));
                packageLayout.setBackground(YourActivityMembershipPlanDetails.this.getResources().getDrawable(R.color.light_gray));
            }
        });

        packageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPackageFragment();
                memberShipLayout.setBackground(YourActivityMembershipPlanDetails.this.getResources().getDrawable(R.color.light_gray));
                packageLayout.setBackground(YourActivityMembershipPlanDetails.this.getResources().getDrawable(R.color.dark_yellow));
            }
        });
    }


    private void openMemberShipFragment() {
        Bundle args = new Bundle();
        Fragment fragmentt = new YourMembershipFragment();
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

}