package com.dailypit.dp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Fragment.HomeFragment;
import com.dailypit.dp.Interface.ServiceClickListner;
import com.dailypit.dp.Model.Service.ServiceResponseData;
import com.dailypit.dp.R;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;


import static com.dailypit.dp.Utils.Constants.IMAGE;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder>{

    List<ServiceResponseData> serviceList;
    ServiceClickListner serviceClickListner;
    Context context;
    HomeFragment homeFragment;
    int size;
    View view;


    public ServicesAdapter(int size, List<ServiceResponseData> serviceList, Context context, View view, HomeFragment homeFragment, ServiceClickListner serviceClickListner) {
        this.serviceList = serviceList;
        this.context = context;
        this.serviceClickListner = serviceClickListner;
        this.homeFragment = homeFragment;
        this.size = size;
        this.view = view;
    }

    @NonNull
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.servis_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.ViewHolder holder, int position) {

        final ServiceResponseData myListData = serviceList.get(position);
        holder.txt_discription.setText(myListData.getName());

        if(position == 11) {
            holder.imageView.setImageResource(R.drawable.ic_see_all);
        } else {
            Glide.with(context).load(IMAGE +myListData.getIcon()).into(holder.imageView);
        }


        holder.service_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceClickListner.serviceClick(myListData.getId(),myListData.getName(),myListData.getDiscount_amount(),myListData.getDiscount_type(), view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public TextView txt_discription;
        public  TextView txt_discount;
        LinearLayout service_linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.serviceImage);
            txt_discription = itemView.findViewById(R.id.txt_discription);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            service_linearLayout = itemView.findViewById(R.id.service_linearLayout);
        }
    }


}
