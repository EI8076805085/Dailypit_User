package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.SubCategoryActivity;
import com.dailypit.dp.Model.Service.SubcategoryResponse;
import com.dailypit.dp.R;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder>{
    private SubcategoryResponse[] listdata;
    Context context;

    public SubcategoryAdapter(SubcategoryResponse[] myListData, SubCategoryActivity subCategoryActivity) {
        this.listdata = myListData;
        this.context = subCategoryActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.sub_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubcategoryResponse myListData = listdata[position];
        holder.txt_ServiceName.setText(myListData.getName());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ServiceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_ServiceName = itemView.findViewById(R.id.txt_ServiceName);
        }
    }
}
