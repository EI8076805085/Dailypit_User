package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dailypit.dp.Adapter.AddOnServiceAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponse;
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData;
import com.dailypit.dp.Model.PayOnlineResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.Model.Wallet.WalletMoneyResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TrackingBuilder;


import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class OrderAssignTo extends AppCompatActivity implements PaymentResultListener {

    ImageView back_img, help_img, homeAddressImage, patnerPhoto, img_callPatner, img_mave_to_mapLayout;
    String addressType, address, totalAmount, discount, finalAmount, orderId, Date, Time, couponName,
            Quentity, ChildCatName, partnerName, partnerMobile, partnerPhoto, service_cate_name,
            order_date, time_slot, payment_Status, temprature, userName, userEmail, userMobile, category, jobStartCode, paymentMode;

    TextView txt_addressType, txt_address, txt_orderId, txt_totalMoney, txt_discountMoney, txt_FinalAmount,
            txt_Date, txt_Time, txt_order_id, txt_totalItemCount, txt_appliedCouponName, txt_PatnerName, txt_serviceCAtegory,
            txt_serviceChildCategory, txt_TotalPayment, total_pay, txt_temperature, btn_Paid, txt_cancleOrder, txt_rescheduleOrder,
            txt_timeSlot, txt_Submit, txt_confirmed, txt_code, txt_service_appliedCouponName, txt_service_discountMoney,
            txt_addnewServicetotalAMount, txt_userWalletAmount, txt_paymentmodeStatus, txt_addOnpaymentmode,txt_notUserWallet,txt_dailypitWallet;

    View txt_view;
    RadioButton rb_onLinePay, rb_peyLator;
    TextView txt_dateReschedule;
    private Integer month = 0;
    private Integer year = 0;
    private Integer day = 0;
    String DOB = "";
    ArrayList<String> time = new ArrayList<>();
    SpinnerDialog spinnerDialogFrom;
    String currentDateTimeString;
    String datefomated, diductWalletAmount = "0";
    ProgressBar mainProgressbar;
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    LinearLayout txt_calendraLayout, txt_timeLayout, add_onServicesLayout, paymentLayout, addOnPaymentModeLayout;
    LinearLayout onLinePayLayout, payLatorLayout, user_walletLayout, trackGenieLayout, trackLayout;
    String paymentMethod = "Online", orderStatus = "0";
    List<AddOnServiceResponseData> addOnServiceResponseData = new ArrayList<>();
    RecyclerView add_newServiceRecyclerView;
    CheckBox cb_userWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        back_img = findViewById(R.id.back_img);
        homeAddressImage = findViewById(R.id.homeAddressImage);
        help_img = findViewById(R.id.help_img);
        img_callPatner = findViewById(R.id.img_callPatner);
        patnerPhoto = findViewById(R.id.patnerPhoto);
        txt_PatnerName = findViewById(R.id.txt_PatnerName);
        txt_serviceCAtegory = findViewById(R.id.txt_serviceCAtegory);
        txt_addressType = findViewById(R.id.txt_addressType);
        txt_address = findViewById(R.id.txt_address);
        txt_orderId = findViewById(R.id.txt_orderID);
        txt_totalMoney = findViewById(R.id.txt_totalMoney);
        txt_discountMoney = findViewById(R.id.txt_discountMoney);
        txt_FinalAmount = findViewById(R.id.txt_FinalAmount);
        txt_Date = findViewById(R.id.txt_Date);
        txt_Time = findViewById(R.id.txt_Time);
        txt_order_id = findViewById(R.id.txt_order_id);
        txt_totalItemCount = findViewById(R.id.txt_totalItemCount);
        txt_appliedCouponName = findViewById(R.id.txt_appliedCouponName);
        txt_serviceChildCategory = findViewById(R.id.txt_serviceChildCategory);
        txt_TotalPayment = findViewById(R.id.txt_TotalPayment);
        total_pay = findViewById(R.id.total_pay);
        btn_Paid = findViewById(R.id.btn_Paid);
        txt_temperature = findViewById(R.id.txt_temperature);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        img_mave_to_mapLayout = findViewById(R.id.img_mave_to_mapLayout);
        txt_cancleOrder = findViewById(R.id.txt_cancleOrder);
        txt_rescheduleOrder = findViewById(R.id.txt_rescheduleOrder);
        txt_confirmed = findViewById(R.id.txt_confirmed);
        txt_code = findViewById(R.id.txt_code);
        onLinePayLayout = findViewById(R.id.onLinePayLayout);
        payLatorLayout = findViewById(R.id.payLatorLayout);
        rb_onLinePay = findViewById(R.id.rb_onLinePay);
        rb_peyLator = findViewById(R.id.rb_peyLator);
        add_onServicesLayout = findViewById(R.id.add_onServicesLayout);
        paymentLayout = findViewById(R.id.paymentLayout);
        txt_service_appliedCouponName = findViewById(R.id.txt_service_appliedCouponName);
        txt_service_discountMoney = findViewById(R.id.txt_service_discountMoney);
        txt_addnewServicetotalAMount = findViewById(R.id.txt_addnewServicetotalAMount);
        add_newServiceRecyclerView = findViewById(R.id.add_newServiceRecyclerView);
        txt_userWalletAmount = findViewById(R.id.txt_userWalletAmount);
        user_walletLayout = findViewById(R.id.user_walletLayout);
        txt_view = findViewById(R.id.txt_view);
        cb_userWallet = findViewById(R.id.cb_userWallet);
        trackGenieLayout = findViewById(R.id.trackGenieLayout);
        txt_paymentmodeStatus = findViewById(R.id.txt_paymentmodeStatus);
        txt_addOnpaymentmode = findViewById(R.id.txt_addOnpaymentmode);
        addOnPaymentModeLayout = findViewById(R.id.addOnPaymentModeLayout);
        trackLayout = findViewById(R.id.trackLayout);
        txt_notUserWallet = findViewById(R.id.txt_notUserWallet);
        txt_dailypitWallet = findViewById(R.id.txt_dailypitWallet);


        addressType = getIntent().getStringExtra("address_type");
        address = getIntent().getStringExtra("address");
        orderId = getIntent().getStringExtra("order_id");
        totalAmount = getIntent().getStringExtra("total_amount");
        discount = getIntent().getStringExtra("discount_count");
        finalAmount = getIntent().getStringExtra("net_amount");
        Date = getIntent().getStringExtra("date");
        Time = getIntent().getStringExtra("time");
        couponName = getIntent().getStringExtra("coupon_name");
        Quentity = getIntent().getStringExtra("quentity");
        ChildCatName = getIntent().getStringExtra("child_cate_name");
        partnerName = getIntent().getStringExtra("partner_name");
        partnerMobile = "tel:" + getIntent().getStringExtra("partner_mobile");
        partnerPhoto = getIntent().getStringExtra("partner_photo");
        service_cate_name = getIntent().getStringExtra("service_cate_name");
        order_date = getIntent().getStringExtra("order_date");
        time_slot = getIntent().getStringExtra("order_time_slot");
        payment_Status = getIntent().getStringExtra("payment_Status");
        temprature = getIntent().getStringExtra("temprature");
        category = getIntent().getStringExtra("category");
        jobStartCode = getIntent().getStringExtra("c_code");
        paymentMode = getIntent().getStringExtra("payment_mode");
        String c_code_verification = getIntent().getStringExtra("c_code_verification");

        if (c_code_verification.equals("1")) {
            trackLayout.setVisibility(View.GONE);
        }


        txt_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Dialog dialog = new Dialog(OrderAssignTo.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.confirm_order_popup);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                dialog.show();

            }
        });

        if (payment_Status.equals("Paid") || payment_Status.equals("Cash Paid")) {
            paymentLayout.setVisibility(View.GONE);
            btn_Paid.setText("Paid");
            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.gre_background));
            if (paymentMode.equals("Pay Online")) {
                txt_paymentmodeStatus.setText(paymentMode);
            } else {
                txt_paymentmodeStatus.setText("Cash Pay");
            }

        } else {
            btn_Paid.setText("Pay Now");
            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
            user_walletLayout.setVisibility(View.VISIBLE);
            txt_view.setVisibility(View.VISIBLE);
            txt_paymentmodeStatus.setText(paymentMode);
        }

        img_callPatner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(partnerMobile));
                startActivity(intent);
            }
        });

        if (addressType.equals("Work")) {
            homeAddressImage.setImageResource(R.drawable.address_work);
        } else if (addressType.equals("Other")) {
            homeAddressImage.setImageResource(R.drawable.edit_location);
        } else {
            homeAddressImage.setImageResource(R.drawable.ic_address_home);
        }


        List<String> quantityList = Arrays.asList(Quentity.split(","));

        int sum = 0;
        for (String value : quantityList) {
            sum += Integer.parseInt(value);
        }

        List<String> itemNumberParentList = Arrays.asList(Quentity);
        List<String> itemNameParentList = Arrays.asList(ChildCatName);

        String totalValue = "";
        for (int i = 0; i < itemNameParentList.size(); i++) {
            List<String> itemNumberList = Arrays.asList(itemNumberParentList.get(i).split(","));
            List<String> itemNameList = Arrays.asList(itemNameParentList.get(i).split(","));
            for (int j = 0; j < itemNumberList.size(); j++) {

                if (j==itemNumberList.size()-1)
                {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n";

                }
                else {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n\n⦿";
                }   }
        }

        txt_addressType.setText(addressType);
        txt_address.setText(address);
        txt_orderId.setText(orderId);
        txt_totalMoney.setText("\u20B9" + " " + totalAmount);
        txt_discountMoney.setText("\u20B9" + " -" + discount);
        txt_FinalAmount.setText("\u20B9" + " " + finalAmount);
        txt_TotalPayment.setText("\u20B9" + " " + finalAmount);
        total_pay.setText(finalAmount);
        txt_Date.setText(order_date);
        txt_Time.setText(time_slot);
        txt_order_id.setText("Order Id "+ orderId);
        txt_totalItemCount.setText("X " + sum);
        txt_appliedCouponName.setText(couponName);
        txt_PatnerName.setText(partnerName);
        txt_serviceCAtegory.setText(category);
        txt_serviceChildCategory.setText("⦿ "+totalValue);
        txt_temperature.setText((temprature) + " \u2103");
        Glide.with(getApplicationContext()).load(IMAGE + partnerPhoto).into(patnerPhoto);
        txt_code.setText(jobStartCode);


        btn_Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payOldOrder();
            }
        });

        img_mave_to_mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teliver.init(OrderAssignTo.this, "1f05438a3d0ba738700a181b345d9f62");
                MarkerOption option = new MarkerOption(orderId);
                option.setMarkerTitle("Dailypit Genie");
                option.setMarkerSnippet("I'm on my way");
                option.setIconMarker(R.drawable.app_genie);
                Teliver.startTracking(new TrackingBuilder(option).withTitle("Track").build());

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        help_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAssignTo.this, Help.class);
                intent.putExtra("Order_id", orderId);
                startActivity(intent);
            }
        });

        txt_cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAssignTo.this, CancleOrderActivity.class);
                startActivity(intent);
            }
        });

        txt_rescheduleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(OrderAssignTo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.reschedule_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
                SimpleDateFormat dateee = new SimpleDateFormat("dd-MM-yyyy");
                currentDateTimeString = sdf.format(date);
                datefomated = dateee.format(date);

                txt_calendraLayout = dialog.findViewById(R.id.txt_calendraLayout);
                txt_timeLayout = dialog.findViewById(R.id.txt_timeLayout);
                txt_timeSlot = dialog.findViewById(R.id.txt_timeSlot);
                txt_dateReschedule = dialog.findViewById(R.id.txt_dateReschedule);
                txt_Submit = dialog.findViewById(R.id.txt_Submit);

                txt_timeSlot.setText(currentDateTimeString);
                txt_dateReschedule.setText(datefomated);

                gettimeslot(datefomated, datefomated);

                txt_timeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinnerDialogFrom.showSpinerDialog();
                    }
                });

                txt_calendraLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCelender(OrderAssignTo.this, year, month, day, txt_dateReschedule);
                    }
                });

                txt_Submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderAssignTo.this, RescheduleOrderActivity.class);
                        startActivity(intent);
                    }
                });


                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });

        rb_onLinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_walletLayout.setClickable(true);
                cb_userWallet.setClickable(true);
                txt_dailypitWallet.setTextColor(Color.parseColor("#000000"));
                user_walletLayout.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.whilte_background));


                btn_Paid.setText("Pay Now");
                btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                rb_onLinePay.setChecked(true);
                rb_peyLator.setChecked(false);
                paymentMethod = "Online";

                if (user_walletLayout.getVisibility() == View.GONE) {
                    user_walletLayout.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.VISIBLE);
                }
            }
        });

        rb_peyLator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_dailypitWallet.setVisibility(View.VISIBLE);
                user_walletLayout.setVisibility(View.VISIBLE);

                user_walletLayout.setClickable(false);
                cb_userWallet.setClickable(false);

                txt_dailypitWallet.setTextColor(Color.parseColor("#cacaca"));
                user_walletLayout.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.blur_background));

                String walletAmount = txt_userWalletAmount.getText().toString();
                String nextAmount = total_pay.getText().toString();

                if (cb_userWallet.isChecked()) {
                    int payAmount = Integer.parseInt(nextAmount) + (Integer.parseInt(diductWalletAmount));
                    total_pay.setText(payAmount + "");
                    cb_userWallet.setChecked(false);
                    btn_Paid.setText("Submit");
                    btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Lator";
                   // user_walletLayout.setVisibility(View.GONE);
                    txt_view.setVisibility(View.GONE);

                } else {
                    btn_Paid.setText("Submit");
                    btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Lator";
                   // user_walletLayout.setVisibility(View.GONE);
                    txt_view.setVisibility(View.GONE);

                }
            }
        });

        onLinePayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_walletLayout.setClickable(true);
                cb_userWallet.setClickable(true);
                txt_dailypitWallet.setTextColor(Color.parseColor("#000000"));
                user_walletLayout.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.whilte_background));

                btn_Paid.setText("Pay Now");
                btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));

                rb_onLinePay.setChecked(true);
                rb_peyLator.setChecked(false);
                paymentMethod = "Online";

                if (user_walletLayout.getVisibility() == View.GONE) {
                    user_walletLayout.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.VISIBLE);
                }

            }
        });

        payLatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_dailypitWallet.setVisibility(View.VISIBLE);
                user_walletLayout.setVisibility(View.VISIBLE);

                user_walletLayout.setClickable(false);
                cb_userWallet.setClickable(false);

                txt_dailypitWallet.setTextColor(Color.parseColor("#cacaca"));
                user_walletLayout.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.blur_background));
                String walletAmount = txt_userWalletAmount.getText().toString();
                String nextAmount = total_pay.getText().toString();

                if (cb_userWallet.isChecked()) {
                    int payAmount = Integer.parseInt(nextAmount) + (Integer.parseInt(diductWalletAmount));
                    total_pay.setText(payAmount + "");
                    cb_userWallet.setChecked(false);
                    btn_Paid.setText("Submit");
                    btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Lator";
                   // user_walletLayout.setVisibility(View.GONE);
                    txt_view.setVisibility(View.GONE);

                } else {
                    btn_Paid.setText("Submit");
                    btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                    rb_onLinePay.setChecked(false);
                    rb_peyLator.setChecked(true);
                    paymentMethod = "Lator";
                  //  user_walletLayout.setVisibility(View.GONE);
                    txt_view.setVisibility(View.GONE);

                }
            }
        });

        cb_userWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walletAmount = txt_userWalletAmount.getText().toString();
                String nextAmount = total_pay.getText().toString();

                if (cb_userWallet.isChecked()) {
                    if (Integer.parseInt(walletAmount) > Integer.parseInt(nextAmount)) {
                        int payAmount = (Integer.parseInt(walletAmount) - Integer.parseInt(nextAmount));
                        diductWalletAmount = nextAmount;
                        total_pay.setText("0");
                    } else {
                        int payAmount = Integer.parseInt(nextAmount) - (Integer.parseInt(walletAmount));
                        total_pay.setText(payAmount + "");
                        diductWalletAmount = walletAmount;
                    }
                } else {
                    int payAmount = Integer.parseInt(nextAmount) + (Integer.parseInt(diductWalletAmount));
                    total_pay.setText(payAmount + "");
                }
            }
        });

        trackGenieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teliver.init(OrderAssignTo.this, "1f05438a3d0ba738700a181b345d9f62");
                MarkerOption option = new MarkerOption(orderId);
                option.setMarkerTitle("Dailypit Genie");
                option.setMarkerSnippet("I'm on my way");
                option.setIconMarker(R.drawable.app_genie);
                Teliver.startTracking(new TrackingBuilder(option).withTitle("Track").build());

            }
        });

        getAddONServices(orderId);

    }

    private void payOldOrder() {
        String status = btn_Paid.getText().toString();
        if (rb_peyLator.isChecked()) {
            if (status.equals("Submit")) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String userId = yourPrefrence.getData("USERID");
                if (userId.isEmpty()) {
                    Toast.makeText(OrderAssignTo.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                } else if (orderId.isEmpty()) {
                    Toast.makeText(OrderAssignTo.this, "Order id is required", Toast.LENGTH_SHORT).show();
                } else {
                    payLator();
                }
            }
        } else {
            if (status.equals("Pay Now")) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String userId = yourPrefrence.getData("USERID");
                if (userId.isEmpty()) {
                    Toast.makeText(OrderAssignTo.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                } else if (orderId.isEmpty()) {
                    Toast.makeText(OrderAssignTo.this, "Order id is required", Toast.LENGTH_SHORT).show();
                } else {
                    payOnline();
                }
            }
        }

    }

    private void openCelender(OrderAssignTo dashboard, Integer year, Integer month, Integer day, TextView txt_dateReschedule) {

        final Calendar c;
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog mDatePicker = new DatePickerDialog(dashboard, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                if (selectedmonth < 9) {
                    DOB = selectedday + "-" + "0" + (selectedmonth + 1) + "-" + selectedyear;
                } else {
                    DOB = selectedday + "-" + (selectedmonth + 1) + "-" + selectedyear;
                }

                txt_dateReschedule.setText(DOB);
                gettimeslot(datefomated, DOB);

                //  datetime.setText(new StringBuilder().append(selectedday).append("-").append(selectedmonth + 1).append("-").append(selectedyear));
            }
        }, year, month, day);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.setTitle("Please Select Date");
        mDatePicker.show();
    }

    private void payOnline() {
        Checkout checkout = new Checkout();
        final Activity activity = OrderAssignTo.this;
        int totalPay = Integer.parseInt(total_pay.getText().toString());
        try {
            JSONObject options = new JSONObject();
            options.put("name", userName);
            options.put("currency", "INR");
            options.put("prefill.email", userEmail);
            options.put("prefill.contact", userMobile);
            options.put("amount", totalPay * 100);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfile();
        getWalletAmaunt();

    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(OrderAssignTo.this);
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
                    } else {
                        Toast.makeText(OrderAssignTo.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(OrderAssignTo.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getWalletAmaunt() {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String user_id = yourPrefrence.getData("USERID");

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
                        // txt_userWalletAmount.setText("4000");
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        if (razorpayPaymentID.isEmpty()) {
            Toast.makeText(OrderAssignTo.this, "Failed Payment", Toast.LENGTH_SHORT).show();
        } else {
            paymentsuccess(razorpayPaymentID);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    private void paymentsuccess(String razorpayPaymentID) {
        String payment_for = "";
        if (orderStatus.equals("0")) {
            payment_for = "order";
        } else {
            payment_for = "addon_order";
        }

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(OrderAssignTo.this);
        String userId = yourPrefrence.getData("USERID");
        map.put("payment_for", payment_for);
        map.put("user_id", userId);
        map.put("order_id", orderId);
        map.put("payment_mode", "Pay Online");
        map.put("payment_status", "Paid");
        map.put("transaction_id", razorpayPaymentID);
        map.put("wallet_amount", diductWalletAmount);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PayOnlineResponse> call = serviceInterface.payOnline(map);
        call.enqueue(new Callback<PayOnlineResponse>() {
            @Override
            public void onResponse(Call<PayOnlineResponse> call, retrofit2.Response<PayOnlineResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        String payment_Status = response.body().getPaymentStatus();
                        if (payment_Status.equals("Paid")) {
                            String addonPaymentmode = response.body().getPaymentMode();
                            addOnPaymentModeLayout.setVisibility(View.VISIBLE);
                            if (addonPaymentmode.equals("Pay Online")) {
                                txt_addOnpaymentmode.setText(addonPaymentmode);
                                txt_paymentmodeStatus.setText(addonPaymentmode);
                            } else {
                                txt_addOnpaymentmode.setText("Cash Pay");
                                txt_paymentmodeStatus.setText("Cash Pay");
                            }
                            user_walletLayout.setVisibility(View.GONE);
                            txt_view.setVisibility(View.GONE);
                            paymentLayout.setVisibility(View.GONE);
                            btn_Paid.setText("Paid");
                            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.gre_background));
                        } else {
                            btn_Paid.setText("Pay Now");
                            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));
                        }
                    } else {
                        Toast.makeText(OrderAssignTo.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(OrderAssignTo.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PayOnlineResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }



    public void gettimeslot(String currentdatecroped, String libdate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate = dateFormat.parse(currentdatecroped);
            convertedDate2 = dateFormat.parse(libdate);

            if (convertedDate2.after(convertedDate)) {
                time.clear();
                time.add("Please Select Time Slot");
                time.add("8:00 AM - 10:00 AM");
                time.add("10:00 AM - 12:00 PM");
                time.add("12:00 PM - 3:00 PM");
                time.add("3:00 PM - 6:00 PM");
                time.add("6:00 PM - 9:00 PM");

                txt_timeSlot.setText("Please Select Time Slot");

                spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                spinnerDialogFrom.setCancellable(true); // for cancellable
                spinnerDialogFrom.setShowKeyboard(false); // for open keyboard by default
                spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        txt_timeSlot.setText(item);
                    }
                });

            } else {
                String timecrop = currentDateTimeString.substring(0, 2);
                if (Integer.parseInt(timecrop) < 4) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("8:00 AM - 10:00 AM");
                    time.add("10:00 AM - 12:00 PM");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });

                } else if (Integer.parseInt(timecrop) < 8) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("10:00 AM - 12:00 PM");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });

                } else if (Integer.parseInt(timecrop) < 10) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });

                } else if (Integer.parseInt(timecrop) < 12) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });
                } else if (Integer.parseInt(timecrop) < 15) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });

                } else {
                    time.clear();
                    time.add("Slot not available, Please choose another date!");
                    txt_timeSlot.setText("Slot not available, Please choose another date!");

                    spinnerDialogFrom = new SpinnerDialog(OrderAssignTo.this, time, "Choose ", R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
                    spinnerDialogFrom.setCancellable(true); // for cancellable
                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String item, int position) {
                            txt_timeSlot.setText(item);
                        }
                    });
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getAddONServices(String orderId) {
        addOnServiceResponseData.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("p_order_id", orderId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<AddOnServiceResponse> call = serviceInterface.addNewServicesCnfed(map);
        call.enqueue(new Callback<AddOnServiceResponse>() {
            @Override
            public void onResponse(Call<AddOnServiceResponse> call, retrofit2.Response<AddOnServiceResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        addOnServiceResponseData = response.body().getData();
                        bindData(addOnServiceResponseData, response.body().getDiscountAmount(), response.body().getNetAmount(), orderId, response.body().getAddonpaymentmode(), response.body().getAddonpaymentstatus());
                    }
                } else {
                    Toast.makeText(OrderAssignTo.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddOnServiceResponse> call, Throwable t) {
                Log.d("ff", t.toString());

            }
        });
    }

    private void bindData(List<AddOnServiceResponseData> addOnServiceResponseData, String discountAmount, String netAmount, String orderId, String addonPaymentmode, String addonPaymentstatus) {

        if (addonPaymentstatus.equals("Paid") || addonPaymentstatus.equals("Cash Paid")) {
            addOnPaymentModeLayout.setVisibility(View.VISIBLE);

            if (addonPaymentmode.equals("Pay Online")) {
                txt_addOnpaymentmode.setText(addonPaymentmode);
            } else {
                txt_addOnpaymentmode.setText("Cash Pay");
            }

            if (user_walletLayout.getVisibility() == View.VISIBLE) {
                user_walletLayout.setVisibility(View.GONE);
                txt_view.setVisibility(View.GONE);
            } else {
                user_walletLayout.setVisibility(View.GONE);
                txt_view.setVisibility(View.GONE);
            }

            int oldAmount = Integer.parseInt(finalAmount) + Integer.parseInt(netAmount);
            total_pay.setText(oldAmount + "");

        } else {
            orderStatus = "1";
            paymentLayout.setVisibility(View.VISIBLE);
            btn_Paid.setText("Pay Now");
            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.btn_background));

            if (user_walletLayout.getVisibility() == View.GONE) {
                user_walletLayout.setVisibility(View.VISIBLE);
                txt_view.setVisibility(View.VISIBLE);
            } else {
                user_walletLayout.setVisibility(View.VISIBLE);
                txt_view.setVisibility(View.VISIBLE);
            }
        }

        if (payment_Status.equals("Paid")) {
            total_pay.setText(netAmount);
        } else {
            int oldAmount = Integer.parseInt(finalAmount) + Integer.parseInt(netAmount);
            total_pay.setText(oldAmount + "");
        }

        add_onServicesLayout.setVisibility(View.VISIBLE);
        txt_service_appliedCouponName.setText("");
        txt_service_discountMoney.setText(discountAmount);
        txt_addnewServicetotalAMount.setText(netAmount);

        AddOnServiceAdapter adapter = new AddOnServiceAdapter(addOnServiceResponseData);
        add_newServiceRecyclerView.setHasFixedSize(true);
        add_newServiceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        add_newServiceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void payLator() {
        String payment_for = "";
        if (orderStatus.equals("0")) {
            payment_for = "order";
        } else {
            payment_for = "addon_order";
        }

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(OrderAssignTo.this);
        String userId = yourPrefrence.getData("USERID");
        map.put("payment_for", payment_for);
        map.put("user_id", userId);
        map.put("order_id", orderId);
        map.put("payment_mode", "Pay later");
        map.put("payment_status", "Cash Paid");
        map.put("transaction_id", "");
        map.put("wallet_amount", "0");

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PayOnlineResponse> call = serviceInterface.payOnline(map);
        call.enqueue(new Callback<PayOnlineResponse>() {
            @Override
            public void onResponse(Call<PayOnlineResponse> call, retrofit2.Response<PayOnlineResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        String payment_Status = response.body().getPaymentStatus();
                        if (payment_Status.equals("Cash Paid")) {
                            String addonPaymentmode = response.body().getPaymentMode();
                            addOnPaymentModeLayout.setVisibility(View.VISIBLE);
                            if (addonPaymentmode.equals("Pay Online")) {
                                txt_addOnpaymentmode.setText(addonPaymentmode);
                                txt_paymentmodeStatus.setText(addonPaymentmode);
                            } else {
                                txt_addOnpaymentmode.setText("Cash Pay");
                                txt_paymentmodeStatus.setText("Cash Pay");
                            }

                            user_walletLayout.setVisibility(View.GONE);
                            txt_view.setVisibility(View.GONE);
                            paymentLayout.setVisibility(View.GONE);
                            btn_Paid.setText("Paid");
                            btn_Paid.setBackground(OrderAssignTo.this.getResources().getDrawable(R.drawable.gre_background));

                        }
                    } else {
                        Toast.makeText(OrderAssignTo.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(OrderAssignTo.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PayOnlineResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }
}