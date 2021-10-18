package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.demono.adapter.InfinitePagerAdapter;
import com.dailypit.dp.Interface.AllServiceIDListner;
import com.dailypit.dp.Model.Banner.BannerResponseData;
import com.dailypit.dp.R;

import java.util.ArrayList;
import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class AllServiceSlider extends InfinitePagerAdapter {


    private LayoutInflater inflater;
    private Context context;
    List<BannerResponseData> bannerResponses;
    AllServiceIDListner serviceClickListner;

    public AllServiceSlider(Context context,  List<BannerResponseData> bannerResponses, AllServiceIDListner serviceClickListner) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.bannerResponses = bannerResponses;
        this.serviceClickListner = serviceClickListner;

    }

    @Override
    public int getItemCount() {
        return  bannerResponses.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup view) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        Glide.with(context).load(IMAGE +bannerResponses.get(position).getImage()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceClickListner.serviceClickId(bannerResponses.get(position).getCategoryId(),bannerResponses.get(position).getCategoryName());
            }
        });

        return imageLayout;
    }
}
