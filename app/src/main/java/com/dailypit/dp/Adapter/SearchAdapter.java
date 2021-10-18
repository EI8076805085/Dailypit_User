package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Activity.ServiceAcitvity;
import com.dailypit.dp.Model.Service.ServiceSubCategoryResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    ArrayList<ServiceSubCategoryResponseData> serviceSubCategoryList;
    private ArrayList<ServiceSubCategoryResponseData> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();

    Context context;
    String category;

    public SearchAdapter(ArrayList<ServiceSubCategoryResponseData> serviceSubCategoryList, Context context) {
        this.serviceSubCategoryList = serviceSubCategoryList;
        this.context = context;
        this.filteredData=serviceSubCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.sub_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServiceSubCategoryResponseData myListData = filteredData.get(position);

        holder.txt_ServiceName.setText(myListData.getName());
        holder.txt_serviceDiscription.setText(HtmlCompat.fromHtml(myListData.getDescriptions(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        holder.txt_serviceChildCategory.setText(myListData.getChildData());
        holder.img_favorite.setVisibility(View.GONE);

        if (myListData.getMultiCoupon().equals("1")){
            holder.img_coupon.setImageResource(R.drawable.coupon);
        }

        Glide.with(context).load(IMAGE +myListData.getImage()).into(holder.img_Category);

        holder.txt_serviceSubCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);
                yourPrefrence.saveData("subCategoryID",myListData.getId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Category_Name",category);
                intent.putExtra("Service_Name",myListData.getName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());
                intent.putExtra("flag","0");
                context.startActivity(intent);

            }
        });

        holder.txt_imgserviceSubCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ServiceAcitvity.class);
                YourPreference yourPrefrence = YourPreference.getInstance(context);
                yourPrefrence.saveData("subCategoryID",myListData.getId());
                yourPrefrence.saveData("multiCoupon",myListData.getMultiCoupon());
                yourPrefrence.saveData("discount",myListData.getDiscount());
                yourPrefrence.saveData("discountType",myListData.getDiscountType());
                intent.putExtra("Category_Name",category);
                intent.putExtra("Service_Name",myListData.getName());
                intent.putExtra("Service_Image",myListData.getImage());
                intent.putExtra("service_discription",myListData.getDescriptions());
                intent.putExtra("flag","0");
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ServiceName,txt_serviceChildCategory,txt_serviceDiscription;
        ImageView img_Category,img_coupon,img_favorite;
        LinearLayout txt_serviceSubCategoryLayout,txt_imgserviceSubCategoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ServiceName = itemView.findViewById(R.id.txt_ServiceName);
            txt_serviceDiscription = itemView.findViewById(R.id.txt_serviceDiscription);
            txt_serviceChildCategory = itemView.findViewById(R.id.txt_serviceChildCategory);
            img_Category = itemView.findViewById(R.id.img_Category);
            txt_serviceSubCategoryLayout = itemView.findViewById(R.id.txt_serviceSubCategoryLayout);
            txt_imgserviceSubCategoryLayout = itemView.findViewById(R.id.txt_imgserviceSubCategoryLayout);
            img_coupon = itemView.findViewById(R.id.img_coupon);
            img_favorite = itemView.findViewById(R.id.img_favorite);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<ServiceSubCategoryResponseData> list = serviceSubCategoryList;
            int count = list.size();
            final ArrayList<ServiceSubCategoryResponseData> nlist = new ArrayList<>(count);
            String filterableText;
            if (!filterString.equals("")) {
                for (ServiceSubCategoryResponseData model : list) {
                    if(!filterString.equals("")) {
                        filterableText = model.getName()+model.getChildData();
                        if (filterableText != null && filterableText.toLowerCase().contains(filterString))  {
                            nlist.add(model);
                        }
                    } else {
                        nlist.add(model);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            } else {
                results.values = serviceSubCategoryList;
                results.count = serviceSubCategoryList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<ServiceSubCategoryResponseData>) results.values;
            notifyDataSetChanged();
        }
    }
}
