package com.dailypit.dp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.dailypit.dp.Model.HowItWorks.HowitWorksResponseData;
import com.dailypit.dp.R;
import java.util.List;

public class HowItWorksAdapters extends RecyclerView.Adapter<HowItWorksAdapters.ViewHolder> {

    Context context;
    List<HowitWorksResponseData> howitWorksResponseData;
    ProgressBar mainProgressbar;

    public HowItWorksAdapters(List<HowitWorksResponseData> howitWorksResponseData, Context context, ProgressBar mainProgressbar) {
        this.howitWorksResponseData = howitWorksResponseData;
        this.context = context;
        this.mainProgressbar = mainProgressbar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.how_it_work_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HowitWorksResponseData myListData = howitWorksResponseData.get(position);
         holder.txt_Heading.setText(HtmlCompat.fromHtml(myListData.getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
         holder.txt_discription.setText(HtmlCompat.fromHtml(myListData.getWorkProcedure(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        String path = "<iframe src='"+myListData.getWorkProcedureVideo()+"' width='100%' height='100%' style='border: none;'></iframe>";

        holder.web_howitWorks.loadData(path, "text/html", "utf-8");
        holder.web_howitWorks.getSettings().setJavaScriptEnabled(true);
        holder.web_howitWorks.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mainProgressbar.setProgress(progress);
                if (progress == 100) {
                    mainProgressbar.setVisibility(View.GONE);
                } else {
                    mainProgressbar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return howitWorksResponseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_Heading,txt_discription;
        public WebView web_howitWorks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Heading = itemView.findViewById(R.id.txt_Heading);
            web_howitWorks = itemView.findViewById(R.id.web_howitWorks);
            txt_discription = itemView.findViewById(R.id.txt_discription);

        }
    }
}
