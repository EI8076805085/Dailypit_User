package com.dailypit.dp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.PdfConverter;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import java.io.File;
import static com.facebook.FacebookSdk.getApplicationContext;


public class RefundandEarnFragment extends Fragment {

    LinearLayout facebook_linearLayout, whatsAppLayout, shareMoreLayout, messageLayout;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refundand_earn, container, false);
        facebook_linearLayout = view.findViewById(R.id.facebook_linearLayout);
        whatsAppLayout = view.findViewById(R.id.whatsAppLayout);
        shareMoreLayout = view.findViewById(R.id.shareMoreLayout);
        messageLayout = view.findViewById(R.id.messageLayout);

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String referalcode = yourPrefrence.getData("REFERALCODE");
        String videoLink = "https://play.google.com/store/apps/details?id=com.dailypit.dp&referrer=" + referalcode;
        String sms = "Upto ₹ 100 instant off on Dailypit AC | RO | Laptops | Washing Machine | Refrigerator and many more Repair Services.\n" +
                "\n" +
                "Dailypit- " + videoLink +
                "\n" +
                "Trusted and verified  professionals ensure reliable services at your door step.\n" +
                "INSTALL NOW!";


        facebook_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendAppMsg("com.facebook.katana",sms);

//                callbackManager = CallbackManager.Factory.create();
//                shareDialog = new ShareDialog(getActivity());
//                FacebookSdk.sdkInitialize(getApplicationContext());
//                String videoLink = "https://play.google.com/store/apps/details?id=com.dailypit.dp&referrer=" + referalcode;
//                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().setQuote(videoLink)
//                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.dailypit.dp&referrer=" + referalcode))
//                        .build();
//                if (ShareDialog.canShow(ShareLinkContent.class)) {
//                    shareDialog.show(shareLinkContent);
//                }

            }
        });

        whatsAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAppMsg("com.whatsapp",sms);

//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                whatsappIntent.setType("text/plain");
//                whatsappIntent.setPackage("com.whatsapp");
//                whatsappIntent.putExtra(Intent.EXTRA_TEXT, sms);
//                try {
//                    startActivity(whatsappIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(getActivity(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//                }
//

            }
        });

        shareMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dailypit");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sms);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra("sms_body",sms);
                startActivity(intent);
            }

        });




        return view;
    }

    public void sendAppMsg(String packagename, String msg) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            // change with required  application package
            intent.setPackage(packagename);
            if (intent != null) {
                intent.putExtra(Intent.EXTRA_TEXT, msg);//
                startActivity(Intent.createChooser(intent, msg));
            } else {
                Toast.makeText(getActivity(), "App not found", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "App not found", Toast.LENGTH_SHORT).show();
        }
    }
}