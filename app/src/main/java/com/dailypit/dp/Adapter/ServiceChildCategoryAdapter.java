package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.dailypit.dp.Interface.ChildCategoryListner;
import com.dailypit.dp.Interface.ServiceItemChargListner;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.Model.Service.ServicesChildCategoryData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.YourPreference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServiceChildCategoryAdapter extends RecyclerView.Adapter<ServiceChildCategoryAdapter.ViewHolder> {

    List<ServicesChildCategoryData> servicesChildCategoryList;
    Context context;
    ServiceItemChargListner serviceItemChargListner;
    ChildCategoryListner childCategoryListner;
    int totalPrice = 0;
    DatabaseHandler databaseHandler;
    int finalCharg = 0;
    int totalDiscount = 0;
    int total_Item_count = 0;
    int minvalue = 0;
    String discount = "0",discountType,multiCoupon;

    public ServiceChildCategoryAdapter(List<ServicesChildCategoryData> serviceChildCategoryList, Context context, ServiceItemChargListner serviceItemChargListner, String multiCoupon, String discount, String discountType, int minvalue, ChildCategoryListner childCategoryListner) {
        this.servicesChildCategoryList = serviceChildCategoryList;
        this.context = context;
        this.serviceItemChargListner = serviceItemChargListner;
        this.childCategoryListner = childCategoryListner;
        this.discount = discount;
        this.discountType =discountType;
        this.multiCoupon = multiCoupon;
        this.minvalue = minvalue;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.service_child_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceChildCategoryAdapter.ViewHolder holder, int position) {
        final ServicesChildCategoryData myListData = servicesChildCategoryList.get(position);

        setUpDB();

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String multiCoupon = yourPrefrence.getData("multiCoupon");
//      String discount = yourPrefrence.getData("discount");
//      String discountType = yourPrefrence.getData("discountType");

        holder.txt_serviceName.setText(myListData.getName());
        holder.txt_serviceCharge.setText("\u20B9" + " " +myListData.getFees());
        serviceItemChargListner.serviceItemClick(String.valueOf(finalCharg),""+totalDiscount);

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.inc_layout.setVisibility(View.VISIBLE);
                holder.addbtn.setVisibility(View.GONE);
                childCategoryListner.getCategory(myListData.getId(),myListData.getName());
                if (databaseHandler.cartInterface().isserviceavailabe(myListData.getId())) {
                    Toast.makeText(context, "Id Exist", Toast.LENGTH_SHORT).show();
                } else {
                    total_Item_count=total_Item_count+1;
                    String fee = myListData.getFees();
                    if (total_Item_count>1) {
                        if (multiCoupon.equalsIgnoreCase("1")) {
                            if (discountType.equalsIgnoreCase("percentage")) {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                int discountnumber = Integer.parseInt(discount);
                                int discount = (feedouble* discountnumber/ 100);
                                totalDiscount = totalDiscount + discount;
                                fee = String.valueOf(Integer.parseInt(fee) - discount);
                            } else {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                totalDiscount = totalDiscount + Integer.parseInt(discount);
                                 fee = String.valueOf(Integer.parseInt(fee) - Integer.parseInt(discount));
                            }
                        }
                    }

                    holder.txt_itemCount.setText("1");
                    CartResponse cartResponse = new CartResponse(myListData.getId(), "" + 1, myListData.getName(), "0", myListData.getFees());
                    databaseHandler.cartInterface().addcart(cartResponse);
//                    serviceItemChargListner.serviceItemClick(String.valueOf(finalCharg),""+totalDiscount);
                    databaseHandler.cartInterface().setQty(myListData.getId(), "1", holder.txt_serviceCharge.getText().toString(),""+position);
                    finalCharg = finalCharg + Integer.parseInt(fee);
                    totalPrice = totalPrice + Integer.parseInt(fee);
                    serviceItemChargListner.serviceItemClick(String.valueOf(finalCharg),""+totalDiscount);
                }
            }
        });

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = holder.txt_itemCount.getText().toString();
                String id = myListData.getId();
                String fee = myListData.getFees();

                if (qty.equals("15")) {

                } else {
                    int qty2 = Integer.parseInt(qty) + 1;
                    total_Item_count=total_Item_count+1;

                    if (total_Item_count>1) {
                        if (multiCoupon.equalsIgnoreCase("1")) {
                            if (discountType.equalsIgnoreCase("percentage")) {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                int discountnumber = Integer.parseInt(discount);
                                int discount = (feedouble* discountnumber/ 100);
                                totalDiscount = totalDiscount + discount;
                                fee = String.valueOf(Integer.parseInt(fee) - discount);
                            }
                            else
                            {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                totalDiscount = totalDiscount + Integer.parseInt(discount);
                                fee = String.valueOf(Integer.parseInt(fee) - Integer.parseInt(discount));
                            }
                        }
                    }

                    holder.txt_itemCount.setText("" + qty2);
                    String itemCount = holder.txt_itemCount.getText().toString();
                    totalPrice = Integer.parseInt(itemCount) * Integer.parseInt(fee);
                    finalCharg = finalCharg + Integer.parseInt(fee);
                    totalPrice = totalPrice + Integer.parseInt(fee);
                    totalDiscount = totalDiscount + 0;

                    if (total_Item_count<2)
                    {
                        totalDiscount=0;
                    }

                    serviceItemChargListner.serviceItemClick(String.valueOf(finalCharg),""+totalDiscount);
                    databaseHandler.cartInterface().setQty(id, ""+qty2, holder.txt_serviceCharge.getText().toString(), ""+position);
                    SharedPreferences.Editor editor = context.getSharedPreferences("cart", MODE_PRIVATE).edit();
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
                String id = myListData.getId();
                String fee = myListData.getFees();
                if (qty.equals("0")) {
                    holder.addbtn.setVisibility(View.VISIBLE);
                    holder.inc_layout.setVisibility(View.GONE);
                } else {
                    int qty2 = Integer.parseInt(qty) - 1;
                    holder.txt_itemCount.setText("" + qty2);
                    total_Item_count=total_Item_count-1;
                    if (total_Item_count>0) {
                        if (multiCoupon.equalsIgnoreCase("1")) {
                            if (discountType.equalsIgnoreCase("percentage")) {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                int discountnumber = Integer.parseInt(discount);
                                int discount = (feedouble* discountnumber/ 100);
                                totalDiscount = totalDiscount - discount;
                                fee = String.valueOf(Integer.parseInt(fee) - discount);
                            }
                            else
                            {
                                int feedouble = minvalue;//Integer.parseInt(fee);
                                totalDiscount = totalDiscount - Integer.parseInt(discount);
                                fee = String.valueOf(Integer.parseInt(fee) - Integer.parseInt(discount));
                            }
                        }
                    }

                    if (qty2==0)
                    {
                        holder.addbtn.setVisibility(View.VISIBLE);
                        holder.inc_layout.setVisibility(View.GONE);
                        databaseHandler.cartInterface().deletebyid(myListData.getId());
                    }

                    String itemCount = holder.txt_itemCount.getText().toString();
                    totalPrice = Integer.parseInt(itemCount) * Integer.parseInt(fee);
                    finalCharg = finalCharg - Integer.parseInt(fee);
                    totalPrice = totalPrice + Integer.parseInt(fee);
                    totalDiscount = totalDiscount + 0;

                    if (total_Item_count==1)
                    {
                        totalDiscount=0;
                        List<CartResponse> alldata=  databaseHandler.cartInterface().getallcartdata();
                        String position=alldata.get(0).getS_fees();
                        finalCharg=Integer.parseInt(servicesChildCategoryList.get(Integer.parseInt(position)).getFees());

                    }
                    else if (total_Item_count==0)
                    {
                        totalDiscount=0;
                        finalCharg=0;
                    }

                    serviceItemChargListner.serviceItemClick(String.valueOf(finalCharg),""+totalDiscount);
                    databaseHandler.cartInterface().setQty(id, "" + qty2, holder.txt_serviceCharge.getText().toString(),""+position);
                    SharedPreferences.Editor editor = context.getSharedPreferences("cart", MODE_PRIVATE).edit();
                    editor.putInt(id, qty2);
                    editor.putInt(fee, totalPrice);

                    editor.apply();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesChildCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_serviceName, txt_serviceCharge, txt_minus, txt_plus, txt_itemCount;
        LinearLayout addbtn, inc_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_serviceName = itemView.findViewById(R.id.txt_serviceName);
            txt_serviceCharge = itemView.findViewById(R.id.txt_serviceCharge);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txt_plus = itemView.findViewById(R.id.txt_plus);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            addbtn = itemView.findViewById(R.id.addbtn);
            inc_layout = itemView.findViewById(R.id.inc_layout);

        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(context, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }
}
