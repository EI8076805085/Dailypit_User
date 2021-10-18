package com.dailypit.dp.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.dailypit.dp.R;


public class MyOrderFragment extends Fragment {

    TabLayout tabs;
    ProgressBar mainProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        openSignINFragment();

        tabs = view.findViewById(R.id.tabs);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (i == 0) {
                tabs.getTabAt(i).setIcon(R.drawable.ongoing);
                // tabs.setBackgroundResource(R.drawable.grey_background);
            } else {
                tabs.getTabAt(i).setIcon(R.drawable.check_circle);
                // tabs.setBackgroundResource(R.drawable.grey_background);
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    openSignINFragment();
                } else if (tab.getPosition() == 1) {
                    openSignUpFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void openSignINFragment() {
        Bundle args = new Bundle();
        Fragment fragmentt = new OngoingOrderFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragmentDAta, fragmentt);
        transaction.commit();
    }

    private void openSignUpFragment() {
        Bundle args = new Bundle();
        Fragment fragmentt = new CompleteOrderFragmeny();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragmentt.setArguments(args);
        transaction.replace(R.id.fragmentDAta, fragmentt);
        transaction.commit();
    }
}