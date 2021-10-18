package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Interface.ChildCategoryListner;
import com.dailypit.dp.Interface.ItemQuantityListner;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    List<CartResponse> cartResponses;
    Context context;
    ChildCategoryListner childCategoryListner;
    ItemQuantityListner itemQuantityListner;

    public CartAdapter(List<CartResponse> alldata, Context context,ChildCategoryListner childCategoryListner,ItemQuantityListner itemQuantityListner) {
        this.cartResponses = alldata;
        this.context = context;
        this.childCategoryListner = childCategoryListner;
        this.itemQuantityListner = itemQuantityListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_list, parent, false);
         ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        final CartResponse myListData = cartResponses.get(position);
        holder.serviceName.setText(myListData.getS_name());
        holder.txt_itemCount.setText(myListData.getQty());
        holder.txt_servicePrice.setText(myListData.getS_total());
        childCategoryListner.getCategory(myListData.getS_id(),myListData.getS_name());
        itemQuantityListner.getQuantity(myListData.getQty());

    }

    @Override
    public int getItemCount() {
        return cartResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName,txt_itemCount,txt_servicePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.serviceName);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            txt_servicePrice = itemView.findViewById(R.id.txt_servicePrice);
        }
    }
}
