package com.dailypit.dp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Activity.PackageDetailActivity;
import com.dailypit.dp.Model.Packageg.PackageResponseData;
import com.dailypit.dp.R;
import com.github.demono.adapter.InfinitePagerAdapter;
import java.util.List;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class PackageSliderAdapter extends InfinitePagerAdapter {

    private Context context;
    List<PackageResponseData> list;

    public PackageSliderAdapter(Context context, List<PackageResponseData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup view) {
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View imageLayout = layoutInflater.inflate(R.layout.package_slider_list, view, false);
        final ImageView imageView = imageLayout.findViewById(R.id.image);

        Glide.with(context).load(IMAGE +list.get(position).getPackageImage()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageDetailActivity.class);
                intent.putExtra("Package_id", list.get(position).getPackageId());
                context.startActivity(intent);
            }
        });

        return imageLayout;
    }
}
