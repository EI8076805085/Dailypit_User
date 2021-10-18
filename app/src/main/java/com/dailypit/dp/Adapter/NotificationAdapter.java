package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Model.Notification.NotificationResponseData;
import com.dailypit.dp.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    List<NotificationResponseData> notificationList;
    Context context;

    public NotificationAdapter(List<NotificationResponseData> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context =context;
    }


    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.notification_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        final NotificationResponseData myListData = notificationList.get(position);
        holder.txt_notification.setText(myListData.getDescription());
        holder.txt_notificationType.setText(myListData.getTitle());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_notification,txt_notificationType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_notification = itemView.findViewById(R.id.txt_notification);
            txt_notificationType = itemView.findViewById(R.id.txt_notificationType);

        }
    }
}
