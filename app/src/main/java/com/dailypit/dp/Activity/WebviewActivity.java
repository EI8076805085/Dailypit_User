package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dailypit.dp.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

public class WebviewActivity extends AppCompatActivity {

    WebView main_webView;
    ProgressDialog progressDialog;
    String orderID;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        main_webView = findViewById(R.id.main_webView);
        layout = findViewById(R.id.layout);
        orderID = getIntent().getStringExtra("order_id");

        main_webView.getSettings().setJavaScriptEnabled(true);
        main_webView.getSettings().setLoadWithOverviewMode(true);
        main_webView.getSettings().setDomStorageEnabled(true);
        main_webView.getSettings().setUseWideViewPort(true);
        main_webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        main_webView.setWebViewClient(new WebViewClient() {
           @Override
             public boolean shouldOverrideUrlLoading(WebView wv, String url) {
              if (url.startsWith("tel:") || url.startsWith("whatsapp:")) {
                 Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setData(Uri.parse(url));
                  startActivity(intent);
                   main_webView.goBack();
                  return true;
                   }
                return false;
            }

          @Override
           public void onPageStarted(WebView view, String url, Bitmap favicon) {
                  super.onPageStarted(view, url, favicon);
                  progressDialog.show();

               }

          @Override
            public void onPageFinished(WebView view, String url) {
                  super.onPageFinished(view, url);
                   progressDialog.dismiss();
                   pdf();
                 }
              }
        );

        main_webView.loadUrl("https://dailypit.com/dp/api/user/invoice?order_id="+orderID);

    }

    public void pdf()
    {
        PdfGenerator.getBuilder()
                .setContext(WebviewActivity.this)
                .fromViewSource()
                .fromView(layout)
                .setFileName("Invoice Dailypit")
                .openPDFafterGeneration(true)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                       // Toast.makeText(WebviewActivity.this, "Downloaded Sucessfully", Toast.LENGTH_SHORT).show();
                        /*When PDF generation is finished*/
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        Toast.makeText(WebviewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
          }


}