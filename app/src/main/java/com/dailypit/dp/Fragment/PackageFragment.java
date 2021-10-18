package com.dailypit.dp.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.dailypit.dp.Adapter.PackageAdapter;
import com.dailypit.dp.Adapter.YourPackageAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Packageg.CheckPackageResponse;
import com.dailypit.dp.Model.Packageg.CheckPackageResponseData;
import com.dailypit.dp.Model.Packageg.PackageResponse;
import com.dailypit.dp.Model.Packageg.PackageResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class PackageFragment extends Fragment {

    List<PackageResponseData> packageResponseData = new ArrayList<>();
    List<CheckPackageResponseData> checkPackageResponseData = new ArrayList<>();
    RecyclerView packageRecyclerView, yourpackageRecyclerView;
    ProgressBar mainProgressbar;
    LinearLayout myPackageLayout,packageLayout;
    View viewLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_package, container, false);
        packageRecyclerView = view.findViewById(R.id.packageRecyclerView);
        yourpackageRecyclerView = view.findViewById(R.id.yourpackageRecyclerView);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        myPackageLayout = view.findViewById(R.id.myPackageLayout);
        packageLayout = view.findViewById(R.id.packageLayout);
        viewLine = view.findViewById(R.id.viewLine);

        getPackageg();
        isPackage();

        return view;
    }

    private void isPackage() {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("USERID");
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CheckPackageResponse> call = serviceInterface.getMyPackage(map);
        call.enqueue(new Callback<CheckPackageResponse>() {
            @Override
            public void onResponse(Call<CheckPackageResponse> call, retrofit2.Response<CheckPackageResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        myPackageLayout.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                        checkPackageResponseData = response.body().getPackageData();
                        bindPackagegCategory(checkPackageResponseData);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckPackageResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindPackagegCategory(List<CheckPackageResponseData> checkPackageResponseData) {
        YourPackageAdapter adapter = new YourPackageAdapter(checkPackageResponseData, (FragmentActivity) getContext());
        yourpackageRecyclerView.setHasFixedSize(true);
        yourpackageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        yourpackageRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getPackageg() {
        packageResponseData.clear();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String user_id = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PackageResponse> call = serviceInterface.getPackageg(map);
        call.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, retrofit2.Response<PackageResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (response.body().getData().get(i).getTaken_package().equals("1")) {

                            } else {
                                packageLayout.setVisibility(View.VISIBLE);
                                packageResponseData.add(response.body().getData().get(i));
                                bindPackageg(packageResponseData);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindPackageg(List<PackageResponseData> packageResponseData) {
        PackageAdapter adapter = new PackageAdapter(packageResponseData, (FragmentActivity) getContext());
        packageRecyclerView.setHasFixedSize(true);
        packageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        packageRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}