package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponse;
import com.dailypit.dp.Model.Packageg.PackagePlanlistResponseData;
import com.dailypit.dp.R;

import java.util.List;

public class PackagePlanListAdapter extends RecyclerView.Adapter<PackagePlanListAdapter.ViewHolder>{

    List<PackagePlanlistResponseData> myListData;
    private List<String> listdata;

    Context context;

    public PackagePlanListAdapter(List<String> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.membership_service_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // final PackagePlanlistResponseData list = myListData.get(position);
        holder.txt_servicePlan.setText(listdata.get(position));
       
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_servicePlan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_servicePlan = itemView.findViewById(R.id.txt_servicePlan);

        }
    }
}
