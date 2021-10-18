package com.dailypit.dp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Adapter.OrderStatusAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.OrderStatus.OngoingOrderResponse;
import com.dailypit.dp.Model.OrderStatus.OngoingOrderResponseData;
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

public class OngoingOrderFragment extends Fragment {
    RecyclerView onGoingRecyclerView;
    ProgressBar mainProgressbar;
    List<OngoingOrderResponseData> ongoingOrderList = new ArrayList<>();
    ImageView img_dataNotFound;

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
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);
        onGoingRecyclerView = view.findViewById(R.id.onGoingRecyclerView);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        img_dataNotFound = view.findViewById(R.id.img_dataNotFound);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getContext())){
           getOngoingOrder();
        } else {
            Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
        }
    }

    private void getOngoingOrder() {
        ongoingOrderList.clear();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("type", "Ongoing");

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<OngoingOrderResponse> call = serviceInterface.getOngoingOrder(map);
        call.enqueue(new Callback<OngoingOrderResponse>() {
            @Override
            public void onResponse(Call<OngoingOrderResponse> call, retrofit2.Response<OngoingOrderResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {

                        if (onGoingRecyclerView.getVisibility() == View.GONE) {
                            onGoingRecyclerView.setVisibility(View.VISIBLE);
                        }

                        if (img_dataNotFound.getVisibility() == View.VISIBLE) {
                            img_dataNotFound.setVisibility(View.GONE);
                        }

                        ongoingOrderList = response.body().getData();
                        Collections.reverse(ongoingOrderList);
                        OrderStatusAdapter adapter = new OrderStatusAdapter(ongoingOrderList,getContext());
                        onGoingRecyclerView.setHasFixedSize(true);
                        onGoingRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        onGoingRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        onGoingRecyclerView.setVisibility(View.GONE);
                        img_dataNotFound.setVisibility(View.VISIBLE);

                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OngoingOrderResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}