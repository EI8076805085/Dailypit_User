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
import com.dailypit.dp.Activity.FavouriteServiceActivity;
import com.dailypit.dp.Activity.ServiceAcitvity;
import com.dailypit.dp.Interface.FavouriteInterface;
import com.dailypit.dp.Model.Favourite.MyFavouriteServiceResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class MyFavouriteServiceAdapter extends RecyclerView.Adapter<MyFavouriteServiceAdapter.ViewHolder> {

    List<MyFavouriteServiceResponseData> myFavouriteServiceResponseData;
    Context context;
    AppEventsLogger logger = AppEventsLogger.newLogger(context);
    FavouriteInterface favouriteInterface;

    public MyFavouriteServiceAdapter(List<MyFavouriteServiceResponseData> myFavouriteServiceResponseData, Context context, FavouriteInterface favouriteInterface) {
    this.myFavouriteServiceResponseData = myFavouriteServiceResponseData;
    this.context = context;
    this.favouriteInterface = favouriteInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.fab_list_data, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyFavouriteServiceResponseData myListData = myFavouriteServiceResponseData.get(position);
        holder.txt_ServiceName.setText(myListData.getSubcatName());
        holder.txt_serviceDiscription.setText(HtmlCompat.fromHtml(myListData.getDescriptions(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.img_favorite.setImageResource(R.drawable.ic_favorite_fill);

        if (myListData.getMultiCoupon().equals("1")){
            holder.img_coupon.setImageResource(R.drawable.coupon);
        }

        holder.txt_serviceChildCategory.setText(HtmlCompat.fromHtml(myListData.getCategoryName(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(IMAGE +myListData.getImage()).into(holder.img_Category);
        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteInterface.makeFavouritOrUnfavourite(myListData.getCategoryId(),myListData.getSubcatId());
            }
        });

        holder.fab_serviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logAdClickEvent(myListData.getCategoryName(),myListData.getSubcatName());
                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);
                yourPrefrence.saveData("subCategoryID",myListData.getSubcatId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Category_Name",myListData.getCategoryName());
                intent.putExtra("Service_Name",myListData.getSubcatName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());
                intent.putExtra("flag","0");
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return myFavouriteServiceResponseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ServiceName,txt_serviceChildCategory,txt_serviceDiscription;
        ImageView img_Category,img_coupon,img_favorite;
        LinearLayout fab_serviceLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_ServiceName = itemView.findViewById(R.id.txt_ServiceName);
            txt_serviceDiscription = itemView.findViewById(R.id.txt_serviceDiscription);
            txt_serviceChildCategory = itemView.findViewById(R.id.txt_serviceChildCategory);
            img_Category = itemView.findViewById(R.id.img_Category);
            img_coupon = itemView.findViewById(R.id.img_coupon);
            img_favorite = itemView.findViewById(R.id.img_favorite);
            fab_serviceLayout = itemView.findViewById(R.id.fab_serviceLayout);

        }
    }



    public void logAdClickEvent(String category, String serviceName) {
        Bundle params = new Bundle();
        params.putString("Category Name ", category);
        params.putString("Sub Category Name ", serviceName);
        logger.logEvent("Sub Category Click ", params);

    }
}
