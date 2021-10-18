package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Interface.AllServicesListner;
import com.dailypit.dp.Model.Service.ServiceResponseData;
import com.dailypit.dp.R;

import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class SeeAllServiceAdapter extends RecyclerView.Adapter<SeeAllServiceAdapter.ViewHolder>{

    List<ServiceResponseData> serviceList;
    AllServicesListner serviceClickListner;
    Context context;

    public SeeAllServiceAdapter(List<ServiceResponseData> serviceList, Context context, AllServicesListner serviceClickListner) {
        this.serviceList = serviceList;
        this.context = context;
        this.serviceClickListner = serviceClickListner;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.servis_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServiceResponseData myListData = serviceList.get(position);
        holder.txt_discription.setText(myListData.getName());
        Glide.with(context).load(IMAGE + myListData.getIcon()).into(holder.imageView);

        holder.service_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceClickListner.serviceClick(myListData.getId(),myListData.getName() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
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
