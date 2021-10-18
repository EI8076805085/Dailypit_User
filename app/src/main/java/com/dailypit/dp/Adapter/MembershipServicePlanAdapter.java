package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Model.MemberShip.MembershipResponse;
import com.dailypit.dp.R;

import java.util.List;

public class MembershipServicePlanAdapter extends RecyclerView.Adapter<MembershipServicePlanAdapter.ViewHolder> {

    private List<String> listdata;

    public MembershipServicePlanAdapter(List<String> listdata, Context applicationContext) {
        this.listdata = listdata;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.member_service_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
