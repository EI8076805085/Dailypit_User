package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Interface.PackageCategory;
import com.dailypit.dp.Interface.PackageQuantity;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.Model.Packageg.PackageResponse;
import com.dailypit.dp.Model.Packageg.PackageServiceChildCategoryResponse;
import com.dailypit.dp.Model.Payment.PackageCartResponse;
import com.dailypit.dp.R;

import java.util.List;

public class PackageServiceChildAdapter extends RecyclerView.Adapter<PackageServiceChildAdapter.ViewHolder> {

    List<PackageCartResponse> packageCartResponses;
    Context context;
    PackageCategory packageCategory;
    PackageQuantity packageQuantity;

    public PackageServiceChildAdapter(List<PackageCartResponse> packageCartResponses, Context context,PackageCategory packageCategory,PackageQuantity packageQuantity) {
        this.packageCartResponses = packageCartResponses;
        this.context = context;
        this.packageCategory = packageCategory;
        this.packageQuantity = packageQuantity;
    }

    @NonNull
    @Override
    public PackageServiceChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.service_child_category_package_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PackageServiceChildAdapter.ViewHolder holder, int position) {
        final PackageCartResponse list = packageCartResponses.get(position);
        holder.txt_serviceName.setText(list.getS_name());
        holder.txt_itemCount.setText(list.getQty());
        holder.txt_serviceCharge.setText(list.getS_total());
        packageCategory.getpackageCategory(list.getS_id(),list.getSub_catID());
        packageQuantity.getpackageQuentity(list.getQty());

    }

    @Override
    public int getItemCount() {
        return packageCartResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_serviceName;
        TextView txt_itemCount,txt_serviceCharge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_serviceName = itemView.findViewById(R.id.txt_serviceName);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            txt_serviceCharge = itemView.findViewById(R.id.txt_serviceCharge);
        }
    }
}
