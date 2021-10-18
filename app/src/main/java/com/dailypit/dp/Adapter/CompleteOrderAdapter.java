package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.OrderAssignTo;
import com.dailypit.dp.Activity.WebviewActivity;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponse;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData;
import com.dailypit.dp.Model.OrderStatus.CompleteOrderResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.ViewHolder>{
    List<CompleteOrderResponseData> completeOrderList;
    List<AddOnServiceResponseData> addOnServiceResponseData = new ArrayList<>();
    Context context;
    ProgressBar progressBar;

    public CompleteOrderAdapter(FragmentActivity activity, List<CompleteOrderResponseData> completeOrderList, ProgressBar progressBar) {
        this.context = activity;
        this.completeOrderList = completeOrderList;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.complete_order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CompleteOrderResponseData myListData = completeOrderList.get(position);
        holder.txt_orderId.setText("Order id: "+myListData.getId());
        holder.txt_serviceName.setText(myListData.getCategoryName() );
        holder.txt_date.setText(myListData.getOrderDate());
        holder.txt_time.setText(myListData.getTimeSlot());
        holder.txt_totalMoney.setText("\u20B9 "+myListData.getTotalAmount());
        holder.txt_appliedCouponName.setText(myListData.getCouponCode());
        holder.txt_discountMoney.setText("\u20B9 "+" -"+myListData.getDiscountAmount());
        holder.txt_TotalAmount.setText("\u20B9 "+myListData.getNetAmount());
        holder.txt_address.setText(myListData.getAddress());
        holder.txt_addressType.setText(myListData.getAdressType());
        holder.txt_paymentmodeStatus.setText(myListData.getPaymentMode());

        getAddONServices(myListData.getId(),holder.add_onServicesLayout,holder.txt_service_discountMoney,holder.txt_addnewServicetotalAMount,holder.add_newServiceRecyclerView,holder.txt_paymentmode);


        List<String> quantityList = Arrays.asList(myListData.getQuantity().split(","));
        int sum = 0;
        for(String value: quantityList){
            sum += Integer.parseInt(value);
        }

        holder.txt_totalItemCount.setText(sum+"");

        List<String> itemNumberParentList = Arrays.asList(myListData.getQuantity());
        List<String> itemNameParentList = Arrays.asList(myListData.getChildCategoryName());

        String totalValue = "";
        for (int i = 0; i < itemNameParentList.size(); i++) {
            List<String> itemNumberList = Arrays.asList(itemNumberParentList.get(i).split(","));
            List<String> itemNameList = Arrays.asList(itemNameParentList.get(i).split(","));
            for (int j = 0; j < itemNumberList.size(); j++) {

                if (j==itemNumberList.size()-1)
                {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n";

                }
                else {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n\n⦿";
                }
            }
        }

        holder.txt_serviceSubName.setText("⦿ "+totalValue);

        holder.img_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInvoice(myListData.getId());
            }
        });


    }


    @Override
    public int getItemCount() {
        return completeOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_orderId,txt_serviceName,txt_serviceSubName,txt_date,txt_time,
                txt_totalItemCount,txt_totalMoney,txt_appliedCouponName,txt_discountMoney,
                txt_TotalAmount,txt_address,txt_addressType,txt_service_discountMoney,txt_addnewServicetotalAMount,txt_paymentmode,txt_paymentmodeStatus;
        ImageView img_invoice;
        LinearLayout add_onServicesLayout;
        RecyclerView add_newServiceRecyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_orderId = itemView.findViewById(R.id.txt_orderId);
            txt_serviceName = itemView.findViewById(R.id.txt_serviceName);
            txt_serviceSubName = itemView.findViewById(R.id.txt_serviceSubName);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_totalItemCount = itemView.findViewById(R.id.txt_totalItemCount);
            txt_totalMoney = itemView.findViewById(R.id.txt_totalMoney);
            txt_appliedCouponName = itemView.findViewById(R.id.txt_appliedCouponName);
            txt_discountMoney = itemView.findViewById(R.id.txt_discountMoney);
            txt_TotalAmount = itemView.findViewById(R.id.txt_TotalAmount);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_addressType = itemView.findViewById(R.id.txt_addressType);
            img_invoice = itemView.findViewById(R.id.img_invoice);
            add_onServicesLayout = itemView.findViewById(R.id.add_onServicesLayout);
            txt_service_discountMoney = itemView.findViewById(R.id.txt_service_discountMoney);
            txt_addnewServicetotalAMount = itemView.findViewById(R.id.txt_addnewServicetotalAMount);
            add_newServiceRecyclerView = itemView.findViewById(R.id.add_newServiceRecyclerView);
            txt_paymentmode = itemView.findViewById(R.id.txt_paymentmode);
            txt_paymentmodeStatus = itemView.findViewById(R.id.txt_paymentmodeStatus);

        }
    }

    private void getInvoice(String order_id) {
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra("order_id", order_id);
        context.startActivity(intent);

    }

    private void getAddONServices(String orderId, LinearLayout add_onServicesLayout, TextView txt_service_discountMoney, TextView txt_addnewServicetotalAMount, RecyclerView add_newServiceRecyclerView,TextView txt_paymentmode) {
        addOnServiceResponseData.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("p_order_id", orderId);
        progressBar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<AddOnServiceResponse> call = serviceInterface.addNewServicesCnfed(map);
        call.enqueue(new Callback<AddOnServiceResponse>() {
            @Override
            public void onResponse(Call<AddOnServiceResponse> call, retrofit2.Response<AddOnServiceResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        addOnServiceResponseData = response.body().getData();
                        bindData(addOnServiceResponseData, response.body().getDiscountAmount(), response.body().getNetAmount(),orderId,response.body().getAddonpaymentmode(),response.body().getAddonpaymentstatus(),add_onServicesLayout,txt_service_discountMoney,txt_addnewServicetotalAMount,add_newServiceRecyclerView,txt_paymentmode);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddOnServiceResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("ff", t.toString());

            }
        });
    }

    private void bindData(List<AddOnServiceResponseData> addOnServiceResponseData, String discountAmount, String netAmount, String orderId, String addonPaymentmode, String addonPaymentstatus, LinearLayout add_onServicesLayout, TextView txt_service_discountMoney, TextView txt_addnewServicetotalAMount, RecyclerView add_newServiceRecyclerView,TextView txt_paymentmode) {
        add_onServicesLayout.setVisibility(View.VISIBLE);
        txt_paymentmode.setText(addonPaymentmode);
        txt_service_discountMoney.setText("\u20B9 "+discountAmount);
        txt_addnewServicetotalAMount.setText("\u20B9 "+netAmount);
        AddOnServiceAdapter adapter = new AddOnServiceAdapter(addOnServiceResponseData);
        add_newServiceRecyclerView.setHasFixedSize(true);
        add_newServiceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        add_newServiceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



}
