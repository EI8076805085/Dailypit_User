package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dailypit.dp.Model.Wallet.WalletMoneyResponse;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.dailypit.dp.Adapter.CartAdapter;
import com.dailypit.dp.Interface.ChildCategoryListner;
import com.dailypit.dp.Interface.ItemQuantityListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.Model.Payment.PaymentResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class ProceedPay extends AppCompatActivity implements ChildCategoryListner, ItemQuantityListner, PaymentResultListener {

    Button btn_confirm;
    String childCategory, totalitemQuantity, address_id = "", userName, userEmail, userMobile, TotalAmountPay, memberShipId = "0";
    int price_rs;
    ImageView back_img, img_forwordImage, img_clear, address_img;
    DatabaseHandler databaseHandler;
    List<CartResponse> alldata;
    View membershipView,memberShipChargView;
    RecyclerView cartRecyclerView;
    ArrayList<String> childCategoryId = new ArrayList<>();
    ArrayList<String> itemQuantity = new ArrayList<>();
    TextView txt_Ok, txt_defaultAddress, txt_itemTotal, txt_discount, txt_finalAmount, txt_ChangeAddresss, txt_couponCode,
            txt_couponName, couponDiscription, txt_addressType, txt_multiServicediscount, txt_checkOrder, txt_userWalletAmount,
            txt_walletDiductAmount,txt_notUserWallet,txt_dailypitWallet,txt_membership_discount,txt_membership_charge;
    RadioButton rb_onLinePay, rb_peyLator;
    LinearLayout onLinePayLayout, payLatorLayout, applyCouponLayout, disCountLayout, apply_Coupon_Layout, applyed_CouponLayout,
            multiServicedisCountLayout, user_walletLayout,membership_discount_layout,membership_charge_layout;
    String user_id, subcategory_id, coupon_id = "0", coupon_discount, multi_discount,
            total_amount, net_amount, item_quentity, payment_mode, payment_status, couponcode, couponName, couponDiscount, couponID, disCount_Typeee;
    ProgressBar mainProgressbar;
    String paymentMethod = "Online", discount, totalAmount, total, date, time,CategoryID ="";
    List<UserAddressListResponseData> userAddressListResponseData = new ArrayList<>();
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    String categoryName, subcategoryName, childCategoryName, address_latitude="",address_longitude="";
    AppEventsLogger logger;
    CheckBox cb_userWallet;
    android.location.Address location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_pay);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        txt_defaultAddress = findViewById(R.id.txt_defaultAddress);
        txt_multiServicediscount = findViewById(R.id.txt_multiServicediscount);
        rb_onLinePay = findViewById(R.id.rb_onLinePay);
        rb_peyLator = findViewById(R.id.rb_peyLator);
        onLinePayLayout = findViewById(R.id.onLinePayLayout);
        payLatorLayout = findViewById(R.id.payLatorLayout);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_itemTotal = findViewById(R.id.txt_itemTotal);
        txt_discount = findViewById(R.id.txt_discount);
        back_img = findViewById(R.id.back_img);
        disCountLayout = findViewById(R.id.disCountLayout);
        txt_finalAmount = findViewById(R.id.txt_finalAmount);
        txt_ChangeAddresss = findViewById(R.id.txt_ChangeAddresss);
        txt_couponCode = findViewById(R.id.txt_couponCode);
        applyCouponLayout = findViewById(R.id.applyCouponLayout);
        apply_Coupon_Layout = findViewById(R.id.apply_Coupon_Layout);
        applyed_CouponLayout = findViewById(R.id.applyed_CouponLayout);
        img_forwordImage = findViewById(R.id.img_forwordImage);
        img_clear = findViewById(R.id.img_clear);
        txt_addressType = findViewById(R.id.txt_addressType);
        address_img = findViewById(R.id.address_img);
        multiServicedisCountLayout = findViewById(R.id.multiServicedisCountLayout);
        txt_userWalletAmount = findViewById(R.id.txt_userWalletAmount);
        txt_walletDiductAmount = findViewById(R.id.txt_walletDiductAmount);
        cb_userWallet = findViewById(R.id.cb_userWallet);
        user_walletLayout = findViewById(R.id.user_walletLayout);
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_notUserWallet = findViewById(R.id.txt_notUserWallet);
        txt_dailypitWallet = findViewById(R.id.txt_dailypitWallet);
        txt_membership_discount = findViewById(R.id.txt_membership_discount);
        txt_membership_charge = findViewById(R.id.txt_membership_charge);
        membership_discount_layout = findViewById(R.id.membership_discount_layout);
        membershipView = findViewById(R.id.membershipView);
        membership_charge_layout = findViewById(R.id.membership_charge_layout);
        memberShipChargView = findViewById(R.id.memberShipChargView);
        logger = AppEventsLogger.newLogger(this);

        setUpDB();

        cb_userWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walletAmount = "";

                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String walletAmountt = txt_userWalletAmount.getText().toString();
                String nextAmount = txt_finalAmount.getText().toString();
                String[] newamount = nextAmount.split(" ");
                totalAmount = newamount[1];

                int amount = ((Integer.parseInt(totalAmount) * 20) / 100);

                if(amount>100) {
                   if (Integer.parseInt(walletAmountt)<100){
                       walletAmount =walletAmountt;
                   } else {
                       walletAmount ="100";
                   }
                } else {
                    if (Integer.parseInt(walletAmountt)<amount){
                        walletAmount =walletAmountt;
                    } else {
                        walletAmount =String.valueOf(amount);
                    }
                }

                if (cb_userWallet.isChecked()) {
                    int newWalletAmount = 0;
                    if(Integer.parseInt(walletAmountt)>Integer.parseInt(walletAmount)){
                        newWalletAmount = Integer.parseInt(walletAmountt) - Integer.parseInt(walletAmount);
                    } else {
                        newWalletAmount = Integer.parseInt(walletAmount) - Integer.parseInt(walletAmountt);
                    }

                    txt_userWalletAmount.setText(newWalletAmount+"");

                    if (Integer.parseInt(walletAmount) > Integer.parseInt(totalAmount)) {
                        int safaAmount = Integer.parseInt(walletAmount) - Integer.parseInt(totalAmount);
                        txt_walletDiductAmount.setText("\u20B9" + " " + totalAmount);
                        txt_finalAmount.setText("\u20B9" + " " + "0");
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        yourPrefrence.saveData("wallet_diduct_amount", totalAmount);
                    } else {
                        int safaAmount = Integer.parseInt(totalAmount) - Integer.parseInt(walletAmount);
                        txt_walletDiductAmount.setText("\u20B9" + " " + walletAmount);
                        txt_finalAmount.setText("\u20B9" + " " + safaAmount);
                        String nextAmount1 = txt_finalAmount.getText().toString();
                        String[] amount1 = nextAmount1.split(" ");
                        totalAmount = amount1[1];
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        yourPrefrence.saveData("wallet_diduct_amount", walletAmount);
                    }
                } else {
                    String discountAmount = yourPrefrence.getData("wallet_diduct_amount");
                    int newWalletAmount = Integer.parseInt(walletAmountt) + Integer.parseInt(discountAmount);
                    txt_userWalletAmount.setText(newWalletAmount+"");

                    String next_mount = newamount[1];
                    String walletDiductAmount = yourPrefrence.getData("wallet_diduct_amount");
                    int safaAmount = Integer.parseInt(next_mount) + Integer.parseInt(walletDiductAmount);
                    txt_walletDiductAmount.setText("");
                    txt_finalAmount.setText("\u20B9" + " " + safaAmount);
                    String nextAmount1 = txt_finalAmount.getText().toString();
                    String[] amount1 = nextAmount1.split(" ");
                    totalAmount = amount1[1];
                    yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                    yourPrefrence.saveData("wallet_diduct_amount", totalAmount);
                }
            }
        });

        txt_ChangeAddresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProceedPay.this, Address.class);
                startActivity(intent);
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rb_onLinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_walletLayout.setClickable(true);
                cb_userWallet.setClickable(true);
                txt_dailypitWallet.setTextColor(Color.parseColor("#000000"));
                user_walletLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.drawable.whilte_background));
                rb_onLinePay.setChecked(true);
                rb_peyLator.setChecked(false);
                paymentMethod = "Online";

                if (user_walletLayout.getVisibility() == View.GONE) {
                    user_walletLayout.setVisibility(View.VISIBLE);

                } else {
                    // Either gone or invisible
                }

            }
        });

        rb_peyLator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_walletLayout.setClickable(false);
                cb_userWallet.setClickable(false);
                txt_dailypitWallet.setTextColor(Color.parseColor("#cacaca"));
                user_walletLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.drawable.blur_background));
                if (paymentMethod.equals("Online")) {
                    if (cb_userWallet.isChecked()) {

                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] newamount = nextAmount.split(" ");
                        totalAmount = newamount[1];
                        String walletAmountt = txt_userWalletAmount.getText().toString();

                        String discountAmount = yourPrefrence.getData("wallet_diduct_amount");
                        int newWalletAmount = Integer.parseInt(walletAmountt) + Integer.parseInt(discountAmount);
                        txt_userWalletAmount.setText(newWalletAmount+"");

                        String next_mount = newamount[1];
                        int safaAmount = Integer.parseInt(next_mount) + Integer.parseInt(discountAmount);
                        txt_walletDiductAmount.setText("");
                        txt_finalAmount.setText("\u20B9" + " " + safaAmount);
                        String nextAmount1 = txt_finalAmount.getText().toString();
                        String[] amount1 = nextAmount1.split(" ");
                        totalAmount = amount1[1];
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        yourPrefrence.saveData("wallet_diduct_amount", totalAmount);

                        rb_onLinePay.setChecked(false);
                        rb_peyLator.setChecked(true);
                        paymentMethod = "Later";
                        cb_userWallet.setChecked(false);

                    } else {
                        rb_onLinePay.setChecked(false);
                        rb_peyLator.setChecked(true);
                        paymentMethod = "Later";
                        cb_userWallet.setChecked(false);
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] amount = nextAmount.split(" ");
                        String next_mount = amount[1];
                        txt_walletDiductAmount.setText("");
                        txt_finalAmount.setText("\u20B9" + " " + next_mount);
                        String nextAmount1 = txt_finalAmount.getText().toString();
                        String[] amount1 = nextAmount1.split(" ");
                        totalAmount = amount1[1];
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                    }
                }
                else {
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Later";
                    cb_userWallet.setChecked(false);
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String nextAmount = txt_finalAmount.getText().toString();
                    String[] amount = nextAmount.split(" ");
                    String next_mount = amount[1];
                    txt_walletDiductAmount.setText("");
                    txt_finalAmount.setText("\u20B9" + " " + next_mount);
                    String nextAmount1 = txt_finalAmount.getText().toString();
                    String[] amount1 = nextAmount1.split(" ");
                    totalAmount = amount1[1];
                    yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                }
            }
        });

        onLinePayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_walletLayout.setClickable(true);
                cb_userWallet.setClickable(true);
                txt_dailypitWallet.setTextColor(Color.parseColor("#000000"));
                user_walletLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.drawable.whilte_background));
                rb_onLinePay.setChecked(true);
                rb_peyLator.setChecked(false);
                paymentMethod = "Online";
                if (user_walletLayout.getVisibility() == View.GONE) {
                    user_walletLayout.setVisibility(View.VISIBLE);

                } else {
                    // Either gone or invisible
                }
            }
        });

        payLatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_walletLayout.setClickable(false);
                cb_userWallet.setClickable(false);
                txt_dailypitWallet.setTextColor(Color.parseColor("#cacaca"));
                user_walletLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.drawable.blur_background));
                if (paymentMethod.equals("Online")) {
                    if (cb_userWallet.isChecked()) {
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] newamount = nextAmount.split(" ");
                        totalAmount = newamount[1];
                        String walletAmountt = txt_userWalletAmount.getText().toString();

                        String discountAmount = yourPrefrence.getData("wallet_diduct_amount");
                        int newWalletAmount = Integer.parseInt(walletAmountt) + Integer.parseInt(discountAmount);
                        txt_userWalletAmount.setText(newWalletAmount+"");

                        String next_mount = newamount[1];
                        int safaAmount = Integer.parseInt(next_mount) + Integer.parseInt(discountAmount);
                        txt_walletDiductAmount.setText("");
                        txt_finalAmount.setText("\u20B9" + " " + safaAmount);
                        String nextAmount1 = txt_finalAmount.getText().toString();
                        String[] amount1 = nextAmount1.split(" ");
                        totalAmount = amount1[1];
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        yourPrefrence.saveData("wallet_diduct_amount", totalAmount);

                        rb_onLinePay.setChecked(false);
                        rb_peyLator.setChecked(true);
                        paymentMethod = "Later";
                        cb_userWallet.setChecked(false);
                    } else {
                        rb_onLinePay.setChecked(false);
                        rb_peyLator.setChecked(true);
                        paymentMethod = "Later";
                        cb_userWallet.setChecked(false);
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] amount = nextAmount.split(" ");
                        String next_mount = amount[1];
                        txt_walletDiductAmount.setText("");
                        txt_finalAmount.setText("\u20B9" + " " + next_mount);
                        String nextAmount1 = txt_finalAmount.getText().toString();
                        String[] amount1 = nextAmount1.split(" ");
                        totalAmount = amount1[1];
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                    }
                } else {
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Later";
                    cb_userWallet.setChecked(false);
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String nextAmount = txt_finalAmount.getText().toString();
                    String[] amount = nextAmount.split(" ");
                    String next_mount = amount[1];
                    txt_walletDiductAmount.setText("");
                    txt_finalAmount.setText("\u20B9" + " " + next_mount);
                    String nextAmount1 = txt_finalAmount.getText().toString();
                    String[] amount1 = nextAmount1.split(" ");
                    totalAmount = amount1[1];
                    yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                }
            }
        });

        alldata = databaseHandler.cartInterface().getallcartdata();
        CartAdapter adapter = new CartAdapter(alldata, ProceedPay.this, this, this);
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new GridLayoutManager(ProceedPay.this, 1));
        cartRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        user_id = yourPrefrence.getData("USERID");
        item_quentity = totalitemQuantity;
        payment_mode = "Online";
        payment_status = "Success";

        img_forwordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProceedPay.this, ApplyCoupon.class);
                intent.putExtra("subCategory", subcategory_id);
                startActivity(intent);
                finish();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String disCount_Type = yourPrefrence.getData("disCount_Typee");
                coupon_discount = yourPrefrence.getData("total_discount");
                multi_discount = yourPrefrence.getData("multi_discount");
                subcategory_id = yourPrefrence.getData("subCategoryID");
                total_amount = yourPrefrence.getData("final_amount");
                net_amount = yourPrefrence.getData("net_amount");
                date = yourPrefrence.getData("date");
                time = yourPrefrence.getData("timeSlot");

                String[] amount = net_amount.split(" ");
                totalAmount = amount[1];
                applyCouponLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.color.blue));

                apply_Coupon_Layout.setVisibility(View.VISIBLE);
                applyed_CouponLayout.setVisibility(View.GONE);
                disCountLayout.setVisibility(View.GONE);
                coupon_id = "0";

                String[] disCount = coupon_discount.split("-");
                String newDisCount = disCount[1];
                String[] Amount = net_amount.split(" ");
                String NewNetAmount = Amount[1];

                if (disCount_Type.equalsIgnoreCase("percentage")) {
                    String[] Amounte = TotalAmountPay.split(" ");
                    String NewNetAmounte = Amounte[1];

                    int feedouble = Integer.parseInt(couponDiscount);
                    int discountnumber = Integer.parseInt(NewNetAmounte);
                    int discountt = (discountnumber / 100) * feedouble;
                    int totalDiscount = Integer.parseInt(newDisCount) - discountt;
                    int finalAmount = Integer.parseInt(NewNetAmount) + discountt;
                    discount = String.valueOf(totalDiscount);
                    txt_discount.setText("\u20B9" + " -" + discount);
                    txt_finalAmount.setText("\u20B9" + " " + finalAmount);
                    String nextAmount = txt_finalAmount.getText().toString();
                    String[] newamount = nextAmount.split(" ");
                    totalAmount = newamount[1];
                    yourPrefrence.saveData("total_discount", "\u20B9" + " -" + discount);
                    yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                    String dis = txt_discount.getText().toString();
                    String[] time1 = dis.split("-");
                    int totalPay = Integer.parseInt(time1[1]);
                    discount = String.valueOf(totalPay);
                    img_forwordImage.setVisibility(View.VISIBLE);
                    img_clear.setVisibility(View.GONE);
                    apply_Coupon_Layout.setVisibility(View.VISIBLE);
                    applyed_CouponLayout.setVisibility(View.GONE);
                    yourPrefrence.saveData("coupon", "0");

                } else {
                    int totalDiscount = Integer.parseInt(newDisCount) - Integer.parseInt(couponDiscount);
                    int finalAmount = Integer.parseInt(NewNetAmount) + Integer.parseInt(couponDiscount);
                    discount = String.valueOf(totalDiscount);
                    txt_discount.setText("\u20B9" + " -" + discount);
                    txt_finalAmount.setText("\u20B9" + " " + finalAmount);
                    String nextAmount = txt_finalAmount.getText().toString();
                    String[] newamount = nextAmount.split(" ");
                    totalAmount = newamount[1];
                    yourPrefrence.saveData("total_discount", "\u20B9" + " -" + discount);
                    yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                    String dis = txt_discount.getText().toString();
                    String[] time1 = dis.split("-");
                    int totalPay = Integer.parseInt(time1[1]);
                    discount = String.valueOf(totalPay);
                    img_forwordImage.setVisibility(View.VISIBLE);
                    img_clear.setVisibility(View.GONE);
                    apply_Coupon_Layout.setVisibility(View.VISIBLE);
                    applyed_CouponLayout.setVisibility(View.GONE);
                    yourPrefrence.saveData("coupon", "0");
                }

            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethod.equals("Online")) {
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    String childCategory = yourPrefrence.getData("childCategory");
                    String totalitemQuantity = yourPrefrence.getData("totalitemQuantity");

                    if (subcategory_id.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please Select Service", Toast.LENGTH_SHORT).show();
                    } else if (childCategory.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please Select Type", Toast.LENGTH_SHORT).show();
                    } else if (total.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Total Amount is required", Toast.LENGTH_SHORT).show();
                    } else if (totalAmount.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Total Amount is required", Toast.LENGTH_SHORT).show();
                    } else if (totalitemQuantity.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Item is required", Toast.LENGTH_SHORT).show();
                    } else if (address_id.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add order delivery address", Toast.LENGTH_SHORT).show();
                    } else if (userName == null || userName.trim().isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add name in your profile", Toast.LENGTH_SHORT).show();
                    } else if (userMobile == null || userMobile.trim().isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add mobile number in your profile", Toast.LENGTH_SHORT).show();
                    } else {
                        getLocationFromAddress(getApplicationContext(),txt_defaultAddress.getText().toString());
                        String finalAmount = txt_finalAmount.getText().toString();
                        String[] amount = finalAmount.split(" ");
                        String next_mount = amount[1];
                        if (next_mount.equals("0")) {
                            startPaymentWithWallet();
//                            Intent intent = new Intent(ProceedPay.this,SelectPaymentMethod.class);
//                            startActivity(intent);
                        } else {
                            startpayment();
                        }
                    }

                } else {
                    if (subcategory_id.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please Select Service", Toast.LENGTH_SHORT).show();
                    } else if (childCategory.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please Select Type", Toast.LENGTH_SHORT).show();
                    } else if (total.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Total Amount is required", Toast.LENGTH_SHORT).show();
                    } else if (totalAmount.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Total Amount is required", Toast.LENGTH_SHORT).show();
                    } else if (totalitemQuantity.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Item is required", Toast.LENGTH_SHORT).show();
                    } else if (address_id.isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add order delivery address", Toast.LENGTH_SHORT).show();
                    } else if (userName == null || userName.trim().isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add name in your profile", Toast.LENGTH_SHORT).show();
                    } else if (userMobile == null || userMobile.trim().isEmpty()) {
                        Toast.makeText(ProceedPay.this, "Please add mobile number in your profile", Toast.LENGTH_SHORT).show();
                    } else {
                        getLocationFromAddress(getApplicationContext(),txt_defaultAddress.getText().toString());
                        payLator();
                    }
                }
            }
        });

    }

    private void startPaymentWithWallet() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        String walletDiductAmount = "";
        if (cb_userWallet.isChecked()) {
            walletDiductAmount = yourPrefrence.getData("wallet_diduct_amount");
        } else {
            walletDiductAmount = "0";
        }

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("category_id", CategoryID);
        map.put("sub_category_id", subcategory_id);
        map.put("child_category_id", childCategory);
        map.put("coupon_id", coupon_id);
        map.put("transaction_id", "");
        map.put("discount_amount", discount);
        map.put("total_amount", total);
        map.put("net_amount", "0");
        map.put("quantity", totalitemQuantity);
        map.put("address_id", address_id);
        map.put("order_date", date);
        map.put("time_slot", time);
        map.put("payment_mode", "Pay Online");
        map.put("payment_status", "Paid");
        map.put("wallet_amount", walletDiductAmount);
        map.put("latitude", address_latitude);
        map.put("longitude", address_longitude);
        map.put("payment_for", "order");
        map.put("membership_id", memberShipId);
        map.put("is_package", "0");
        map.put("package_id", "0");
        map.put("unique_id", "");

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PaymentResponse> call = serviceInterface.placeOrder(map);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Success", "Online");
                        final Dialog dialog = new Dialog(ProceedPay.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.cnf_bottom_sheet_layout);
                        dialog.setCancelable(false);
                        txt_checkOrder = dialog.findViewById(R.id.txt_checkOrder);
                        txt_checkOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                YourPreference yourPrefrence = YourPreference.getInstance(ProceedPay.this);
                                yourPrefrence.saveData("memberShipId","");
                                yourPrefrence.saveData("coupon", "0");
                                yourPrefrence.saveData("wallet_diduct_amount", "0");
                                com.dailypit.dp.MainActivity.MainObject.setFromActivity(false);
                                Intent intent = new Intent(ProceedPay.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    } else {
                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Failed (Server Issue)", "Online");
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(ProceedPay.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void startpayment() {
        Checkout checkout = new Checkout();
        final Activity activity = this;
        String[] time1 = total_amount.split(" ");
        int totalPay = Integer.parseInt(time1[1]);
        total = String.valueOf(totalPay);
        price_rs = Integer.parseInt(String.valueOf(totalAmount));

        try {
            JSONObject options = new JSONObject();
            options.put("name", userName);
            options.put("currency", "INR");
            options.put("prefill.email", userEmail);
            options.put("prefill.contact", userMobile);
            options.put("amount", price_rs * 100);
            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }
    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(ProceedPay.this);
        String userId = yourPrefrence.getData("USERID");
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserProfileResponse> call = serviceInterface.getUserProfile(map);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userProfileResponseData = response.body().getData();
                        for (int i = 0; i < userProfileResponseData.size(); i++) {
                            userEmail = userProfileResponseData.get(i).getEmail();
                            userMobile = userProfileResponseData.get(i).getMobile();
                            userName = userProfileResponseData.get(i).getName();
                        }
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(ProceedPay.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(ProceedPay.this, DatabaseHandler.class, "cart")
                .allowMainThreadQueries().build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProceedPay.this, ServiceAcitvity.class);
        intent.putExtra("flag", "0");
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        yourPrefrence.saveData("coupon", "0");
        yourPrefrence.saveData("wallet_diduct_amount", "0");
    }

    @Override
    public void getCategory(String id, String name) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        childCategoryId.add(id);
        String vtype = childCategoryId.toString();
        childCategory = vtype.replaceAll("[\\[\\]\\(\\) ]", "");
        Log.d("stan", "" + childCategory);
        yourPrefrence.saveData("childCategory", childCategory);

    }

    @Override
    public void getQuantity(String id) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        itemQuantity.add(id);
        String quantity = itemQuantity.toString();
        totalitemQuantity = quantity.replaceAll("[\\[\\]\\(\\) ]", "");
        Log.d("totalitemQuantity", "" + totalitemQuantity);
        yourPrefrence.saveData("totalitemQuantity", totalitemQuantity);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (razorpayPaymentID.isEmpty()) {
            Toast.makeText(ProceedPay.this, "Transaction failed", Toast.LENGTH_SHORT).show();
        } else {
            paymentsuccess(razorpayPaymentID);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Transaction Failed", "Online");
        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
    }

    private void paymentsuccess(String razorpayPaymentID) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        String walletDiductAmount = "";

        if (cb_userWallet.isChecked()) {
            walletDiductAmount = yourPrefrence.getData("wallet_diduct_amount");
        } else {
            walletDiductAmount = "0";
        }

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("category_id", CategoryID);
        map.put("sub_category_id", subcategory_id);
        map.put("child_category_id", childCategory);
        map.put("coupon_id", coupon_id);
        map.put("transaction_id", razorpayPaymentID);
        map.put("discount_amount", discount);
        map.put("total_amount", total);
        map.put("net_amount", totalAmount);
        map.put("quantity", totalitemQuantity);
        map.put("address_id", address_id);
        map.put("order_date", date);
        map.put("time_slot", time);
        map.put("payment_mode", "Pay Online");
        map.put("payment_status", "Paid");
        map.put("wallet_amount", walletDiductAmount);
        map.put("latitude", address_latitude);
        map.put("longitude", address_longitude);
        map.put("payment_for", "order");
        map.put("membership_id", memberShipId);
        map.put("is_package", "0");
        map.put("package_id", "0");
        map.put("unique_id", "");

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PaymentResponse> call = serviceInterface.placeOrder(map);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Success", "Online");
                        final Dialog dialog = new Dialog(ProceedPay.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.cnf_bottom_sheet_layout);
                        dialog.setCancelable(false);
                        txt_checkOrder = dialog.findViewById(R.id.txt_checkOrder);
                        txt_checkOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                YourPreference yourPrefrence = YourPreference.getInstance(ProceedPay.this);
                                yourPrefrence.saveData("memberShipId","");
                                yourPrefrence.saveData("coupon", "0");
                                yourPrefrence.saveData("wallet_diduct_amount", "0");
                                com.dailypit.dp.MainActivity.MainObject.setFromActivity(false);
                                Intent intent = new Intent(ProceedPay.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    } else {
                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Failed (Server Issue)", "Online");
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(ProceedPay.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void payLator() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String childCategory = yourPrefrence.getData("childCategory");
        String totalitemQuantity = yourPrefrence.getData("totalitemQuantity");

        try {
            if (subcategory_id.isEmpty()) {
                Toast.makeText(this, "Please Select Service", Toast.LENGTH_SHORT).show();
            } else if (childCategory.isEmpty()) {
                Toast.makeText(this, "Please Select Type", Toast.LENGTH_SHORT).show();
            } else if (total.isEmpty()) {
                Toast.makeText(this, "Total Amount is required", Toast.LENGTH_SHORT).show();
            } else if (totalAmount.isEmpty()) {
                Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show();
            } else if (totalitemQuantity.isEmpty()) {
                Toast.makeText(this, "Item is required", Toast.LENGTH_SHORT).show();
            } else if (address_id.isEmpty()) {
                Toast.makeText(this, "Please add order delivery address", Toast.LENGTH_SHORT).show();
            } else if (userName == null || userName.trim().isEmpty()) {
                Toast.makeText(ProceedPay.this, "Please add name in your profile", Toast.LENGTH_SHORT).show();
            } else if (userMobile == null || userMobile.trim().isEmpty()) {
                Toast.makeText(ProceedPay.this, "Please add mobile number in your profile", Toast.LENGTH_SHORT).show();
            } else {
                if (Helper.INSTANCE.isNetworkAvailable(ProceedPay.this)) {
                    getLocationFromAddress(getApplicationContext(),txt_defaultAddress.getText().toString());
                    orderPaylator();
                } else {
                    Helper.INSTANCE.Error(ProceedPay.this, getString(R.string.NOCONN));
                }
            }
        } catch (Exception e) {
            Log.e("tag", "Exception in onPaymentSuccess", e);
        }
    }

    private void orderPaylator() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("category_id", CategoryID);
        map.put("sub_category_id", subcategory_id);
        map.put("child_category_id", childCategory);
        map.put("coupon_id", coupon_id);
        map.put("transaction_id", "");
        map.put("discount_amount", discount);
        map.put("total_amount", total);
        map.put("net_amount", totalAmount);
        map.put("quantity", totalitemQuantity);
        map.put("address_id", address_id);
        map.put("order_date", date);
        map.put("time_slot", time);
        map.put("payment_mode", "Pay later");
        map.put("payment_status", "Not Paid");
        map.put("wallet_amount", "0");
        map.put("latitude", address_latitude);
        map.put("longitude", address_longitude);
        map.put("payment_for", "order");
        map.put("membership_id", memberShipId);
        map.put("is_package", "0");
        map.put("package_id", "0");
        map.put("unique_id", "");

        JSONObject jsonObject=new JSONObject(map);
        Log.i("Order_parameters",""+jsonObject);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PaymentResponse> call = serviceInterface.placeOrder(map);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {

                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Success", "Offline");

                        final Dialog dialog = new Dialog(ProceedPay.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.cnf_bottom_sheet_layout);
                        dialog.setCancelable(false);
                        txt_checkOrder = dialog.findViewById(R.id.txt_checkOrder);

                        txt_checkOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                YourPreference yourPrefrence = YourPreference.getInstance(ProceedPay.this);
                                yourPrefrence.saveData("memberShipId","");
                                yourPrefrence.saveData("coupon", "0");
                                yourPrefrence.saveData("wallet_diduct_amount", "0");
                                com.dailypit.dp.MainActivity.MainObject.setFromActivity(false);
                                Intent intent = new Intent(ProceedPay.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                                dialog.dismiss();
                            }
                        });

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();

                    } else {
                        logAdClickEvent(userName, userMobile, txt_defaultAddress.getText().toString(), categoryName, subcategoryName, childCategoryName, "Failed (Server Issue)", "Offline");
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(ProceedPay.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getUserAddressList() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<UserAddressListResponse> call = serviceInterface.getUserAddress(map);
        call.enqueue(new Callback<UserAddressListResponse>() {
            @Override
            public void onResponse(Call<UserAddressListResponse> call, retrofit2.Response<UserAddressListResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        userAddressListResponseData = response.body().getData();
                        for (int i = 0; i < userAddressListResponseData.size(); i++) {
                            if (userAddressListResponseData.get(i).getDefaults().equals("1")) {
                                String hfNumber = userAddressListResponseData.get(i).getHfNumber();
                                String address = userAddressListResponseData.get(i).getAddress();
                                String ldMark = userAddressListResponseData.get(i).getLandmark();
                                String state = userAddressListResponseData.get(i).getState();
                                String pin = userAddressListResponseData.get(i).getPincode();
                                String addressType = userAddressListResponseData.get(i).getType();

                                txt_defaultAddress.setText(hfNumber + ", " + address + ", " + ldMark + ", " + state + ", " + pin);
                                address_id = userAddressListResponseData.get(i).getId();
                                txt_addressType.setText(addressType);

                                if (addressType.equals("Work")) {
                                    address_img.setImageResource(R.drawable.address_work);
                                } else if (addressType.equals("Other")) {
                                    address_img.setImageResource(R.drawable.edit_location);
                                } else {
                                    address_img.setImageResource(R.drawable.ic_address_home);
                                }
                            }
                        }
                    } else {
                        address_id = "";
                        txt_defaultAddress.setText("Please add address");
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        getUserAddressList();
        getUserProfile();
        getWalletAmaunt();
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");

        String key = yourPrefrence.getData("coupon");
        coupon_discount = yourPrefrence.getData("total_discount");
        multi_discount = yourPrefrence.getData("multi_discount");
        subcategory_id = yourPrefrence.getData("subCategoryID");
        total_amount = yourPrefrence.getData("final_amount");
        net_amount = yourPrefrence.getData("net_amount");
        date = yourPrefrence.getData("date");
        time = yourPrefrence.getData("timeSlot");
        categoryName = yourPrefrence.getData("categoryName");
        subcategoryName = yourPrefrence.getData("subCategoryName");
        childCategoryName = yourPrefrence.getData("childCategoryname");


        memberShipId = yourPrefrence.getData("memberShipId");

        String memberShipDis = yourPrefrence.getData("membershipDiscount");

        String memberShipDiscountCharg = yourPrefrence.getData("membershipDiscountCharg");

        if(memberShipDis.equalsIgnoreCase("0") || memberShipDis.equalsIgnoreCase("")) {

        } else {
            membership_discount_layout.setVisibility(View.VISIBLE);
            membershipView.setVisibility(View.VISIBLE);
            txt_membership_discount.setText(yourPrefrence.getData("membershipDiscount"));

        }

        if(memberShipDiscountCharg.equalsIgnoreCase("0") || memberShipDiscountCharg.equalsIgnoreCase("")) {

        } else {
            membership_charge_layout.setVisibility(View.VISIBLE);
            memberShipChargView.setVisibility(View.VISIBLE);
            txt_membership_charge.setText(yourPrefrence.getData("membershipDiscountCharg"));
        }

        String[] multi_dis = multi_discount.split("-");
        String multidiscount = multi_dis[1];

        if(multidiscount.equalsIgnoreCase("0") || multidiscount.equalsIgnoreCase("") || multidiscount.equalsIgnoreCase("-0")) {

        } else {
            multiServicedisCountLayout.setVisibility(View.VISIBLE);
            txt_multiServicediscount.setText(multi_discount);
        }

        if(memberShipId.equalsIgnoreCase("0") || memberShipId.equalsIgnoreCase("")) {

        } else {
            payLatorLayout.setVisibility(View.GONE);
            user_walletLayout.setVisibility(View.GONE);
        }

        CategoryID = yourPrefrence.getData("CategoryID");

//        if(CategoryID.equalsIgnoreCase("20")){
//            payLatorLayout.setVisibility(View.GONE);
//        }

        String[] amount = net_amount.split(" ");
        totalAmount = amount[1];
        TotalAmountPay = total_amount;
        String[] Total_item = total_amount.split(" ");
        int total_Pay = Integer.parseInt(Total_item[1]);
        total = String.valueOf(total_Pay);

        if (coupon_discount.isEmpty()) {
            discount = "0";
            txt_itemTotal.setText(total_amount);
            txt_finalAmount.setText(net_amount);
        } else {
            txt_itemTotal.setText(total_amount);
            txt_finalAmount.setText(net_amount);
            String[] time1 = coupon_discount.split("-");
            int totalPay = Integer.parseInt(time1[1]);
            discount = String.valueOf(totalPay + "");
        }

        if (flag.equals("1")) {
            final Dialog dialog = new Dialog(ProceedPay.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.coupon_dialog);
            txt_Ok = dialog.findViewById(R.id.txt_Ok);
            txt_couponName = dialog.findViewById(R.id.txt_couponName);
            couponDiscription = dialog.findViewById(R.id.couponDiscription);
            couponcode = intent.getStringExtra("coupon_code");
            couponDiscription.setText(HtmlCompat.fromHtml(intent.getStringExtra("coupon_discription"), HtmlCompat.FROM_HTML_MODE_LEGACY));
            txt_couponName.setText(couponcode);
            txt_Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    couponcode = intent.getStringExtra("coupon_code");
                    couponName = intent.getStringExtra("coupon_discription");
                    couponDiscount = intent.getStringExtra("coupon_discount");
                    couponID = intent.getStringExtra("coupon_id");
                    disCount_Typeee = intent.getStringExtra("discount_typee");
                    applyCouponLayout.setBackground(ProceedPay.this.getResources().getDrawable(R.color.orenge));

                    apply_Coupon_Layout.setVisibility(View.GONE);
                    applyed_CouponLayout.setVisibility(View.VISIBLE);
                    txt_couponCode.setText(couponcode);
                    coupon_id = couponID;
                    String[] disCount = coupon_discount.split("-");
                    String newDisCount = disCount[1];
                    String[] Amount = net_amount.split(" ");
                    String NewNetAmount = Amount[1];

                    if (disCount_Typeee.equalsIgnoreCase("percentage")) {
                        int feedouble = Integer.parseInt(couponDiscount);
                        int discountnumber = Integer.parseInt(NewNetAmount);
                        int discountt = (discountnumber / 100) * feedouble;
                        int totalDiscount = Integer.parseInt(newDisCount) + discountt;
                        int finalAmount = Integer.parseInt(NewNetAmount) - discountt;
                        discount = String.valueOf(totalDiscount);
                        disCountLayout.setVisibility(View.VISIBLE);
                        txt_discount.setText("\u20B9" + " -" + discountt);
                        getIntent().putExtra("flag", "0");
                        img_forwordImage.setVisibility(View.GONE);
                        apply_Coupon_Layout.setVisibility(View.GONE);
                        img_clear.setVisibility(View.VISIBLE);
                        applyed_CouponLayout.setVisibility(View.VISIBLE);
                        txt_finalAmount.setText("\u20B9" + " " + finalAmount);
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] amount = nextAmount.split(" ");
                        totalAmount = amount[1];
                        yourPrefrence.saveData("disCount_Typee", disCount_Typeee);
                        yourPrefrence.saveData("total_discount", "\u20B9" + " -" + discount);
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("coupon", "1");
                        dialog.dismiss();

                    } else {
                        int totalDiscount = Integer.parseInt(newDisCount) + Integer.parseInt(couponDiscount);
                        int finalAmount = Integer.parseInt(NewNetAmount) - Integer.parseInt(couponDiscount);
                        discount = String.valueOf(totalDiscount);
                        disCountLayout.setVisibility(View.VISIBLE);
                        txt_discount.setText("\u20B9" + " -" + couponDiscount);
                        getIntent().putExtra("flag", "0");
                        img_forwordImage.setVisibility(View.GONE);
                        apply_Coupon_Layout.setVisibility(View.GONE);
                        img_clear.setVisibility(View.VISIBLE);
                        applyed_CouponLayout.setVisibility(View.VISIBLE);
                        txt_finalAmount.setText("\u20B9" + " " + finalAmount);
                        String nextAmount = txt_finalAmount.getText().toString();
                        String[] amount = nextAmount.split(" ");
                        totalAmount = amount[1];
                        yourPrefrence.saveData("disCount_Typee", disCount_Typeee);
                        yourPrefrence.saveData("total_discount", "\u20B9" + " -" + discount);
                        yourPrefrence.saveData("net_amount", "\u20B9" + " " + totalAmount);
                        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                        yourPrefrence.saveData("coupon", "1");
                        dialog.dismiss();
                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

        }

        if (flag.equals("2")) {
            getIntent().putExtra("flag", "0");
        }

        if (key.equals("1")) {
            img_forwordImage.setVisibility(View.GONE);
            apply_Coupon_Layout.setVisibility(View.GONE);
            img_clear.setVisibility(View.VISIBLE);
            applyed_CouponLayout.setVisibility(View.VISIBLE);
        } else {
            img_forwordImage.setVisibility(View.VISIBLE);
            img_clear.setVisibility(View.GONE);
            apply_Coupon_Layout.setVisibility(View.VISIBLE);
            applyed_CouponLayout.setVisibility(View.GONE);
        }
    }

    private void getWalletAmaunt() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        user_id = yourPrefrence.getData("USERID");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<WalletMoneyResponse> call = serviceInterface.getWalletMoney(map);
        call.enqueue(new Callback<WalletMoneyResponse>() {
            @Override
            public void onResponse(Call<WalletMoneyResponse> call, retrofit2.Response<WalletMoneyResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_userWalletAmount.setText(response.body().getBalance());
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletMoneyResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    public void logAdClickEvent(String userName, String userMobile, String address, String categoryName, String subcategoryName, String childCategoryName, String paymentstatus, String paymentmode) {
        Bundle params = new Bundle();
        params.putString("User Name ", userName);
        params.putString("User Mobile", userMobile);
        params.putString("User Address", address);
        params.putString("Category Name", categoryName);
        params.putString("Payment Type", "Pay Online");
        params.putString("Sub Category Name", subcategoryName);
        params.putString("Child Category Name ", childCategoryName);
        params.putString("Payment Status", childCategoryName);
        logger.logEvent("Order", params);

    }

    public void getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null) {
            }

            location = address.get(0);
            address_latitude = String.valueOf(location.getLatitude());
            address_longitude = String.valueOf(location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }


}