package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.demono.adapter.InfinitePagerAdapter;
import com.dailypit.dp.Interface.ServiceClickListner;
import com.dailypit.dp.Model.Banner.BottomBannerResponseData;
import com.dailypit.dp.R;

import java.util.ArrayList;
import java.util.List;

import static com.dailypit.dp.Utils.Constants.IMAGE;

public class SliderAdapter extends InfinitePagerAdapter {

    private Context context;
    List<BottomBannerResponseData> bottomBannerResponses;
    ServiceClickListner serviceClickListner;
    View view;

    public SliderAdapter(Context context, List<BottomBannerResponseData> bottomBannerResponses, View view, ServiceClickListner serviceClickListner) {
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
                serviceClickListner.serviceClick(bottomBannerResponses.get(position).getCategoryId(),bottomBannerResponses.get(position).getCategoryName(),bottomBannerResponses.get(position).getDiscount_amount(),bottomBannerResponses.get(position).getDiscount_type(),view);
            }
        });

        return imageLayout;
    }
}

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//
//    @Override
//    public int getCount() {
//        return bottomBannerResponses.size();
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup view, int position) {
//        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
//
//        assert imageLayout != null;
//        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
//
//        Glide.with(context).load(BANNERIMG+IMAGES.get(position)).into(imageView);
//        view.addView(imageLayout, 0);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                serviceClickListner.serviceClick(bottomBannerResponses.get(position).getCategoryId(), view);
//            }
//        });
//
//        return imageLayout;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view.equals(object);
//    }
//
//    @Override
//    public void restoreState(Parcelable state, ClassLoader loader) {
//    }
//
//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
//
//
//}