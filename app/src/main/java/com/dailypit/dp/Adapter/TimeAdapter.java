package com.dailypit.dp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Interface.SelectTimeListner;
import com.dailypit.dp.R;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{

    List<String> statelist;
    SelectTimeListner stateListner;
    Context context;
    Dialog dialog;

    public TimeAdapter(List<String> statelist, Context context, SelectTimeListner stateListner, Dialog dialog) {
        this.statelist = statelist;
        this.context = context;
        this.stateListner = stateListner;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.state_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_State.setText(statelist.get(position));
        String name = statelist.get(position);
        holder.stateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateListner.timeClickListner(name);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return statelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_State;
        LinearLayout stateLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_State = itemView.findViewById(R.id.txt_State);
            stateLayout = itemView.findViewById(R.id.stateLayout);
        }
    }
}
