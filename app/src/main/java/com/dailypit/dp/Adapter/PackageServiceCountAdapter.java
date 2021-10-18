package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponse;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponseData;
import com.dailypit.dp.Model.Packageg.PackageService;
import com.dailypit.dp.Model.Packageg.PackageServicesList;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.R;

import java.util.ArrayList;
import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class PackageServiceCountAdapter extends RecyclerView.Adapter<PackageServiceCountAdapter.ViewHolder>{

    List<PackagePlanlistResponseData> packagePlanlistResponseData;
    List<PackageService> packageServices = new ArrayList<>();
    Context context;

    public PackageServiceCountAdapter(List<PackagePlanlistResponseData> packagePlanlistResponseData, Context context) {
        this.packagePlanlistResponseData = packagePlanlistResponseData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.available_package_plan_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PackagePlanlistResponseData list = packagePlanlistResponseData.get(position);
        Glide.with(context).load(IMAGE +list.getCategoryIcon()).into(holder.img_plan);
        holder.txt_serviceName.setText(list.getCategoryName());

        packageServices = list.getServices();

        PackageSubCategoryListAdapter adapter = new PackageSubCategoryListAdapter(packageServices,context);
        holder.packagePlayRecyclerView.setHasFixedSize(true);
        holder.packagePlayRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.packagePlayRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return packagePlanlistResponseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_serviceCount,txt_serviceName;
        ImageView img_plan;
        RecyclerView packagePlayRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_serviceCount = itemView.findViewById(R.id.txt_serviceCount);
            txt_serviceName = itemView.findViewById(R.id.txt_serviceName);
            img_plan = itemView.findViewById(R.id.img_plan);
            packagePlayRecyclerView = itemView.findViewById(R.id.packagePlayRecyclerView);

        }
    }
}

