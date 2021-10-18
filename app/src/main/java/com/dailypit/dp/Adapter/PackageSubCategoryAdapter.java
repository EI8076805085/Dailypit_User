package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Activity.ProceedPay;
import com.dailypit.dp.Interface.PackageAmountListner;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.Model.Packageg.CategoryResponseData;
import com.dailypit.dp.Model.Payment.PackageCartResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.PackageDB;
import com.dailypit.dp.Utils.YourPreference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class PackageSubCategoryAdapter extends RecyclerView.Adapter<PackageSubCategoryAdapter.ViewHolder>{

    List<CategoryResponseData> myListData;
    Context context;
    PackageAmountListner packageAmountListner;
    PackageDB databaseHandler;
    int finalCharg = 0;
    int total_Item_count = 0;
    int totalPrice = 0;

    public PackageSubCategoryAdapter(List<CategoryResponseData> myListData, Context context,PackageAmountListner packageAmountListner) {
        this.myListData = myListData;
        this.context = context;
        this.packageAmountListner = packageAmountListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.package_child_service_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoryResponseData list = myListData.get(position);
        holder.txt_subCategoryName.setText(list.getSubcatName());
        holder.txt_childName.setText(list.getChildName());
        holder.txt_n_serviceCount.setText("Free\nServices :- "+list.getnServices());
        holder.txt_serviceCharge.setText("\u20B9"+" "+list.getFees());
        Glide.with(context).load(IMAGE +list.getSubcatImage()).into(holder.img_service_poster);

        setUpDB();

        packageAmountListner.getTotalAmount(String.valueOf(finalCharg));

        if(list.getnServices().equalsIgnoreCase("0")) {
            holder.mainCardLayout.setBackground(context.getResources().getDrawable(R.drawable.package_blur_background));
            holder.addbtn.setBackground(context.getResources().getDrawable(R.drawable.grey_background));
            holder.addbtn.setClickable(false);
        }

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.getnServices().equalsIgnoreCase("0")) {

                } else {
                    holder.inc_layout.setVisibility(View.VISIBLE);
                    holder.addbtn.setVisibility(View.GONE);

                    if (databaseHandler.packageInterface().isserviceavailabepackage(list.getChildId())) {
                        Toast.makeText(context, "Id Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        total_Item_count = total_Item_count+1;
                        String fee = list.getFees();
                        holder.txt_itemCount.setText("1");
                        PackageCartResponse packageCartResponse = new PackageCartResponse(list.getChildId(), "" + 1, list.getChildName(), "0", list.getFees(), list.getSubcatId());
                        databaseHandler.packageInterface().addpackagecart(packageCartResponse);
                        databaseHandler.packageInterface().setpackageQty(list.getChildId(), "1", holder.txt_serviceCharge.getText().toString(),""+position);
                        finalCharg = finalCharg + Integer.parseInt(fee);
                        packageAmountListner.getTotalAmount(String.valueOf(finalCharg));
                    }
                }
            }
        });

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = holder.txt_itemCount.getText().toString();
                String id = list.getChildId();
                String fee = list.getFees();

                if (qty.equals(list.getnServices())) {
                    Toast.makeText(context, "You have already added  maximum Services.", Toast.LENGTH_SHORT).show();
                } else {
                    int qty2 = Integer.parseInt(qty) + 1;
                    total_Item_count = total_Item_count+1;

                    holder.txt_itemCount.setText("" + qty2);
                    String itemCount = holder.txt_itemCount.getText().toString();
                    totalPrice = Integer.parseInt(itemCount) * Integer.parseInt(fee);
                    finalCharg = finalCharg + Integer.parseInt(fee);
                    totalPrice = totalPrice + Integer.parseInt(fee);
                    packageAmountListner.getTotalAmount(String.valueOf(finalCharg));
                    databaseHandler.packageInterface().setpackageQty(id, ""+qty2, holder.txt_serviceCharge.getText().toString(), ""+position);
                    SharedPreferences.Editor editor = context.getSharedPreferences("package", MODE_PRIVATE).edit();
                    editor.putInt(id, qty2);
                    editor.putInt(fee, totalPrice);
                    editor.apply();
                }
            }
        });

        holder.txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = holder.txt_itemCount.getText().toString();
                String id = list.getChildId();
                String fee = list.getFees();

                if (qty.equals("0")) {
                    holder.addbtn.setVisibility(View.VISIBLE);
                    holder.inc_layout.setVisibility(View.GONE);
                } else {
                    int qty2 = Integer.parseInt(qty) - 1;
                    holder.txt_itemCount.setText("" + qty2);
                    total_Item_count=total_Item_count-1;

                    if (qty2==0) {
                        holder.addbtn.setVisibility(View.VISIBLE);
                        holder.inc_layout.setVisibility(View.GONE);
                        databaseHandler.packageInterface().deletebypackageid(list.getChildId());
                    }

                    String itemCount = holder.txt_itemCount.getText().toString();
                    totalPrice = Integer.parseInt(itemCount) * Integer.parseInt(fee);
                    finalCharg = finalCharg - Integer.parseInt(fee);
                    totalPrice = totalPrice + Integer.parseInt(fee);

                    if (total_Item_count==1) {
                        List<PackageCartResponse> alldata=  databaseHandler.packageInterface().getallpackagecartdata();
                        String position=alldata.get(0).getS_fees();
                        finalCharg=Integer.parseInt(myListData.get(Integer.parseInt(position)).getFees());
                    } else if (total_Item_count==0) {
                        finalCharg=0;
                    }

                    packageAmountListner.getTotalAmount(String.valueOf(finalCharg));
                    databaseHandler.packageInterface().setpackageQty(id, "" + qty2, holder.txt_serviceCharge.getText().toString(),""+position);
                    SharedPreferences.Editor editor = context.getSharedPreferences("package", MODE_PRIVATE).edit();
                    editor.putInt(id, qty2);
                    editor.putInt(fee, totalPrice);

                    editor.apply();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_service_poster;
        CardView cardView;
        TextView txt_subCategoryName,txt_childName,txt_n_serviceCount,txt_serviceCharge,txt_itemCount,txt_minus, txt_plus;
        LinearLayout addbtn,inc_layout,mainCardLayout,addQqyLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_service_poster = itemView.findViewById(R.id.img_service_poster);
            txt_subCategoryName = itemView.findViewById(R.id.txt_subCategoryName);
            txt_childName = itemView.findViewById(R.id.txt_childName);
            txt_n_serviceCount = itemView.findViewById(R.id.txt_n_serviceCount);
            txt_serviceCharge = itemView.findViewById(R.id.txt_serviceCharge);
            cardView = itemView.findViewById(R.id.cardView);
            addbtn = itemView.findViewById(R.id.addbtn);
            inc_layout = itemView.findViewById(R.id.inc_layout);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txt_plus = itemView.findViewById(R.id.txt_plus);
            mainCardLayout = itemView.findViewById(R.id.mainCardLayout);
            addQqyLayout = itemView.findViewById(R.id.addQqyLayout);

        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(context, PackageDB.class, "package").allowMainThreadQueries().build();
    }
}
