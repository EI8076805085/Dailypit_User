package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.SearchAdapter;
import com.dailypit.dp.Adapter.ServiceSubCategoryAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponse;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {
    EditText txt_serviceName;
    ImageView back_img,img_cancel;
    RecyclerView subCategory_recyclerView;
    ProgressBar mainProgressbar;
    ArrayList<ServiceSubCategoryResponseData> serviceSubCategoryList = new ArrayList<>();
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txt_serviceName = findViewById(R.id.txt_serviceName);

        back_img = findViewById(R.id.back_img);
        img_cancel = findViewById(R.id.img_cancel);
        subCategory_recyclerView = findViewById(R.id.subCategory_recyclerView);
        mainProgressbar = findViewById(R.id.mainProgressbar);

        if (Helper.INSTANCE.isNetworkAvailable(this)) {
            getSubCategoryList();
        } else {
            Helper.INSTANCE.Error(SearchActivity.this, getString(R.string.NOCONN));
        }


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_serviceName.setText("");
            }
        });


        txt_serviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (adapter != null)
                    adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length()>0)
                {
                    subCategory_recyclerView.setVisibility(View.VISIBLE);
                    //imagesearche.setVisibility(View.GONE);
                }
                else
                {
                    subCategory_recyclerView.setVisibility(View.GONE);
                  //  imagesearche.setVisibility(View.VISIBLE);

                }
            }
        });



    }


    private void getSubCategoryList() {
        serviceSubCategoryList.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("category_id", "0");
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<ServiceSubCategoryResponse> call = serviceInterface.getSubCategory(map);
        call.enqueue(new Callback<ServiceSubCategoryResponse>() {
            @Override
            public void onResponse(Call<ServiceSubCategoryResponse> call, retrofit2.Response<ServiceSubCategoryResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        mainProgressbar.setVisibility(View.GONE);
                        ArrayList<ServiceSubCategoryResponseData> list = new ArrayList<>();
                        list = response.body().getData();
                        setList(list);
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceSubCategoryResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void setList(ArrayList<ServiceSubCategoryResponseData> list) {
         if (list != null && list.size() > 0) {
            if (adapter == null) {
                serviceSubCategoryList.clear();
                serviceSubCategoryList.addAll(list);
                adapter = new SearchAdapter(serviceSubCategoryList, SearchActivity.this);
                subCategory_recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false));
                subCategory_recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                serviceSubCategoryList.clear();
                serviceSubCategoryList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

}