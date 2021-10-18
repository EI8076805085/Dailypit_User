package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsSubData;
import com.dailypit.dp.R;

import java.util.List;

public class PlanFullDetailsAdapter extends RecyclerView.Adapter<PlanFullDetailsAdapter.ViewHolder> {
    List<PlanDetailsSubData> planDetailsSubDataList;

    public PlanFullDetailsAdapter(List<PlanDetailsSubData> planDetailsSubDataList, Context applicationContext) {
        this.planDetailsSubDataList = planDetailsSubDataList;
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
        final PlanDetailsSubData myListData = planDetailsSubDataList.get(position);
        holder.txt_servicePlan.setText(myListData.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return planDetailsSubDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_servicePlan;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_servicePlan = itemView.findViewById(R.id.txt_servicePlan);

        }
    }
}

