package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.MemberShipPlanDetails;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsSubData;
import com.dailypit.dp.R;

import java.util.ArrayList;
import java.util.List;

public class PlanDetailsAdapter extends RecyclerView.Adapter<PlanDetailsAdapter.ViewHolder> {
    List<PlanDetailsData> planDetailsData;
    List<PlanDetailsSubData> planDetailsSubDataList = new ArrayList<>();

    Context context;

    public PlanDetailsAdapter(List<PlanDetailsData> planDetailsData, Context context) {
        this.planDetailsData = planDetailsData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.membership_plan_listdata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PlanDetailsData myListData = planDetailsData.get(position);
        holder.txt_servicePlan.setText(myListData.getParentName());
        holder.txt_servicePlandiscount.setText("SAVE "+myListData.getDiscountAmount()+ " " +myListData.getDiscountType()+" ON");

        planDetailsSubDataList = myListData.getCategoryData();

        PlanFullDetailsAdapter adapter = new PlanFullDetailsAdapter(planDetailsSubDataList,context);
        holder.planDetailsRecyclerView.setHasFixedSize(true);
        holder.planDetailsRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.planDetailsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return planDetailsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_servicePlan,txt_servicePlandiscount;
        RecyclerView planDetailsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_servicePlan = itemView.findViewById(R.id.txt_servicePlan);
            txt_servicePlandiscount = itemView.findViewById(R.id.txt_servicePlandiscount);
            planDetailsRecyclerView = itemView.findViewById(R.id.planDetailsRecyclerView);


        }
    }
}
