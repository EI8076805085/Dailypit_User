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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Activity.OrderAssignTo;
import com.dailypit.dp.Adapter.AddOnServiceAdapter;
import com.dailypit.dp.Adapter.CompleteOrderAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponse;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData;
import com.dailypit.dp.Model.OrderStatus.CompleteOrderResponse;
import com.dailypit.dp.Model.OrderStatus.CompleteOrderResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CompleteOrderFragmeny extends Fragment {

    RecyclerView completeOrderRecyclerView;
    ProgressBar mainProgressbar;
    ImageView img_dataNotFound;
    List<CompleteOrderResponseData> completeOrderList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete_order_fragmeny, container, false);

        completeOrderRecyclerView = view.findViewById(R.id.completeOrderRecyclerView);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        img_dataNotFound = view.findViewById(R.id.img_dataNotFound);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getContext())){
            getCompleteOrder();
        } else {
            Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
        }
    }

    private void getCompleteOrder() {
        completeOrderList.clear();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("type", "Completed");

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CompleteOrderResponse> call = serviceInterface.getCompleteOrder(map);
        call.enqueue(new Callback<CompleteOrderResponse>() {
            @Override
            public void onResponse(Call<CompleteOrderResponse> call, retrofit2.Response<CompleteOrderResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        if (completeOrderRecyclerView.getVisibility() == View.GONE) {
                            completeOrderRecyclerView.setVisibility(View.VISIBLE);
                        }

                        if (img_dataNotFound.getVisibility() == View.VISIBLE) {
                            img_dataNotFound.setVisibility(View.GONE);
                        }

                        completeOrderList = response.body().getData();
                        Collections.reverse(completeOrderList);
                        CompleteOrderAdapter adapter = new CompleteOrderAdapter(getActivity(),completeOrderList,mainProgressbar);
                        completeOrderRecyclerView.setHasFixedSize(true);
                        completeOrderRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        completeOrderRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        completeOrderRecyclerView.setVisibility(View.GONE);
                        img_dataNotFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<CompleteOrderResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }
}