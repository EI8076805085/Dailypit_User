package com.dailypit.dp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.ApplyCoupon;
import com.dailypit.dp.Activity.ProceedPay;
import com.dailypit.dp.Interface.ApplyCouponListner;
import com.dailypit.dp.Model.Coupon.CouponResponseData;
import com.dailypit.dp.R;

import java.util.List;

public class ApplyCouponAdapter extends RecyclerView.Adapter<ApplyCouponAdapter.ViewHolder>{

    List<CouponResponseData> couponList;
    ApplyCouponListner applyCouponListner;
    Context context;

    public ApplyCouponAdapter(List<CouponResponseData> couponList, ApplyCoupon couponListner, ApplyCouponListner applyCouponListner) {
        this.couponList = couponList;
        this.context = couponListner;
        this.applyCouponListner = applyCouponListner;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.coupon_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CouponResponseData myListData = couponList.get(position);
        holder.txt_couponName.setText(myListData.getCouponCode());
        holder.txt_couponDiscription.setText(HtmlCompat.fromHtml(myListData.getDescriptions(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.txt_applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProceedPay.class);
                intent.putExtra("flag","1");
                intent.putExtra("coupon_id",myListData.getId());
                intent.putExtra("coupon_code",myListData.getCouponCode());
                intent.putExtra("coupon_discription",myListData.getDescriptions());
                intent.putExtra("coupon_discount",myListData.getDiscount());
                intent.putExtra("discount_typee",myListData.getDiscount_type());
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_couponName,txt_couponDiscription,txt_applyCoupon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_couponName = itemView.findViewById(R.id.txt_couponName);
            txt_couponDiscription = itemView.findViewById(R.id.txt_couponDiscription);
            txt_applyCoupon = itemView.findViewById(R.id.txt_applyCoupon);

        }
    }
}
