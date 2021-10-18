package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.dailypit.dp.Activity.ServiceAcitvity;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;

import java.util.List;

public class ServicesAllCategoryAdapter extends RecyclerView.Adapter<ServicesAllCategoryAdapter.ViewHolder>{
    List<ServiceSubCategoryResponseData> serviceSubCategoryList;
    Context context;
    BottomSheetDialog bottomSheetDialog;


    public ServicesAllCategoryAdapter(List<ServiceSubCategoryResponseData> serviceSubCategoryList, Context context, BottomSheetDialog bottomSheetDialog) {
        this.serviceSubCategoryList = serviceSubCategoryList;
        this.context = context;
        this.bottomSheetDialog =bottomSheetDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.service_sub_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAllCategoryAdapter.ViewHolder holder, int position) {
        final ServiceSubCategoryResponseData myListData = serviceSubCategoryList.get(position);
        holder.txt_notification.setText(myListData.getName());
        holder.txt_serviceSubCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);
                yourPrefrence.saveData("subCategoryID",myListData.getId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Service_Name",myListData.getName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());
                intent.putExtra("flag","0");
                context.startActivity(intent);
                bottomSheetDialog.dismiss();


            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceSubCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_notification;
        LinearLayout txt_serviceSubCategoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_notification = itemView.findViewById(R.id.txt_notification);
            txt_serviceSubCategoryLayout = itemView.findViewById(R.id.txt_serviceSubCategoryLayout);

        }
    }
}
