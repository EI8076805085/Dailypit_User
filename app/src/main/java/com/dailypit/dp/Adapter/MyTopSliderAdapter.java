package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.github.demono.adapter.InfinitePagerAdapter;
import com.dailypit.dp.Interface.ServiceClickListner;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.R;
import java.util.ArrayList;
import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class MyTopSliderAdapter extends InfinitePagerAdapter {

    Context context;
    ServiceClickListner serviceClickListner;
    List<BannerResponseData> bottomBannerResponses;
    View view;


    public MyTopSliderAdapter(Context context, List<BannerResponseData> bottomBannerResponses, View view, ServiceClickListner serviceClickListner) {
        this.context = context;
        this.bottomBannerResponses = bottomBannerResponses;
        this.serviceClickListner = serviceClickListner;
        this.view = view;

    }

    @Override
    public int getItemCount() {
        return  bottomBannerResponses.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup view) {
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View imageLayout = layoutInflater.inflate(R.layout.slidingimages_layout, view, false);
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        Glide.with(context).load(IMAGE +bottomBannerResponses.get(position).getImage()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               serviceClickListner.serviceClick(bottomBannerResponses.get(position).getCategoryId(),bottomBannerResponses.get(position).getCategoryName(), bottomBannerResponses.get(position).getDiscount_amount(),bottomBannerResponses.get(position).getDiscount_type() , view);
            }
        });

        return imageLayout;
    }
}