package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.AddNewAddress;
import com.dailypit.dp.Activity.MemberShipPlanDetails;
import com.dailypit.dp.Interface.MemberShipPlayListner;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListdata;
import com.dailypit.dp.Model.MemberShip.MembershipResponse;
import com.dailypit.dp.R;

import java.util.ArrayList;
import java.util.List;


public class MemberShipAdapter extends RecyclerView.Adapter<MemberShipAdapter.ViewHolder> {

    List<MemberShipPlanListdata> memberShipPlanListdata ;
    MemberShipPlayListner memberShipPlayListner;
    Context context;
    int lastposition = -1;
    String value;

    public MemberShipAdapter(List<MemberShipPlanListdata> memberShipPlanListdata, String value, Context context ,  MemberShipPlayListner memberShipPlayListner) {
        this.memberShipPlanListdata = memberShipPlanListdata;
        this.memberShipPlayListner = memberShipPlayListner;
        this.context = context;
        this.value = value;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.membership_plan_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MemberShipPlanListdata myListData = memberShipPlanListdata.get(position);
        holder.txt_plan.setText(myListData.getValidity());
        holder.txt_planPrice.setText("Rs."+myListData.getMembershipAmount());

//        holder.mainCardLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               memberShipPlayListner.planListner(myListData.getId(),myListData.getMembershipAmount());
//            }
//        });

        if(myListData.getRecommendation().equalsIgnoreCase("Yes")){
            holder.txt_recomanded.setVisibility(View.VISIBLE);
        }

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastposition=position;
                notifyDataSetChanged();
            }
        });

        holder.mainCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(context, MemberShipPlanDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("validity",myListData.getValidity());
                    intent.putExtra("planCharg",myListData.getMembershipAmount());
                    intent.putExtra("planId",myListData.getId());

                    context.startActivity(intent);
                }
            }
        });

        if(lastposition==position){
            holder.addToCart.setCardBackgroundColor(Color.parseColor("#FFB300"));
            holder.txt_addToCart.setText("Added");
            holder.img_cart.setImageResource(R.drawable.ic_icon_black_circle);
            memberShipPlayListner.planListner(myListData.getId(),myListData.getValidity(),myListData.getMembershipAmount());
        } else {
            holder.addToCart.setCardBackgroundColor(Color.parseColor("#2AC5C4"));
            holder.txt_addToCart.setText("Add to cart");
            holder.img_cart.setImageResource(R.drawable.ic_ios_cart);
        }

    }

    @Override
    public int getItemCount() {
        return memberShipPlanListdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_plan,txt_addToCart,txt_planPrice,txt_recomanded;
        LinearLayout mainCardLayout;
        CardView addToCart;
        ImageView img_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_plan = itemView.findViewById(R.id.txt_plan);
            mainCardLayout = itemView.findViewById(R.id.mainCardLayout);
            txt_addToCart = itemView.findViewById(R.id.txt_addToCart);
            addToCart = itemView.findViewById(R.id.addToCart);
            img_cart = itemView.findViewById(R.id.img_cart);
            txt_planPrice = itemView.findViewById(R.id.txt_planPrice);
            txt_recomanded = itemView.findViewById(R.id.txt_recomanded);

        }
    }
}
