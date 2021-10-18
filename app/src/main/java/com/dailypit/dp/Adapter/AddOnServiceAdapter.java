package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData;
import com.dailypit.dp.R;
import java.util.List;

public class AddOnServiceAdapter extends RecyclerView.Adapter<AddOnServiceAdapter.ViewHolder> {
    List<AddOnServiceResponseData> addOnServiceResponseData;


    public AddOnServiceAdapter(List<AddOnServiceResponseData> addOnServiceResponseData) {
        this.addOnServiceResponseData = addOnServiceResponseData;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.add_new_services_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddOnServiceResponseData myListData = addOnServiceResponseData.get(position);
        holder.txt_serviceName.setText(myListData.getSubCategoryName());
        holder.txt_serviceChildCategoryName.setText(myListData.getChildCategoryName());
        holder.txt_count.setText(myListData.getQuantity());
        holder.txt_Money.setText("\u20B9 "+myListData.getAmount());

    }

    @Override
    public int getItemCount() {
        return addOnServiceResponseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_serviceName, txt_count, txt_Money, txt_serviceChildCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_serviceName = itemView.findViewById(R.id.txt_serviceName);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_Money = itemView.findViewById(R.id.txt_Money);
            txt_serviceChildCategoryName = itemView.findViewById(R.id.txt_serviceChildCategoryName);

        }
    }
}
