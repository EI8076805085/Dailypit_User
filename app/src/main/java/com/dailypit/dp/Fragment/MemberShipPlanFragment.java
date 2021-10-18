package com.dailypit.dp.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.MemberShipAdapter;
import com.dailypit.dp.Adapter.MembershipServicePlanAdapter;
import com.dailypit.dp.Interface.MemberShipPlayListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.MemberShip.AddMemberResponse;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlan;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlanData;
import com.dailypit.dp.Model.MemberShip.MemberShipContentList;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListResponse;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListdata;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;
import com.github.demono.AutoScrollViewPager;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MemberShipPlanFragment extends Fragment implements MemberShipPlayListner {

    public static ProgressBar mainProgressbar;
    public static String userName, userEmail, userMobile;
    public static int price;
    public static List<MemberShipPlanListdata> memberShipPlanListdata = new ArrayList<>();
    public static List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    public static List<CheckMemberShipPlanData> memberShipPlandata = new ArrayList<>();
    public static List<String> contentList = new ArrayList<String>();
    public static RecyclerView memberShipPlanRecyclerView, membership_service_list_response;
    public static String planId = "", planCharg;
    public static TextView txt_proceedToPay, txt_expireDate, txt_memberShipPlanName, txt_up_toDiscount;
    public static CardView cartProceed;
    public static LinearLayout choosYourPlanlayout, isMemberShipLayout, ischeckMemberShipLayout;
    public static RelativeLayout proceedPayLayout;
    public static YourPreference yourPrefrence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_ship_plan, container, false);

        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        txt_proceedToPay = view.findViewById(R.id.txt_proceedToPay);
        cartProceed = view.findViewById(R.id.cartProceed);
        choosYourPlanlayout = view.findViewById(R.id.choosYourPlanlayout);
        isMemberShipLayout = view.findViewById(R.id.isMemberShipLayout);
        ischeckMemberShipLayout = view.findViewById(R.id.ischeckMemberShipLayout);
        proceedPayLayout = view.findViewById(R.id.proceedPayLayout);
        txt_expireDate = view.findViewById(R.id.txt_expireDate);
        txt_memberShipPlanName = view.findViewById(R.id.txt_memberShipPlanName);
        txt_up_toDiscount = view.findViewById(R.id.txt_up_toDiscount);
        memberShipPlanRecyclerView = view.findViewById(R.id.memberShipPlanRecyclerView);
        membership_service_list_response = view.findViewById(R.id.membership_service_list_response);

        yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");

        isMemberShipPlan(userId);
        getContentListData();
        getUserProfile();


        cartProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planId.equals("") || (planId.equals("null"))) {
                    Toast.makeText(getActivity(), "Please Select Plan", Toast.LENGTH_SHORT).show();
                } else if (planCharg.equals("") || (planCharg.equals("null"))) {
                    Toast.makeText(getActivity(), "Please Select Plan", Toast.LENGTH_SHORT).show();
                } else {
                    startpayment(planCharg);
                }
            }
        });

        return view;
    }

    private void getContentListData() {
        contentList.clear();
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MemberShipContentList> call = serviceInterface.getContentList();
        call.enqueue(new Callback<MemberShipContentList>() {
            @Override
            public void onResponse(Call<MemberShipContentList> call, retrofit2.Response<MemberShipContentList> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        if (contentList.isEmpty()) {
                            txt_up_toDiscount.setVisibility(View.VISIBLE);
                            txt_up_toDiscount.setText(response.body().getDiscount());
                            String content = response.body().getDescription();

                            List<String> itemNameParentList = Arrays.asList(content);
                            for (int i = 0; i < itemNameParentList.size(); i++) {
                                List<String> itemNameList = Arrays.asList(itemNameParentList.get(i).split(","));
                                for (int j = 0; j < itemNameList.size(); j++) {
                                    contentList.add(itemNameList.get(j));
                                }
                            }

                            MembershipServicePlanAdapter adapter = new MembershipServicePlanAdapter(contentList, getActivity());
                            membership_service_list_response.setHasFixedSize(true);
                            membership_service_list_response.setLayoutManager(new LinearLayoutManager(getActivity()));
                            membership_service_list_response.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberShipContentList> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

    private void isMemberShipPlan(String userId) {
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
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
                        if (memberShipPlanRecyclerView.getVisibility() == View.VISIBLE) {
                            memberShipPlanRecyclerView.setVisibility(View.GONE);
                            choosYourPlanlayout.setVisibility(View.GONE);
                            proceedPayLayout.setVisibility(View.GONE);
                        }

                        if (ischeckMemberShipLayout.getVisibility() == View.GONE) {
                            ischeckMemberShipLayout.setVisibility(View.VISIBLE);
                            isMemberShipLayout.setVisibility(View.GONE);
                        }

                        memberShipPlandata = response.body().getData();
                        for (int i = 0; i < memberShipPlandata.size(); i++) {
                            txt_expireDate.setText("Expire in " + memberShipPlandata.get(i).getValidity());
                            txt_memberShipPlanName.setText("YOUR " + memberShipPlandata.get(i).getValidity() + " MEMBERSHIP PLAN ACTIVE");
                        }
                    } else {
                        getMemberShipPlanList();
                        if (memberShipPlanRecyclerView.getVisibility() == View.GONE) {
                            memberShipPlanRecyclerView.setVisibility(View.VISIBLE);
                            choosYourPlanlayout.setVisibility(View.VISIBLE);
                            proceedPayLayout.setVisibility(View.VISIBLE);
                        }

                        if (ischeckMemberShipLayout.getVisibility() == View.VISIBLE) {
                            ischeckMemberShipLayout.setVisibility(View.GONE);
                            isMemberShipLayout.setVisibility(View.VISIBLE);
                        }
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

    public void startpayment(String money) {
        Checkout checkout = new Checkout();
        final Activity activity = getActivity();
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

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
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
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

    public void paymentsuccess(String razorpayPaymentID, String userId) {
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
                        isMemberShipPlan(userId);
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

    private void getMemberShipPlanList() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MemberShipPlanListResponse> call = serviceInterface.getMemberShipPlanList();
        call.enqueue(new Callback<MemberShipPlanListResponse>() {
            @Override
            public void onResponse(Call<MemberShipPlanListResponse> call, retrofit2.Response<MemberShipPlanListResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        memberShipPlanListdata = response.body().getData();
                        bindPlan(memberShipPlanListdata);
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberShipPlanListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindPlan(List<MemberShipPlanListdata> memberShipPlanListdata) {
        MemberShipAdapter adapter = new MemberShipAdapter(memberShipPlanListdata, "0", getActivity(), this);
        memberShipPlanRecyclerView.setHasFixedSize(true);
        memberShipPlanRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        memberShipPlanRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void planListner(String id, String validity, String money) {
        planId = id;
        planCharg = money;
        txt_proceedToPay.setText("Buy for just " + validity + " Rs. " + money);
        cartProceed.setCardBackgroundColor(Color.parseColor("#FFB300"));
    }

}