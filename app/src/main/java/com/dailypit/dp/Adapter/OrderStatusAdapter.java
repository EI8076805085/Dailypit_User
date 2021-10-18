package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Activity.OrderAssignTo;
import com.dailypit.dp.Activity.OrderPlaced;
import com.dailypit.dp.Model.OrderStatus.OngoingOrderResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.Helper;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>{
    List<OngoingOrderResponseData> ongoingOrderList;
    Context context;

    public OrderStatusAdapter(List<OngoingOrderResponseData> ongoingOrderList, Context context) {
        this.ongoingOrderList = ongoingOrderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.ongoing_order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OngoingOrderResponseData myListData = ongoingOrderList.get(position);
        holder.txt_discription.setText(myListData.getCategoryName());
        holder.txt_money.setText("\u20B9 "+myListData.getNetAmount());
        holder.txt_date.setText(myListData.getDate());
        holder.txt_time.setText(myListData.getTime());
        holder.txt_orderID.setText("Order Id:  "+myListData.getId());

        holder.txt_orderStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = myListData.getOrderStatus();
                String add_Type = myListData.getAddressType();

                if(add_Type != null) {
                    if(status.equals("Order Placed")) {
                        Intent intent = new Intent(context, OrderPlaced.class);
                        intent.putExtra("address_type",myListData.getAddressType());
                        intent.putExtra("address",myListData.getAddress());
                        intent.putExtra("time",myListData.getTimeSlot());
                        intent.putExtra("date",myListData.getOrderDate());
                        intent.putExtra("net_amount",myListData.getNetAmount());
                        intent.putExtra("order_id",myListData.getId());
                        intent.putExtra("child_cate_name",myListData.getChildCategoryName());
                        intent.putExtra("service_cate_name",myListData.getSubCategoryName());
                        intent.putExtra("quentity",myListData.getQuantity());
                        intent.putExtra("payment_mode",myListData.getPaymentMode());
                        intent.putExtra("payment_Status",myListData.getPaymentStatus());
                        intent.putExtra("category",myListData.getCategoryName());
                        intent.putExtra("addressPincode",myListData.getAddressPincode());

                        context.startActivity(intent);

                    } else if(status.equals("Order Assigned")) {
                        Intent intent = new Intent(context, OrderAssignTo.class);
                        intent.putExtra("address_type",myListData.getAddressType());
                        intent.putExtra("address",myListData.getAddress());
                        intent.putExtra("total_amount",myListData.getTotalAmount());
                        intent.putExtra("discount_count",myListData.getDiscountAmount());
                        intent.putExtra("net_amount",myListData.getNetAmount());
                        intent.putExtra("order_id",myListData.getId());
                        intent.putExtra("date",myListData.getDate());
                        intent.putExtra("time",myListData.getTime());
                        intent.putExtra("coupon_name",myListData.getCouponCode());
                        intent.putExtra("quentity",myListData.getQuantity());
                        intent.putExtra("child_cate_name",myListData.getChildCategoryName());
                        intent.putExtra("service_cate_name",myListData.getSubCategoryName());
                        intent.putExtra("partner_name",myListData.getPartnerName());
                        intent.putExtra("partner_mobile",myListData.getPartnerMobile());
                        intent.putExtra("partner_photo",myListData.getPartnerPhoto());
                        intent.putExtra("order_date",myListData.getOrderDate());
                        intent.putExtra("order_time_slot",myListData.getTimeSlot());
                        intent.putExtra("payment_Status",myListData.getPaymentStatus());
                        intent.putExtra("temprature",myListData.getTemprature());
                        intent.putExtra("category",myListData.getCategoryName());
                        intent.putExtra("addressPincode",myListData.getAddressPincode());
                        intent.putExtra("c_code",myListData.getC_code());
                        intent.putExtra("payment_mode",myListData.getPaymentMode());
                        intent.putExtra("c_code_verification",myListData.getC_code_verification());

                        context.startActivity(intent);
                    }
                } else {
                    Helper.INSTANCE.Error(context, context.getString(R.string.delete_address));
                }
            }
        });

        if(myListData.getOrderStatus().equals("Order Placed")){
            holder.txt_status.setText(myListData.getOrderStatus());
        } else if(myListData.getOrderStatus().equals("Order Assigned")){
            holder.txt_status.setTextColor(Color.parseColor("#D6BB32"));
            holder.txt_status.setText(myListData.getOrderStatus());
        } else {
            holder.txt_status.setTextColor(Color.parseColor("#5FBD54"));
            holder.txt_status.setText(myListData.getOrderStatus());
            holder.txt_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ongoingOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_discription,txt_money,txt_date,txt_time,txt_status,txt_orderID;
        LinearLayout txt_orderStatusLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_discription = itemView.findViewById(R.id.txt_discription);
            txt_money = itemView.findViewById(R.id.txt_money);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_orderStatusLayout = itemView.findViewById(R.id.txt_orderStatusLayout);
            txt_orderID = itemView.findViewById(R.id.txt_orderID);

        }
    }
}
