package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.PackageServiceChildAdapter;
import com.dailypit.dp.Adapter.PackageSubCategoryAdapter;
import com.dailypit.dp.Interface.PackageAmountListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Packageg.CategoryResponse;
import com.dailypit.dp.Model.Packageg.CategoryResponseData;
import com.dailypit.dp.Model.Packageg.CheckPackageResponse;
import com.dailypit.dp.Model.Packageg.PackageCategory;
import com.dailypit.dp.Model.Packageg.PackageServiceChildCategoryResponse;
import com.dailypit.dp.Model.Packageg.PackageSubCategoryResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.PackageDB;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PackageChildServiceActivity extends AppCompatActivity implements PackageAmountListner {

    RecyclerView package_serviceRecyclerView;
    TextView txt_continue,txt_categoryName,txt_payTotalMoney;
    ImageView back_img;
    String package_id= "",category_id = "",category_name = "",unique_id = "";
    ProgressBar mainProgressbar;
    List<CategoryResponseData> checkPackageResponseData = new ArrayList<>();
    PackageDB databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_child_service);

        setUpDB();

        package_serviceRecyclerView = findViewById(R.id.package_serviceRecyclerView);
        txt_continue = findViewById(R.id.txt_continue);
        back_img = findViewById(R.id.back_img);
        txt_categoryName = findViewById(R.id.txt_categoryName);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_payTotalMoney = findViewById(R.id.txt_payTotalMoney);

        Intent intent = getIntent();
        package_id = intent.getStringExtra("Package_id");
        category_id = intent.getStringExtra("Category_id");
        category_name = intent.getStringExtra("Category_name");
        unique_id = intent.getStringExtra("unique_id");

        txt_categoryName.setText(category_name);
        getCategoryListData(package_id,category_id);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalAmount = txt_payTotalMoney.getText().toString();

                if(totalAmount.equalsIgnoreCase("0") || totalAmount.equalsIgnoreCase("")){
                    Toast.makeText(PackageChildServiceActivity.this, "Please Add services", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PackageChildServiceActivity.this, BookPackageServiceActivity.class);
                    intent.putExtra("totalAmount",totalAmount);
                    intent.putExtra("category_id",category_id);
                    intent.putExtra("unique_id",unique_id);
                    intent.putExtra("package_id",package_id);

                    startActivity(intent);
                }
            }
        });
    }


    private void getCategoryListData(String package_id, String category_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("USERID");
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("package_id", package_id);
        map.put("user_id", user_id);
        map.put("category_id", category_id);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CategoryResponse> call = serviceInterface.getpackageCategoryList(map);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, retrofit2.Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        checkPackageResponseData = response.body().getData();
                        bindPackagegCategory(checkPackageResponseData);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void bindPackagegCategory(List<CategoryResponseData> checkPackageResponseData) {
        PackageSubCategoryAdapter adapter = new PackageSubCategoryAdapter(checkPackageResponseData, getApplicationContext(),this);
        package_serviceRecyclerView.setHasFixedSize(true);
        package_serviceRecyclerView.setLayoutManager(new GridLayoutManager(PackageChildServiceActivity.this, 1));
        package_serviceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void getTotalAmount(String amount) {
        txt_payTotalMoney.setText(amount);
    }

    @Override
    public void onBackPressed() {
        databaseHandler.packageInterface().deleteallpackage();
        super.onBackPressed();
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(PackageChildServiceActivity.this, PackageDB.class, "package").allowMainThreadQueries().build();
    }

}