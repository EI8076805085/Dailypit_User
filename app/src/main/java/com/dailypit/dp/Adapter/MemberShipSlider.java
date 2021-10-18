package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Interface.MembershipClickListner;
import com.dailypit.dp.Interface.ServiceClickListner;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.Model.Banner.BottomBannerResponseData;
import com.dailypit.dp.R;
import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class MemberShipSlider extends InfinitePagerAdapter {

    private Context context;
    List<BannerResponseData> bottomBannerResponses;
    MembershipClickListner serviceClickListner;

    public MemberShipSlider(Context context, List<BannerResponseData> bottomBannerResponses) {
        this.context = context;
        this.bottomBannerResponses = bottomBannerResponses;
        this.serviceClickListner = serviceClickListner;

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

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                serviceClickListner.membershipClick(bottomBannerResponses.get(position).getCategoryId(),bottomBannerResponses.get(position).getCategoryName());
//            }
//        });

        return imageLayout;
    }
}
