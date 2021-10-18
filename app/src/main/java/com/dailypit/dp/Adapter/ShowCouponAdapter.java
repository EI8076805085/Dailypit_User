package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Activity.ShowCoupon;
import com.dailypit.dp.Model.Coupon.CouponResponseData;
import com.dailypit.dp.R;

import java.util.List;

public class ShowCouponAdapter extends RecyclerView.Adapter<ShowCouponAdapter.ViewHolder>{

    List<CouponResponseData> couponList;
    Context context;

    public ShowCouponAdapter(List<CouponResponseData> couponList, ShowCoupon couponListner) {
        this.couponList = couponList;
        this.context = couponListner;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.show_coupon_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CouponResponseData myListData = couponList.get(position);
        holder.txt_couponName.setText(myListData.getCouponCode());
        holder.txt_couponDiscription.setText(HtmlCompat.fromHtml(myListData.getDescriptions(), HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_couponName,txt_couponDiscription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_couponName = itemView.findViewById(R.id.txt_couponName);
            txt_couponDiscription = itemView.findViewById(R.id.txt_couponDiscription);

        }
    }
}
