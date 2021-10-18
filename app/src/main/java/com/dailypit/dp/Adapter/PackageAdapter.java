package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Activity.PackageDetailActivity;
import com.dailypit.dp.Model.Packageg.PackageResponseData;
import com.dailypit.dp.R;

import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder>{

    List<PackageResponseData> myListData;
    Context context;

    public PackageAdapter(List<PackageResponseData> myListData, FragmentActivity activity) {
        this.myListData = myListData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.package_plan_listdata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PackageResponseData list = myListData.get(position);

        Glide.with(context).load(IMAGE +list.getPackageImage()).into(holder.img_package);

        holder.img_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageDetailActivity.class);
                intent.putExtra("Package_id", list.getPackageId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_package;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_package = itemView.findViewById(R.id.img_package);

        }
    }
}

