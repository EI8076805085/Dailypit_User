package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.HowItWorksAdapters;
import com.dailypit.dp.Adapter.StateAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Address.AddAddressResponse;
import com.dailypit.dp.Model.HowItWorks.HowitWorksResponse;
import com.dailypit.dp.Model.HowItWorks.HowitWorksResponseData;
import com.dailypit.dp.Model.State.StateResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Howitwork extends AppCompatActivity  {

    ProgressBar mainProgressbar;
    ImageView back_img;
    RecyclerView how_it_work_recyclerview;
    List<HowitWorksResponseData> howWorkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howitwork);

        back_img = findViewById(R.id.back_img);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        how_it_work_recyclerview = findViewById(R.id.how_it_work_recyclerview);

        getContant();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getContant() {
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<HowitWorksResponse> call = serviceInterface.getAppContant();
        call.enqueue(new Callback<HowitWorksResponse>() {
            @Override
            public void onResponse(Call<HowitWorksResponse> call, Response<HowitWorksResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        howWorkList = response.body().getData();
                        bindData(howWorkList,mainProgressbar);
                    }
                }
            }

            @Override
            public void onFailure(Call<HowitWorksResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindData(List<HowitWorksResponseData> howWorkList, ProgressBar mainProgressbar) {
        HowItWorksAdapters adapter = new HowItWorksAdapters(howWorkList, getApplicationContext(),mainProgressbar);
        how_it_work_recyclerview.setHasFixedSize(true);
        how_it_work_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        how_it_work_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}