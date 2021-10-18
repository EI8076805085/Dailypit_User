package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.Packageg.PackageService;
import com.dailypit.dp.R;
import java.util.List;

public class PackageSubCategoryListAdapter extends RecyclerView.Adapter<PackageSubCategoryListAdapter.ViewHolder>{

    List<PackageService> listdata;
    Context context;

    public PackageSubCategoryListAdapter(List<PackageService> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.available_package_plan_service_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         final PackageService list = listdata.get(position);
         holder.txt_packageChildName.setText(list.getChildName());
         holder.txt_packageServiceName.setText(list.getSubcatName());
         holder.txt_serviceCount.setText(list.getnServices());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_packageChildName,txt_packageServiceName,txt_serviceCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_packageChildName = itemView.findViewById(R.id.txt_packageChildName);
            txt_packageServiceName = itemView.findViewById(R.id.txt_packageServiceName);
            txt_serviceCount = itemView.findViewById(R.id.txt_serviceCount);

        }
    }
}
