package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.SeeAllServiceAdapter;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.github.demono.AutoScrollViewPager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.dailypit.dp.Adapter.AllServiceSlider;
import com.dailypit.dp.Adapter.ServicesAllCategoryAdapter;
import com.dailypit.dp.Interface.AllServiceIDListner;
import com.dailypit.dp.Interface.AllServicesListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Banner.BannerResponse;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.Service.ServiceResponse;
import com.dailypit.dp.Model.Service.ServiceResponseData;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponse;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AllServiceActivity extends AppCompatActivity implements AllServicesListner, AllServiceIDListner {

    ProgressBar mainProgressbar;
    ImageView back_img;
    List<ServiceResponseData> serviceList = new ArrayList<>();
    List<ServiceSubCategoryResponseData> serviceSubCategoryList = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    RecyclerView serviceRecyclerView,subCategoryServiceRecyclerView;
    TextView txt_ServicesNotAvailable,txt_ChoosService,txt_service,txt_ServicesCancel,txt_services;
    ArrayList <String> banner=new ArrayList<>();
    List<BannerResponseData> bannerResponses = new ArrayList<>();
    String image;
    private static ViewPager mPager,bottomPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CirclePageIndicator indicator;
    AutoScrollViewPager mViewPager,viewPager2;
    AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_service);
        mViewPager = findViewById(R.id.viewPager);
        logger = AppEventsLogger.newLogger(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_service = findViewById(R.id.txt_service);
        txt_services = findViewById(R.id.txt_services);

//        indicator = findViewById(R.id.indicator);
//        mPager = findViewById(R.id.view_pager_product);

        back_img = findViewById(R.id.back_img);
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (Helper.INSTANCE.isNetworkAvailable(getApplicationContext())){
            getServices();
            getBanner();
        } else {
            Helper.INSTANCE.Error(getApplicationContext(), getString(R.string.NOCONN));
        }

    }

    private void getServices() {
        serviceList.clear();
        HashMap<String, String> map = new HashMap<>();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
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
                    txt_services.setText("Our Services");
                    serviceList = response.body().getData();
                    getALLServices(serviceList);

                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }


    private void getALLServices(List<ServiceResponseData> serviceList) {
        SeeAllServiceAdapter adapter = new SeeAllServiceAdapter(serviceList,getApplicationContext(), this);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        serviceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void serviceClick(String position , String name) {

        logAdClickEvent(name);
        Intent intent = new Intent(AllServiceActivity.this, SubCategoryActivity.class);
        intent.putExtra("Category_id",position);
        intent.putExtra("Category_name",name);
        startActivity(intent);

//        bottomSheetDialog = new BottomSheetDialog(AllServiceActivity.this,R.style.BottomSheetTheem);
//        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,findViewById(R.id.bottom_sheet));
//        subCategoryServiceRecyclerView = sheetView.findViewById(R.id.subCategoryServiceRecyclerView);
//        txt_ServicesNotAvailable = sheetView.findViewById(R.id.txt_ServicesNotAvailable);
//        txt_ChoosService = sheetView.findViewById(R.id.txt_ChoosService);
//        txt_ServicesCancel = sheetView.findViewById(R.id.txt_ServicesCancel);
//
//
//        if (Helper.INSTANCE.isNetworkAvailable(getApplicationContext())){
//            serviceSubCategoryList.clear();
//            HashMap<String, String> map = new HashMap<>();
//            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
//            map.put("category_id", position);
//            mainProgressbar.setVisibility(View.VISIBLE);
//            ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
//            Call<ServiceSubCategoryResponse> call = serviceInterface.getSubCategory(map);
//            call.enqueue(new Callback<ServiceSubCategoryResponse>() {
//                @Override
//                public void onResponse(Call<ServiceSubCategoryResponse> call, retrofit2.Response<ServiceSubCategoryResponse> response) {
//                    if (response.isSuccessful()) {
//                        String status = response.body().getStatus().toString();
//
//                        if (status.equals("1")) {
//                            if (txt_ServicesNotAvailable.getVisibility() == View.VISIBLE) {
//                                txt_ServicesNotAvailable.setVisibility(View.GONE);
//                            }
//
//                            if (subCategoryServiceRecyclerView.getVisibility() == View.GONE) {
//                                subCategoryServiceRecyclerView.setVisibility(View.VISIBLE);
//                                txt_ChoosService.setVisibility(View.VISIBLE);
//                            }
//
//                            mainProgressbar.setVisibility(View.GONE);
//                            serviceSubCategoryList = response.body().getData();
//                            ServicesAllCategoryAdapter adapter = new ServicesAllCategoryAdapter(serviceSubCategoryList,AllServiceActivity.this,bottomSheetDialog);
//                            subCategoryServiceRecyclerView.setHasFixedSize(true);
//                            subCategoryServiceRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
//                            subCategoryServiceRecyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            txt_ServicesNotAvailable.setVisibility(View.VISIBLE);
//                            subCategoryServiceRecyclerView.setVisibility(View.GONE);
//                            txt_ChoosService.setVisibility(View.GONE);
//                            mainProgressbar.setVisibility(View.GONE);
//                        }
//                    } else {
//                        mainProgressbar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ServiceSubCategoryResponse> call, Throwable t) {
//                    Log.d("ff", t.toString());
//                    mainProgressbar.setVisibility(View.GONE);
//
//                }
//            });
//        } else {
//            Helper.INSTANCE.Error(getApplicationContext(), getString(R.string.NOCONN));
//        }
//
//        bottomSheetDialog.setContentView(sheetView);
//        bottomSheetDialog.show();
//
//        txt_ServicesCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getBanner() {
        banner.clear();
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
                        for(int i = 0; i<bannerResponses.size(); i++ ){
                            image = response.body().getData().get(i).getImage();
                            banner.add(image);
                        }
                        init(bannerResponses);
                    } else {
                        Toast.makeText(AllServiceActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(AllServiceActivity.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
                 }
        });
    }

    private void init(List<BannerResponseData> bannerResponses) {
        mViewPager.setAdapter(new AllServiceSlider(AllServiceActivity.this,bannerResponses,this));
        mViewPager.startAutoScroll();

    }

    @Override
    public void serviceClickId(String position , String name) {

        logAdClickEvent(name);
        Intent intent = new Intent(AllServiceActivity.this, SubCategoryActivity.class);
        intent.putExtra("Category_id",position);
        intent.putExtra("Category_name",name);
        startActivity(intent);


//        bottomSheetDialog = new BottomSheetDialog(AllServiceActivity.this,R.style.BottomSheetTheem);
//        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,findViewById(R.id.bottom_sheet));
//        subCategoryServiceRecyclerView = sheetView.findViewById(R.id.subCategoryServiceRecyclerView);
//        txt_ServicesNotAvailable = sheetView.findViewById(R.id.txt_ServicesNotAvailable);
//        txt_ChoosService = sheetView.findViewById(R.id.txt_ChoosService);
//        txt_ServicesCancel = sheetView.findViewById(R.id.txt_ServicesCancel);
//
//        if (Helper.INSTANCE.isNetworkAvailable(getApplicationContext())){
//            serviceSubCategoryList.clear();
//            HashMap<String, String> map = new HashMap<>();
//            YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
//            map.put("category_id", position);
//            mainProgressbar.setVisibility(View.VISIBLE);
//            ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
//            Call<ServiceSubCategoryResponse> call = serviceInterface.getSubCategory(map);
//            call.enqueue(new Callback<ServiceSubCategoryResponse>() {
//                @Override
//                public void onResponse(Call<ServiceSubCategoryResponse> call, retrofit2.Response<ServiceSubCategoryResponse> response) {
//                    if (response.isSuccessful()) {
//                        String status = response.body().getStatus().toString();
//
//                        if (status.equals("1")) {
//                            if (txt_ServicesNotAvailable.getVisibility() == View.VISIBLE) {
//                                txt_ServicesNotAvailable.setVisibility(View.GONE);
//                            }
//
//                            if (subCategoryServiceRecyclerView.getVisibility() == View.GONE) {
//                                subCategoryServiceRecyclerView.setVisibility(View.VISIBLE);
//                                txt_ChoosService.setVisibility(View.VISIBLE);
//                            }
//
//                            mainProgressbar.setVisibility(View.GONE);
//                            serviceSubCategoryList = response.body().getData();
//                            ServicesAllCategoryAdapter adapter = new ServicesAllCategoryAdapter(serviceSubCategoryList,AllServiceActivity.this,bottomSheetDialog);
//                            subCategoryServiceRecyclerView.setHasFixedSize(true);
//                            subCategoryServiceRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
//                            subCategoryServiceRecyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            txt_ServicesNotAvailable.setVisibility(View.VISIBLE);
//                            subCategoryServiceRecyclerView.setVisibility(View.GONE);
//                            txt_ChoosService.setVisibility(View.GONE);
//                            mainProgressbar.setVisibility(View.GONE);
//                        }
//                    } else {
//                        mainProgressbar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ServiceSubCategoryResponse> call, Throwable t) {
//                    Log.d("ff", t.toString());
//                    mainProgressbar.setVisibility(View.GONE);
//                }
//            });
//        } else {
//            Helper.INSTANCE.Error(getApplicationContext(), getString(R.string.NOCONN));
//        }
//
//        bottomSheetDialog.setContentView(sheetView);
//        bottomSheetDialog.show();
//
//        txt_ServicesCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });

    }

    private void logAdClickEvent(String name) {
        Bundle params = new Bundle();
        params.putString("Category name ", name);
        logger.logEvent("Category Click", params);
    }
}