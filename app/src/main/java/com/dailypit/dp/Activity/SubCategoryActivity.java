package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.ServiceSubCategoryAdapter;
import com.dailypit.dp.Adapter.SubcategoryAdapter;
import com.dailypit.dp.Fragment.HomeFragment;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponse;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.Model.Service.SubcategoryResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SubCategoryActivity extends AppCompatActivity {

    TextView txt_serviceName;
    ImageView back_img,img_cleaning;
    RecyclerView subCategory_recyclerView;
    String categoryId, categoryName,discountAmount,discountType;
    ProgressBar mainProgressbar;
    List<ServiceSubCategoryResponseData> serviceSubCategoryList = new ArrayList<>();
    ImageView img_dataNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        txt_serviceName = findViewById(R.id.txt_serviceName);
        back_img = findViewById(R.id.back_img);
        img_cleaning = findViewById(R.id.img_cleaning);
        subCategory_recyclerView = findViewById(R.id.subCategory_recyclerView);
        mainProgressbar = findViewById(R.id.mainProgressbar);

        categoryId = getIntent().getStringExtra("Category_id");
        categoryName = getIntent().getStringExtra("Category_name");
        discountAmount = getIntent().getStringExtra("Discount_Amount");
        discountType = getIntent().getStringExtra("Discount_Type");

        txt_serviceName.setText(categoryName);
        img_dataNotFound = findViewById(R.id.img_dataNotFound);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(categoryId.equalsIgnoreCase("20")) {
            img_cleaning.setVisibility(View.VISIBLE);
        }

        if (Helper.INSTANCE.isNetworkAvailable(this)) {
            getSubCategoryList(categoryId);
        } else {
            Helper.INSTANCE.Error(SubCategoryActivity.this, getString(R.string.NOCONN));
        }
    }


    private void getSubCategoryList(String categoryId) {
        serviceSubCategoryList.clear();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        map.put("category_id", categoryId);
        map.put("user_id", userId);
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

                        if(categoryId.equalsIgnoreCase("20")) {
                            img_cleaning.setVisibility(View.VISIBLE);
                        }

                        if (subCategory_recyclerView.getVisibility() == View.GONE) {
                            subCategory_recyclerView.setVisibility(View.VISIBLE);
                        }

                        if (img_dataNotFound.getVisibility() == View.VISIBLE) {
                            img_dataNotFound.setVisibility(View.GONE);
                        }

                        serviceSubCategoryList = response.body().getData();
                        ServiceSubCategoryAdapter adapter = new ServiceSubCategoryAdapter(serviceSubCategoryList, SubCategoryActivity.this, categoryName,categoryId,mainProgressbar);
                        subCategory_recyclerView.setHasFixedSize(true);
                        subCategory_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        subCategory_recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        img_dataNotFound.setVisibility(View.VISIBLE);
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
}