package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dailypit.dp.Activity.Address;
import com.dailypit.dp.Activity.UpdateAddress;
import com.dailypit.dp.Interface.DeleteAddressListner;
import com.dailypit.dp.Interface.MakeDefaltAddress;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    DeleteAddressListner deleteAddressListner;
    MakeDefaltAddress makeDefaltAddress;
    Context context;
    List<UserAddressListResponseData> userAddressListResponseData;

    public AddressAdapter(List<UserAddressListResponseData> userAddressListResponseData, Address address, DeleteAddressListner deleteAddressListner, MakeDefaltAddress makeDefaltAddress) {
        this.userAddressListResponseData = userAddressListResponseData;
        this.context = address;
        this.deleteAddressListner = deleteAddressListner;
        this.makeDefaltAddress = makeDefaltAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.address_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserAddressListResponseData myListData = userAddressListResponseData.get(position);

        if(myListData.getDefaults().equals("1")) {
            holder.txt_default.setVisibility(View.VISIBLE);
            holder.cart_AddressLayout.setBackground(context.getResources().getDrawable(R.drawable.address_border));
        }

        if (myListData.getType().equals("Home")){
            holder.imgAddress.setImageResource(R.drawable.ic_bill_total_home);
        } else if(myListData.getType().equals("Work")) {
            holder.imgAddress.setImageResource(R.drawable.address_work);
        } else {
            holder.imgAddress.setImageResource(R.drawable.edit_location);
        }

        holder.txt_addressType.setText(myListData.getType()+" Address");

        holder.txt_address.setText(myListData.getHfNumber()+" "+myListData.getAddress()+" "+myListData.getLandmark()+" "+myListData.getState()+" "+myListData.getPincode());

        holder.txt_editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateAddress.class);
                intent.putExtra("addressId",myListData.getId());
                intent.putExtra("addressType",myListData.getType());
                intent.putExtra("homeNumber",myListData.getHfNumber());
                intent.putExtra("address",myListData.getAddress());
                intent.putExtra("landMark",myListData.getLandmark());
                intent.putExtra("state",myListData.getState());
                intent.putExtra("pinCode",myListData.getPincode());
                intent.putExtra("default",myListData.getDefaults());

                context.startActivity(intent);
            }
        });

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddressListner.deleteId(myListData.getId());
            }
        });

        holder.cart_AddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDefaltAddress.getDefalt(myListData.getId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return userAddressListResponseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAddress,deleteImage;
        public TextView txt_address;
        public TextView txt_addressType;
        public  TextView txt_default;
        public  TextView txt_editAddress;
        public CardView cart_AddressLayout;
        public LinearLayout defaultLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAddress = itemView.findViewById(R.id.imgAddress);
            deleteImage = itemView.findViewById(R.id.deleteImage);
            txt_default = itemView.findViewById(R.id.txt_default);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_editAddress = itemView.findViewById(R.id.txt_editAddress);
            txt_addressType = itemView.findViewById(R.id.txt_addressType);
            cart_AddressLayout = itemView.findViewById(R.id.cart_AddressLayout);
            defaultLayout = itemView.findViewById(R.id.defaultLayout);
        }
    }
}
