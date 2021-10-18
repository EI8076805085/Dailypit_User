package com.dailypit.dp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Activity.AllServiceActivity;
import com.dailypit.dp.Activity.MemberShip;
import com.dailypit.dp.Activity.SearchActivity;
import com.dailypit.dp.Activity.SubCategoryActivity;
import com.dailypit.dp.Adapter.PackageSliderAdapter;
import com.dailypit.dp.Model.Packageg.PackageResponse;
import com.dailypit.dp.Model.Packageg.PackageResponseData;
import com.facebook.appevents.AppEventsLogger;
import com.github.demono.AutoScrollViewPager;
import com.dailypit.dp.Activity.Address;
import com.dailypit.dp.Activity.Notification;
import com.dailypit.dp.Adapter.MyTopSliderAdapter;
import com.dailypit.dp.Adapter.ServicesAdapter;
import com.dailypit.dp.Adapter.SliderAdapter;
import com.dailypit.dp.Interface.ServiceClickListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.Banner.BottomBannerResponse;
import com.dailypit.dp.Model.Banner.BottomBannerResponseData;
import com.dailypit.dp.Model.Service.ServiceResponse;
import com.dailypit.dp.Model.Service.ServiceResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements  ServiceClickListner {
    RecyclerView serviceRecyclerView;
    ImageView img_notification,img_location,img_search;
    List<BannerResponseData> bannerResponses = new ArrayList<>();
    List<PackageResponseData> packageResponseData = new ArrayList<>();
    List<BottomBannerResponseData> bottomBannerResponses = new ArrayList<>();
    ProgressBar mainProgressbar;
    List<ServiceResponseData> serviceList = new ArrayList<>();
    TextView txt_service,txt_AllServices,txt_stateAndPincode,txt_getSubscription;
    AutoScrollViewPager mViewPager,viewPager2;
    List<UserAddressListResponseData> userAddressListResponseData = new ArrayList<>();
    AppEventsLogger logger;
    LinearLayout packageBannerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = view.findViewById(R.id.viewPager);
        viewPager2 = view.findViewById(R.id.viewPager2);
        img_notification = view.findViewById(R.id.img_notification);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        img_location =view.findViewById(R.id.img_location);
        txt_stateAndPincode =view.findViewById(R.id.txt_stateAndPincode);
        txt_AllServices =view.findViewById(R.id.txt_AllServices);
        txt_service =view.findViewById(R.id.txt_service);
        serviceRecyclerView =view.findViewById(R.id.serviceRecyclerView);
        img_search =view.findViewById(R.id.img_search);
        txt_getSubscription =view.findViewById(R.id.txt_getSubscription);
        packageBannerLayout =view.findViewById(R.id.packageBannerLayout);
        mViewPager.setCycle(true);
        viewPager2.setCycle(true);
        logger = AppEventsLogger.newLogger(getActivity());

        txt_getSubscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MemberShip.class);
                startActivity(intent);
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Address.class);
                startActivity(intent);
            }
        });

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notification.class);
                startActivity(intent);
            }
        });

        if (Helper.INSTANCE.isNetworkAvailable(getContext())) {
            getBanner(view);
            getPackageg();
            getBottomBanner(view);
            getServices(view);
            getUserAddressList();
        } else {
            Helper.INSTANCE.Error(getContext(), getString(R.string.NOCONN));
        }

        return view;
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
        packageBannerLayout.setVisibility(View.VISIBLE);
        viewPager2.setAdapter(new PackageSliderAdapter(getContext(),packageResponseData));
        viewPager2.startAutoScroll();
    }

    private void getBottomBanner(View view) {
        bottomBannerResponses.clear();
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<BottomBannerResponse> call = serviceInterface.getBottomBanner();
        call.enqueue(new Callback<BottomBannerResponse>() {
            @Override
            public void onResponse(Call<BottomBannerResponse> call, retrofit2.Response<BottomBannerResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    bottomBannerResponses = response.body().getData();
                    if (status.equals("1")) {
                        initBottombanner(bottomBannerResponses,view);
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BottomBannerResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getBanner(View view) {
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
                        init(bannerResponses,view);
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void initBottombanner(List<BottomBannerResponseData> bottomBannerResponses, View view) {
             viewPager2.setAdapter(new SliderAdapter(getContext(),bottomBannerResponses,view,this));
             viewPager2.startAutoScroll();

         }

    private void init(List<BannerResponseData> bannerResponses, View view) {
              mViewPager.setAdapter(new MyTopSliderAdapter(getContext(),bannerResponses,view,this));
              mViewPager.startAutoScroll();
     }

    @Override
    public void serviceClick(String position , String name , String discountAmount, String discountType, View view) {
        logAdClickEvent(name);
        if(position.equals("0")) {
            Intent intent = new Intent(getActivity(), AllServiceActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
            intent.putExtra("Category_id",position);
            intent.putExtra("Category_name",name);

            YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
            yourPrefrence.saveData("Discount_Amount", discountAmount);
            yourPrefrence.saveData("CategoryID",position);
            yourPrefrence.saveData("Discount_Type", discountType);
            startActivity(intent);
        }
    }

    private void getServices(View view) {
        serviceList.clear();
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<ServiceResponse> call = serviceInterface.getServices();
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, retrofit2.Response<ServiceResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    txt_service.setText("Our Services");
                    serviceList = response.body().getData();
                    getALLServices(serviceList,view);
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getALLServices(List<ServiceResponseData> serviceList, View view) {
        int size ;
        if(serviceList.size() >= 12) {
           size = 12;
           ServiceResponseData dataa = new ServiceResponseData();
           dataa.setId("0");
           dataa.setName("See All");
           serviceList.add(11, dataa);

        } else {
            size = serviceList.size();
         }

        ServicesAdapter adapter = new ServicesAdapter(size,serviceList,getContext(),view,HomeFragment.this,this);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        serviceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getUserAddressList() {
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String userId = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserAddressListResponse> call = serviceInterface.getUserAddress(map);
        call.enqueue(new Callback<UserAddressListResponse>() {
            @Override
            public void onResponse(Call<UserAddressListResponse> call, retrofit2.Response<UserAddressListResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userAddressListResponseData = response.body().getData();
                        for (int i = 0; i < userAddressListResponseData.size(); i++) {
                            if (userAddressListResponseData.get(i).getDefaults().equals("1")) {
                                String hfNumber = userAddressListResponseData.get(i).getHfNumber();
                                String address = userAddressListResponseData.get(i).getAddress();
                                String ldMark = userAddressListResponseData.get(i).getLandmark();
                                String state = userAddressListResponseData.get(i).getState();
                                String pin = userAddressListResponseData.get(i).getPincode();
                                txt_stateAndPincode.setText(hfNumber + ", " + address + ", " + ldMark + ", " + state + ", " + pin);
                            }
                        }
                    } else {
                        txt_stateAndPincode.setText("");
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public void logAdClickEvent (String name) {
        Bundle params = new Bundle();
        params.putString("Category name ", name);
        logger.logEvent("Category Click", params);
    }
}