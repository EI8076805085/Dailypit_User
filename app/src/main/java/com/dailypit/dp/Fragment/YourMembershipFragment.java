package com.dailypit.dp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Activity.MemberShipPlanDetails;
import com.dailypit.dp.Adapter.MembershipServicePlanAdapter;
import com.dailypit.dp.Adapter.PlanDetailsAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlan;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlanData;
import com.dailypit.dp.Model.MemberShip.MemberShipContentList;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class YourMembershipFragment extends Fragment {

    List<CheckMemberShipPlanData> memberShipPlandata = new ArrayList<>();
    TextView txt_expireDate,txt_memberShipPlanName;
    ProgressBar mainProgressbar;
    List<PlanDetailsData> planDetailsData = new ArrayList<>();
    RecyclerView membership_service_list_response;
    TextView txt_up_toDiscount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_membership, container, false);
        txt_expireDate = view.findViewById(R.id.txt_expireDate);
        txt_memberShipPlanName = view.findViewById(R.id.txt_memberShipPlanName);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        membership_service_list_response = view.findViewById(R.id.membership_service_list_response);
        txt_up_toDiscount = view.findViewById(R.id.txt_up_toDiscount);

        isMemberShipPlan();
        getContentList();

        return  view;
    }

    private void isMemberShipPlan() {
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
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
                        getPlanDetails();
                        memberShipPlandata = response.body().getData();
                        for (int i = 0; i < memberShipPlandata.size(); i++) {
                            txt_expireDate.setText("Expire in "+memberShipPlandata.get(i).getValidity());
                            txt_memberShipPlanName.setText("YOUR "+memberShipPlandata.get(i).getValidity()+" MEMBERSHIP PLAN ACTIVE"  );
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

    private void getContentList() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MemberShipContentList> call = serviceInterface.getContentList();
        call.enqueue(new Callback<MemberShipContentList>() {
            @Override
            public void onResponse(Call<MemberShipContentList> call, retrofit2.Response<MemberShipContentList> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_up_toDiscount.setVisibility(View.VISIBLE);
                        txt_up_toDiscount.setText(response.body().getDiscount());
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberShipContentList> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
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
        PlanDetailsAdapter adapter = new PlanDetailsAdapter(planDetailsData,getActivity());
        membership_service_list_response.setHasFixedSize(true);
        membership_service_list_response.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        membership_service_list_response.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}