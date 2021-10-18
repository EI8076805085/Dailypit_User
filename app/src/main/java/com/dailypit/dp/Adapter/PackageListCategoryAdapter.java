package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Activity.PackageChildServiceActivity;
import com.dailypit.dp.Model.Packageg.PackageCategory;
import com.dailypit.dp.R;

import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class PackageListCategoryAdapter extends RecyclerView.Adapter<PackageListCategoryAdapter.ViewHolder>{

    List<PackageCategory> myListData;
    Context context;

    public PackageListCategoryAdapter(List<PackageCategory> myListData, Context context) {
        this.myListData = myListData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.your_package_plan_listdata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PackageCategory list = myListData.get(position);
        holder.txt_packageName.setText(list.getCategoryName());

        Glide.with(context).load(IMAGE +list.getCategoryImage()).into(holder.img_category);

        holder.txt_bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageChildServiceActivity.class);
                intent.putExtra("Package_id",list.getPackage_id());
                intent.putExtra("Category_name",list.getCategoryName());
                intent.putExtra("Category_id",list.getCategoryId());
                intent.putExtra("unique_id",list.getUniqueId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_packageName,txt_bookNow;
        ImageView img_category;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_packageName = itemView.findViewById(R.id.txt_packageName);
            img_category = itemView.findViewById(R.id.img_category);
            txt_bookNow = itemView.findViewById(R.id.txt_bookNow);

        }
    }
}