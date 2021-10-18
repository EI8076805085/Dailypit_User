package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.FavouriteServiceResponse;
import com.dailypit.dp.Utils.ApiClient;
import com.facebook.appevents.AppEventsLogger;
import com.dailypit.dp.Activity.ServiceAcitvity;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class ServiceSubCategoryAdapter extends RecyclerView.Adapter<ServiceSubCategoryAdapter.ViewHolder>{

    List<ServiceSubCategoryResponseData> serviceSubCategoryList;
    Context context;
    AppEventsLogger logger = AppEventsLogger.newLogger(context);
    String category,category_id;
    ProgressBar mainProgressbar;

    public ServiceSubCategoryAdapter(List<ServiceSubCategoryResponseData> serviceSubCategoryList, Context context, String category, String category_id, ProgressBar mainProgressbar) {
        this.serviceSubCategoryList = serviceSubCategoryList;
        this.context = context;
        this.category = category;
        this.category_id = category_id;
        this.mainProgressbar = mainProgressbar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.sub_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServiceSubCategoryResponseData myListData = serviceSubCategoryList.get(position);

        holder.txt_ServiceName.setText(myListData.getName());
        holder.txt_serviceDiscription.setText(HtmlCompat.fromHtml(myListData.getDescriptions(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.txt_serviceChildCategory.setText(myListData.getChildData());

        if (myListData.getMultiCoupon().equals("1")){
            holder.img_coupon.setImageResource(R.drawable.coupon);
        }

        if(myListData.getFav() != null) {
            if (myListData.getFav().equals("1")){
                holder.img_favorite.setImageResource(R.drawable.ic_favorite_fill);
            } else {
                holder.img_favorite.setImageResource(R.drawable.ic_favorite_unfill);
            }

        }


        Glide.with(context).load(IMAGE +myListData.getImage()).into(holder.img_Category);

        holder.txt_serviceSubCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logAdClickEvent(category,myListData.getName());
                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);
                yourPrefrence.saveData("subCategoryID",myListData.getId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Category_Name",category);
                intent.putExtra("Service_Name",myListData.getName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());


                intent.putExtra("flag","0");
                context.startActivity(intent);

            }
        });

       holder.txt_imgserviceSubCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logAdClickEvent(category,myListData.getName());
                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);

                yourPrefrence.saveData("subCategoryID",myListData.getId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Category_Name",category);
                intent.putExtra("Service_Name",myListData.getName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());
                intent.putExtra("flag","0");
                context.startActivity(intent);

            }
        });

        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFavouriteService(myListData.getId(),holder.img_favorite);
            }
        });

    }

    private void makeFavouriteService(String subCat_id, ImageView img_favorite) {
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String user_id = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("category_id", category_id);
        map.put("subcat_id", subCat_id);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<FavouriteServiceResponse> call = serviceInterface.getFavourite(map);
        call.enqueue(new Callback<FavouriteServiceResponse>() {
            @Override
            public void onResponse(Call<FavouriteServiceResponse> call, retrofit2.Response<FavouriteServiceResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        mainProgressbar.setVisibility(View.GONE);
                        if(response.body().getFav() == 1) {
                            img_favorite.setImageResource(R.drawable.ic_favorite_fill);
                            Toast.makeText(context, "Added to "+"'Your Favourite Service'"+ " in Profile to get quick access", Toast.LENGTH_SHORT).show();
                        } else {
                            img_favorite.setImageResource(R.drawable.ic_favorite_unfill);
                            Toast.makeText(context, "Removed from Your Favourite Service", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(context, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavouriteServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceSubCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ServiceName,txt_serviceChildCategory,txt_serviceDiscription;
        ImageView img_Category,img_coupon,img_favorite;
        LinearLayout txt_serviceSubCategoryLayout,txt_imgserviceSubCategoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ServiceName = itemView.findViewById(R.id.txt_ServiceName);
            txt_serviceDiscription = itemView.findViewById(R.id.txt_serviceDiscription);
            txt_serviceChildCategory = itemView.findViewById(R.id.txt_serviceChildCategory);
            img_Category = itemView.findViewById(R.id.img_Category);
            txt_serviceSubCategoryLayout = itemView.findViewById(R.id.txt_serviceSubCategoryLayout);
            txt_imgserviceSubCategoryLayout = itemView.findViewById(R.id.txt_imgserviceSubCategoryLayout);
            img_coupon = itemView.findViewById(R.id.img_coupon);
            img_favorite = itemView.findViewById(R.id.img_favorite);
        }
    }

    public void logAdClickEvent(String category, String serviceName) {
         Bundle params = new Bundle();
         params.putString("Category Name ", category);
         params.putString("Sub Category Name ", serviceName);
         logger.logEvent("Sub Category Click ", params);

    }
}