package com.dailypit.dp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.Wallet.WalletTransactionData;
import com.dailypit.dp.R;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    List<WalletTransactionData> listdata;

    public WalletAdapter(List<WalletTransactionData> myListData) {
        this.listdata = myListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.wallet_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WalletTransactionData myListData = listdata.get(position);

        if(myListData.getType().equals("add")){
            holder.txt_amount.setText("\u20B9 " + myListData.getAmount());
            holder.txt_fileName.setText("Cashback Added for Order Id"+ myListData.getOrderId() +" in your Wallet");
            holder.txt_amount.setTextColor(Color.parseColor("#68C36C"));
        } else {
            holder.txt_fileName.setText("Points Deducted for Order Id "+myListData.getOrderId()+" Placed");
            holder.txt_amount.setText("\u20B9 -"+ myListData.getAmount());
            holder.txt_amount.setTextColor(Color.parseColor("#ff0000"));
        }

        holder.txt_date.setText(myListData.getCreatedDate());
        holder.txt_time.setText(myListData.getCreatedTime());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_fileName,txt_amount,txt_date,txt_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_fileName = itemView.findViewById(R.id.txt_fileName);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);

        }
    }

}
