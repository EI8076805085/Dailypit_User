package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailypit.dp.Adapter.MyFavouriteServiceAdapter;
import com.dailypit.dp.Adapter.ServiceSubCategoryAdapter;
import com.dailypit.dp.Interface.FavouriteInterface;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Favourite.MYFavouriteServiceResponse;
import com.dailypit.dp.Model.Favourite.MyFavouriteServiceResponseData;
import com.dailypit.dp.Model.FavouriteServiceResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FavouriteServiceActivity extends AppCompatActivity implements FavouriteInterface {
    RecyclerView favouriteRecyclerview;
    ProgressBar mainProgressbar;
    ImageView back_img,img_dataNotFound;
    List<MyFavouriteServiceResponseData> myFavouriteServiceResponseData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_service);

        favouriteRecyclerview = findViewById(R.id.favouriteRecyclerview);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        back_img = findViewById(R.id.back_img);
        img_dataNotFound = findViewById(R.id.img_dataNotFound);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Helper.INSTANCE.isNetworkAvailable(this)) {
            getFavouriteList();
        } else {
            Helper.INSTANCE.Error(FavouriteServiceActivity.this, getString(R.string.NOCONN));
        }
    }

    private void getFavouriteList() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("USERID");
        myFavouriteServiceResponseData.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MYFavouriteServiceResponse> call = serviceInterface.getFavouriteList(map);
        call.enqueue(new Callback<MYFavouriteServiceResponse>() {
            @Override
            public void onResponse(Call<MYFavouriteServiceResponse> call, retrofit2.Response<MYFavouriteServiceResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        mainProgressbar.setVisibility(View.GONE);

                        if (favouriteRecyclerview.getVisibility() == View.GONE) {
                            favouriteRecyclerview.setVisibility(View.VISIBLE);
                        }

                        if (img_dataNotFound.getVisibility() == View.VISIBLE) {
                            img_dataNotFound.setVisibility(View.GONE);
                        }

                        myFavouriteServiceResponseData = response.body().getData();
                        MyFavouriteServiceAdapter adapter = new MyFavouriteServiceAdapter(myFavouriteServiceResponseData,FavouriteServiceActivity.this, FavouriteServiceActivity.this);
                        favouriteRecyclerview.setHasFixedSize(true);
                        favouriteRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        favouriteRecyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        img_dataNotFound.setVisibility(View.VISIBLE);
                        favouriteRecyclerview.setVisibility(View.GONE);
                        mainProgressbar.setVisibility(View.GONE);
                     }
                }
            }

            @Override
            public void onFailure(Call<MYFavouriteServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void makeFavouritOrUnfavourite(String category_id, String subCat_id) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("category_id", category_id);
        map.put("subcat_id", subCat_id);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<FavouriteServiceResponse> call = serviceInterface.getFavourite(map);
        call.enqueue(new Callback<FavouriteServiceResponse>() {
            @Override
            public void onResponse(Call<FavouriteServiceResponse> call, retrofit2.Response<FavouriteServiceResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        getFavouriteList();
                        Toast.makeText(FavouriteServiceActivity.this, "Removed from Your Favourite Service", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavouriteServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());
            }
        });
    }

}