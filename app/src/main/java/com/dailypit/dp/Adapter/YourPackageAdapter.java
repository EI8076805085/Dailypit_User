package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.Packageg.CheckPackageResponseData;
import com.dailypit.dp.Model.Packageg.PackageCategory;
import com.dailypit.dp.R;
import java.util.ArrayList;
import java.util.List;

public class YourPackageAdapter extends RecyclerView.Adapter<YourPackageAdapter.ViewHolder>{

    List<CheckPackageResponseData> myListData;
    List<PackageCategory> packageCategories = new ArrayList<>();
    Context context;

    public YourPackageAdapter(List<CheckPackageResponseData> myListData, Context context) {
        this.myListData = myListData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.package_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CheckPackageResponseData list = myListData.get(position);
        holder.txt_packageName.setText(list.getPackage_name() +" Package");
        packageCategories = list.getCategoryData();

        PackageListCategoryAdapter adapter = new PackageListCategoryAdapter(packageCategories, (FragmentActivity) context);
        holder.yourpackageRecyclerView.setHasFixedSize(true);
        holder.yourpackageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.yourpackageRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_packageName;
        RecyclerView yourpackageRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_packageName = itemView.findViewById(R.id.txt_packageName);
            yourpackageRecyclerView = itemView.findViewById(R.id.yourpackageRecyclerView);

        }
    }
}
