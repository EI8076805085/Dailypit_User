package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dailypit.dp.Adapter.MemberShipAdapter;
import com.dailypit.dp.Adapter.MembershipServicePlanAdapter;
import com.dailypit.dp.Adapter.PlanDetailsAdapter;
import com.dailypit.dp.Adapter.ServiceChildCategoryAdapter;
import com.dailypit.dp.Adapter.StateAdapter;
import com.dailypit.dp.Adapter.TimeAdapter;
import com.dailypit.dp.Interface.ChildCategoryListner;
import com.dailypit.dp.Interface.MemberShipPlayListner;
import com.dailypit.dp.Interface.SelectTimeListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Interface.ServiceItemChargListner;
import com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan.CheckMemberShipPlan;
import com.dailypit.dp.Model.MemberShip.MemberShipContentList;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListResponse;
import com.dailypit.dp.Model.MemberShip.MemberShipPlanListdata;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsData;
import com.dailypit.dp.Model.MemberShip.PlanDetails.PlanDetailsResponse;
import com.dailypit.dp.Model.Service.ServiceChildCategoryResponse;
import com.dailypit.dp.Model.Service.ServicesChildCategoryData;
import com.dailypit.dp.Model.State.StateResponseData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import static com.dailypit.dp.Utils.Constants.IMAGE;

public class ServiceAcitvity extends AppCompatActivity implements ServiceItemChargListner, ChildCategoryListner, SelectTimeListner, MemberShipPlayListner {

    private int month = 0;
    private int year  = 0;
    private int day = 0 ;
    SpinnerDialog spinnerDialogFrom;
    ArrayList<String> time = new ArrayList<>();
    String total,serviceDiscription,couponDiscount = "",DOB,category, memberShipId = "0";
    TextView txt_service_discription, txt_itemTotal, txt_payTotalMoney, txt_discount,txt_ServicesName,
            btn_ProceedToPay,txt_timeSlot,txt_close,txt_up_toDiscount,txt_membership_discount,txt_membership_charge;
    ImageView back_img,img_service,calendra_img,img_clock,img_clearDialog,img_forword,img_clear;
    RecyclerView service_childCategoryRecyclerView;
    String serviceSubCategoryID, couponCharg, discountfee, couponcode = "0",serviceName,serviceImage,discount,discountType,multiCoupon;
    ProgressBar mainProgressbar,mainProgressbarDialog;
    List<ServicesChildCategoryData> serviceChildCategoryList = new ArrayList<>();
    List<Integer> minvaluelist = new ArrayList<>();
    DatabaseHandler databaseHandler;
    LinearLayout discountsection,applyCouponLayout,memberShipLayout,apply_MemberShip_Layout,applyed_MemberShipLayout,membership_discount_layout,membership_charge_layout;
    View multiviewdiscount,multidisView,membershipView,membershipDiscountView;
    String currentDateTimeString,libdate,selectedDate = "",childCategory;
    AppEventsLogger logger;
    RecyclerView rv_simple_list;
    RecyclerView membership_service_list_response, memberShipPlanRecyclerView, membershipPlayRecyclerView, memberShipPlanBottomRecyclerView;
    public static List<String> contentList = new ArrayList<String>();
    public static List<MemberShipPlanListdata> memberShipPlanListdata = new ArrayList<>();
    List<PlanDetailsData> planDetailsData = new ArrayList<>();
    Dialog memberShipdialog;
    String membership_status = "";
    Date date,date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_acitvity);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        setUpDB();

        btn_ProceedToPay = findViewById(R.id.btn_ProceedToPay);
        back_img = findViewById(R.id.back_img);
        img_service = findViewById(R.id.img_service);
        txt_ServicesName = findViewById(R.id.txt_ServicesName);
        calendra_img = findViewById(R.id.calendra_img);
        img_clock = findViewById(R.id.img_clock);
        txt_service_discription = findViewById(R.id.txt_service_discription);
        txt_timeSlot = findViewById(R.id.txt_timeSlot);
        txt_discount = findViewById(R.id.txt_discount);
        applyCouponLayout = findViewById(R.id.applyCouponLayout);
        txt_itemTotal = findViewById(R.id.txt_itemTotal);
        txt_payTotalMoney = findViewById(R.id.txt_payTotalMoney);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        service_childCategoryRecyclerView = findViewById(R.id.service_childCategoryRecyclerView);
        discountsection=findViewById(R.id.discountsection);
        multiviewdiscount=findViewById(R.id.multiviewdiscount);
        memberShipLayout=findViewById(R.id.memberShipLayout);
        txt_membership_discount=findViewById(R.id.txt_membership_discount);
        txt_membership_charge=findViewById(R.id.txt_membership_charge);
        img_forword=findViewById(R.id.img_forword);
        img_clear=findViewById(R.id.img_clear);
        apply_MemberShip_Layout=findViewById(R.id.apply_MemberShip_Layout);
        applyed_MemberShipLayout=findViewById(R.id.applyed_MemberShipLayout);
        membership_discount_layout=findViewById(R.id.membership_discount_layout);
        membership_charge_layout=findViewById(R.id.membership_charge_layout);
        multidisView=findViewById(R.id.multidisView);
        membershipView=findViewById(R.id.membershipView);
        membershipDiscountView=findViewById(R.id.membershipDiscountView);
        logger = AppEventsLogger.newLogger(this);

        YourPreference yourPrefrence = YourPreference.getInstance(ServiceAcitvity.this);
        String CategoryID = yourPrefrence.getData("CategoryID");
        Calendar startDate = Calendar.getInstance();

//        if(CategoryID.equalsIgnoreCase("20")){
//            startDate.add(Calendar.DATE, 7);
//            long DAY_IN_MS = 1000 * 60 * 60 * 24;
//            date = new Date(System.currentTimeMillis() + (7 * DAY_IN_MS));
//            date1 = new Date();
//        } else {
//            startDate.add(Calendar.DATE, 0);
//            date = new Date();
//            date1 = new Date();
//        }

        startDate.add(Calendar.DATE, 0);
        date = new Date();
        date1 = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        SimpleDateFormat dateee = new SimpleDateFormat("dd-MM-yyyy");
        currentDateTimeString = sdf.format(date);
        String datefomated = dateee.format(date);
        String datefomate = dateee.format(date1);
        libdate = datefomate;
        selectedDate = datefomated;

//        if(CategoryID.equalsIgnoreCase("20")){
//            gettimeslott(datefomated,libdate);
//        } else {
//            gettimeslot(datefomated,libdate);
//        }

        gettimeslot(datefomated,libdate);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 10);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH);
                day = date.get(Calendar.DATE);
                if (month < 9) {
                    DOB = day + "-" + "0" + (month + 1) + "-" + year;
                } else {
                    DOB = day + "-" + (month + 1) + "-" + year;
                }
                 selectedDate = DOB;
                 gettimeslot(libdate,DOB);

            }
        });

        category = getIntent().getStringExtra("Category_Name");
        serviceName = getIntent().getStringExtra("Service_Name");
        serviceImage = getIntent().getStringExtra("Service_Image");
        serviceDiscription = getIntent().getStringExtra("service_discription");
        serviceSubCategoryID = yourPrefrence.getData("subCategoryID");
        couponCharg = yourPrefrence.getData("COUPONCHARGE");
        discountfee = yourPrefrence.getData("discountFee");
        total = yourPrefrence.getData("TOTAL");
        couponcode = yourPrefrence.getData("COUPONCODE");
        multiCoupon = yourPrefrence.getData("multiCoupon");
        discount = yourPrefrence.getData("discount");
        discountType = yourPrefrence.getData("discountType");

        if (multiCoupon.equalsIgnoreCase("0")) {
            discountsection.setVisibility(View.GONE);
            multiviewdiscount.setVisibility(View.GONE);
        }

        txt_ServicesName.setText(serviceName);
        txt_service_discription.setText(HtmlCompat.fromHtml(serviceDiscription, HtmlCompat.FROM_HTML_MODE_LEGACY));
        Glide.with(getApplicationContext()).load(IMAGE +serviceImage).into(img_service);
        getServiceChildCategory(serviceSubCategoryID);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        txt_timeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // spinnerDialogFrom.showSpinerDialog();
                openTimeDialog(time);
            }
        });

        img_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(time);
               // spinnerDialogFrom.showSpinerDialog();
            }
        });

//        spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//        spinnerDialogFrom.setCancellable(false); // for cancellable
//        spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//        spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//            @Override
//            public void onClick(String item, int position) {
//                txt_timeSlot.setText(item);
//            }
//        });

        btn_ProceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDiscount = txt_discount.getText().toString();
                if(txt_itemTotal.getText().toString().equals("\u20B9"+" "+0)){
                    Toast.makeText(ServiceAcitvity.this, "Please select service", Toast.LENGTH_SHORT).show();
                } else if(selectedDate.isEmpty()) {
                    Toast.makeText(ServiceAcitvity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("")){
                    Toast.makeText(ServiceAcitvity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("Please Select Time Slot")){
                    Toast.makeText(ServiceAcitvity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("Slot not available, Please choose another date!")){
                    Toast.makeText(ServiceAcitvity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ServiceAcitvity.this, ProceedPay.class);
                    logAdClickEvent2();

                    String total_Amount = txt_itemTotal.getText().toString();
                    String net_Amount = txt_payTotalMoney.getText().toString();
                    YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                    yourPrefrence.saveData("coupon","0");
                    intent.putExtra("flag", "0");
                    yourPrefrence.saveData("total_discount",couponDiscount);
                    yourPrefrence.saveData("multi_discount",couponDiscount);
                    yourPrefrence.saveData("final_amount",total_Amount);
                    yourPrefrence.saveData("net_amount",net_Amount);
                    yourPrefrence.saveData("date",selectedDate);
                    yourPrefrence.saveData("timeSlot",txt_timeSlot.getText().toString());
                    yourPrefrence.saveData("categoryName",category);
                    yourPrefrence.saveData("subCategoryName",serviceName);
                    yourPrefrence.saveData("childCategoryname",childCategory);
                    yourPrefrence.saveData("membershipDiscount",txt_membership_discount.getText().toString());
                    yourPrefrence.saveData("membershipDiscountCharg",txt_membership_charge.getText().toString());
                    yourPrefrence.saveData("memberShipId",memberShipId);

                    startActivity(intent);
                }
            }
        });

        applyCouponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceAcitvity.this, ShowCoupon.class);
                startActivity(intent);
            }
        });

        img_forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberShipdialog = new Dialog(ServiceAcitvity.this);
                memberShipdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                memberShipdialog.setContentView(R.layout.membership_dialog_layout);
                memberShipdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                String total_amt = txt_payTotalMoney.getText().toString();
                String split[] = total_amt.split(" ");
                String total_amtt = String.valueOf(Integer.parseInt(split[1]));
                yourPrefrence.saveData("LastAmount", total_amtt);

                membership_service_list_response = memberShipdialog.findViewById(R.id.membership_service_list_response);
                memberShipPlanRecyclerView = memberShipdialog.findViewById(R.id.memberShipPlanRecyclerView);
                membershipPlayRecyclerView = memberShipdialog.findViewById(R.id.membershipPlayRecyclerView);
                memberShipPlanBottomRecyclerView = memberShipdialog.findViewById(R.id.memberShipPlanBottomRecyclerView);
                txt_up_toDiscount = memberShipdialog.findViewById(R.id.txt_up_toDiscount);
                mainProgressbarDialog = memberShipdialog.findViewById(R.id.mainProgressbarDialog);
                img_clearDialog = memberShipdialog.findViewById(R.id.img_clearDialog);

                img_clearDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memberShipdialog.dismiss();
                    }
                });


                getContentList(txt_up_toDiscount,membership_service_list_response,mainProgressbarDialog);
                getMemberShipPlanList(memberShipPlanRecyclerView,memberShipPlanBottomRecyclerView,mainProgressbarDialog);
                getPlanDetails(membershipPlayRecyclerView,mainProgressbarDialog);

                memberShipdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                memberShipdialog.show();

            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_forword.setVisibility(View.VISIBLE);
                img_clear.setVisibility(View.GONE);
                apply_MemberShip_Layout.setVisibility(View.VISIBLE);

                applyed_MemberShipLayout.setVisibility(View.GONE);
                membership_discount_layout.setVisibility(View.GONE);
                membership_charge_layout.setVisibility(View.GONE);
                memberShipLayout.setBackground(ServiceAcitvity.this.getResources().getDrawable(R.color.sub));
                memberShipId = "0";
                membership_status = "0";

                YourPreference yourPrefrence = YourPreference.getInstance(ServiceAcitvity.this);
                String lastAmount = yourPrefrence.getData("LastAmount");
                yourPrefrence.saveData("memberShipId","");

                String mem_charg = txt_membership_charge.getText().toString();
                String splitt[] = mem_charg.split(" ");
                int total_amttt = Integer.parseInt(splitt[1]);

                String lastPay = txt_itemTotal.getText().toString();
                String splittt[] = lastPay.split(" ");
                int lastamount2 = Integer.parseInt(splittt[1]);

                String discount = txt_discount.getText().toString();
                String dis[] = discount.split("-");
                int lastamount3 = Integer.parseInt(dis[1]);

                int  nextAmount  = lastamount2 - lastamount3;

                txt_membership_charge.setText("\u20B9" +" "+"0");

                String dis_amt = yourPrefrence.getData("Discount_Amount");
                if (yourPrefrence.getData("Discount_Type").equalsIgnoreCase("flat"))
                {
                    txt_membership_discount.setText("\u20B9" + " " + "0");
                    String total_amt = txt_payTotalMoney.getText().toString();
                    String split[] = total_amt.split(" ");
                    int total_amtt = Integer.parseInt(split[1]);
                    int disvalue = total_amtt + Integer.parseInt(dis_amt);
                    int nestAmount  = disvalue - total_amttt;
                    txt_payTotalMoney.setText("\u20B9" + " " + nestAmount+"");

                } else {
                    String total_amt = txt_payTotalMoney.getText().toString();
                    String split[] = total_amt.split(" ");
                    int total_amtt = Integer.parseInt(split[1]);
                    //int disvalue = Integer.parseInt(lastAmount)*Integer.parseInt(dis_amt)/100;
                    int disvalue = nextAmount*Integer.parseInt(dis_amt)/100;
                    txt_membership_discount.setText("\u20B9" + " " +"0");
                    int final_amount =  total_amtt + disvalue - total_amttt;
                    txt_payTotalMoney.setText("\u20B9" + " " +final_amount+"");

                }
            }
        });

    }

    private void getContentList(TextView txt_up_toDiscount, RecyclerView membership_service_list_response, ProgressBar mainProgressbarDialog) {
        contentList.clear();
        mainProgressbarDialog.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MemberShipContentList> call = serviceInterface.getContentList();
        call.enqueue(new Callback<MemberShipContentList>() {
            @Override
            public void onResponse(Call<MemberShipContentList> call, retrofit2.Response<MemberShipContentList> response) {
                if (response.isSuccessful()) {
                    mainProgressbarDialog.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        txt_up_toDiscount.setVisibility(View.VISIBLE);
                        txt_up_toDiscount.setText(response.body().getDiscount());
                        String content = response.body().getDescription();

                        List<String> itemNameParentList = Arrays.asList(content);
                        for (int i = 0; i < itemNameParentList.size(); i++) {
                            List<String> itemNameList  = Arrays.asList(itemNameParentList.get(i).split(","));
                            for (int j = 0; j < itemNameList.size(); j++) {
                                contentList.add(itemNameList.get(j));
                            }
                        }

                        MembershipServicePlanAdapter adapter = new MembershipServicePlanAdapter(contentList,getApplicationContext());
                        membership_service_list_response.setHasFixedSize(true);
                        membership_service_list_response.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        membership_service_list_response.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                } else {
                    mainProgressbarDialog.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberShipContentList> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbarDialog.setVisibility(View.GONE);
            }
        });
    }

    private  void getMemberShipPlanList(RecyclerView memberShipPlanRecyclerView, RecyclerView memberShipPlanBottomRecyclerView, ProgressBar mainProgressbarDialog) {
        mainProgressbarDialog.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<MemberShipPlanListResponse> call = serviceInterface.getMemberShipPlanList();
        call.enqueue(new Callback<MemberShipPlanListResponse>() {
            @Override
            public void onResponse(Call<MemberShipPlanListResponse> call, retrofit2.Response<MemberShipPlanListResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbarDialog.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        memberShipPlanListdata = response.body().getData();
                        bindPlan(memberShipPlanListdata,memberShipPlanRecyclerView);
                        bindPlanBottom(memberShipPlanListdata,memberShipPlanBottomRecyclerView);
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberShipPlanListResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbarDialog.setVisibility(View.GONE);
            }
        });
    }

    private  void bindPlan(List<MemberShipPlanListdata> memberShipPlanListdata, RecyclerView memberShipPlanRecyclerView) {
        MemberShipAdapter adapter = new MemberShipAdapter(memberShipPlanListdata,"1", getApplicationContext(), this);
        memberShipPlanRecyclerView.setHasFixedSize(true);
        memberShipPlanRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        memberShipPlanRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private  void bindPlanBottom(List<MemberShipPlanListdata> memberShipPlanListdata, RecyclerView memberShipPlanBottomRecyclerView) {
        MemberShipAdapter adapter = new MemberShipAdapter(memberShipPlanListdata,"1" ,getApplicationContext(), this);
        memberShipPlanBottomRecyclerView.setHasFixedSize(true);
        memberShipPlanBottomRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        memberShipPlanBottomRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getPlanDetails(RecyclerView membershipPlayRecyclerView, ProgressBar mainProgressbarDialog) {
        mainProgressbarDialog.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PlanDetailsResponse> call = serviceInterface.getPlanDiscount();
        call.enqueue(new Callback<PlanDetailsResponse>() {
            @Override
            public void onResponse(Call<PlanDetailsResponse> call, retrofit2.Response<PlanDetailsResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbarDialog.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        planDetailsData = response.body().getData();
                        bindData(planDetailsData,membershipPlayRecyclerView);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlanDetailsResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbarDialog.setVisibility(View.GONE);
            }
        });
    }

    private void bindData(List<PlanDetailsData> planDetailsData, RecyclerView membershipPlayRecyclerView) {
        PlanDetailsAdapter adapter = new PlanDetailsAdapter(planDetailsData,getApplicationContext());
        membershipPlayRecyclerView.setHasFixedSize(true);
        membershipPlayRecyclerView.setLayoutManager(new GridLayoutManager(ServiceAcitvity.this, 1));
        membershipPlayRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void isMemberShipPlan(String userId) {
        mainProgressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<CheckMemberShipPlan> call = serviceInterface.idMemberShipPlan(map);
        call.enqueue(new Callback<CheckMemberShipPlan>() {
            @Override
            public void onResponse(Call<CheckMemberShipPlan> call, retrofit2.Response<CheckMemberShipPlan> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                      membership_status = response.body().getStatus().toString();
                    if (membership_status.equals("1")) {
                        if (memberShipLayout.getVisibility() == View.VISIBLE) {
                            memberShipLayout.setVisibility(View.GONE);
                        }
                    } else {
                        memberShipLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckMemberShipPlan> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getServiceChildCategory(String serviceSubCategoryID) {
        if (Helper.INSTANCE.isNetworkAvailable(ServiceAcitvity.this)){
            serviceChildCategoryList.clear();
            HashMap<String, String> map = new HashMap<>();
            map.put("sub_category_id", serviceSubCategoryID);
            mainProgressbar.setVisibility(View.VISIBLE);
            ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
            Call<ServiceChildCategoryResponse> call = serviceInterface.getChildCategory(map);
            call.enqueue(new Callback<ServiceChildCategoryResponse>() {
                @Override
                public void onResponse(Call<ServiceChildCategoryResponse> call, retrofit2.Response<ServiceChildCategoryResponse> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus().toString();
                        if (status.equals("1")) {
                            if (service_childCategoryRecyclerView.getVisibility() == View.GONE) {
                                service_childCategoryRecyclerView.setVisibility(View.VISIBLE);
                            }

                            mainProgressbar.setVisibility(View.GONE);
                            serviceChildCategoryList = response.body().getData();
                            for (int i = 0; i<serviceChildCategoryList.size(); i++)
                            {
                                minvaluelist.add(Integer.parseInt(serviceChildCategoryList.get(i).getFees()));
                            }

                            getChildCategoryListData(serviceChildCategoryList);
                         } else {
                            service_childCategoryRecyclerView.setVisibility(View.GONE);
                            mainProgressbar.setVisibility(View.GONE);
                        }
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(ServiceAcitvity.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServiceChildCategoryResponse> call, Throwable t) {
                    Log.d("ff", t.toString());
                    mainProgressbar.setVisibility(View.GONE);
                }
            });

        } else {
            Helper.INSTANCE.Error(ServiceAcitvity.this, getString(R.string.NOCONN));
        }
    }

    private void getChildCategoryListData(List<ServicesChildCategoryData> serviceChildCategoryList) {
        int minfeesvalue=Collections.min(minvaluelist);
        ServiceChildCategoryAdapter adapter = new ServiceChildCategoryAdapter(serviceChildCategoryList, ServiceAcitvity.this, this,multiCoupon,discount,discountType,minfeesvalue,this);
        service_childCategoryRecyclerView.setHasFixedSize(true);
        service_childCategoryRecyclerView.setLayoutManager(new GridLayoutManager(ServiceAcitvity.this, 1));
        service_childCategoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void serviceItemClick(String itemCharg, String totaldiscount) {

        int total_amount = Integer.parseInt(itemCharg) + Integer.parseInt(totaldiscount);
        txt_itemTotal.setText("\u20B9" + " " + total_amount);

        if(totaldiscount.equalsIgnoreCase("0") || totaldiscount.equalsIgnoreCase("")){
            txt_discount.setText("\u20B9" + " -" + totaldiscount);
            discountsection.setVisibility(View.GONE);
            multidisView.setVisibility(View.GONE);
        } else {
            discountsection.setVisibility(View.VISIBLE);
            multidisView.setVisibility(View.VISIBLE);
            txt_discount.setText("\u20B9" + " -" + totaldiscount);
        }

        if(membership_status.equalsIgnoreCase("1") || membership_status.equalsIgnoreCase("2") && itemCharg.equalsIgnoreCase("0") || itemCharg.equalsIgnoreCase("")){
            membership_discount_layout.setVisibility(View.VISIBLE);
        } else {
            membership_discount_layout.setVisibility(View.GONE);
        }


        txt_payTotalMoney.setText("\u20B9" + " " + itemCharg);

        if (itemCharg.equalsIgnoreCase("") || itemCharg.equalsIgnoreCase("0")) {

        } else {
            if (membership_status.equalsIgnoreCase("1")) {
                YourPreference yourPrefrence = YourPreference.getInstance(ServiceAcitvity.this);
                String dis_amt = yourPrefrence.getData("Discount_Amount");
                membership_discount_layout.setVisibility(View.VISIBLE);
                membershipView.setVisibility(View.VISIBLE);
                membershipDiscountView.setVisibility(View.VISIBLE);

                if (yourPrefrence.getData("Discount_Type").equalsIgnoreCase("flat")) {
                     txt_membership_discount.setText("\u20B9" + " " + dis_amt);
                     if(Integer.parseInt(itemCharg) > Integer.parseInt(dis_amt)) {
                         int disvalue = Integer.parseInt(itemCharg) - Integer.parseInt(dis_amt);
                         txt_payTotalMoney.setText("\u20B9" + " " + disvalue+"");
                     }

//                    int disvalue = Integer.parseInt(itemCharg) - Integer.parseInt(dis_amt);
//                    txt_payTotalMoney.setText("\u20B9" + " " + disvalue+"");

                } else {
                    int disvalue = Integer.parseInt(itemCharg)*Integer.parseInt(dis_amt)/100;
                    txt_membership_discount.setText("\u20B9" + " " + disvalue+"");
                    if(Integer.parseInt(itemCharg) > disvalue){
                        int finalAmount = Integer.parseInt(itemCharg) - disvalue;
                        txt_payTotalMoney.setText("\u20B9" + " " + finalAmount+"");
                    }

//                    int finalAmount = Integer.parseInt(itemCharg) - disvalue;
//                    txt_payTotalMoney.setText("\u20B9" + " " + finalAmount+"");

                }
            }
            else if (membership_status.equalsIgnoreCase("2")) {
                YourPreference yourPrefrence = YourPreference.getInstance(ServiceAcitvity.this);
                String dis_amt = yourPrefrence.getData("Discount_Amount");
                String membership_charge_current = yourPrefrence.getData("membership_charge_current");
                membership_discount_layout.setVisibility(View.VISIBLE);
                membershipView.setVisibility(View.VISIBLE);
                membershipDiscountView.setVisibility(View.VISIBLE);
                txt_membership_charge.setText("\u20B9" +" "+ membership_charge_current);

                if (yourPrefrence.getData("Discount_Type").equalsIgnoreCase("flat"))
                {
                    txt_membership_discount.setText("\u20B9" + " " + dis_amt);
                    int disvalue = Integer.parseInt(itemCharg) - Integer.parseInt(dis_amt)+Integer.parseInt(membership_charge_current);
                    txt_payTotalMoney.setText("\u20B9" + " " + disvalue+"");

                } else {
                    int disvalue = Integer.parseInt(itemCharg)*Integer.parseInt(dis_amt)/100;
                    txt_membership_discount.setText("\u20B9" + " " + disvalue+"");
                    int finalAmount = Integer.parseInt(itemCharg) - disvalue+Integer.parseInt(membership_charge_current);
                    txt_payTotalMoney.setText("\u20B9" + " " + finalAmount+"");

                }
            }
        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(ServiceAcitvity.this, DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    @Override
    public void onBackPressed() {
        databaseHandler.cartInterface().deleteall();
        super.onBackPressed();
    }

    public  void gettimeslot(String currentdatecroped, String libdate) {
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


//                spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                spinnerDialogFrom.setCancellable(true); // for cancellable
//                spinnerDialogFrom.setShowKeyboard(false); // for open keyboard by default
//                spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                    @Override
//                    public void onClick(String item, int position) {
//                        txt_timeSlot.setText(item);
//                    }
//                });



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

                   // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 8) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("10:00 AM - 12:00 PM");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                   // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 10) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");


                   // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 12) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                   // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 15) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                  //  openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else {
                    time.clear();
                    time.add("Slot not available, Please choose another date!");
                    txt_timeSlot.setText("Slot not available, Please choose another date!");

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                 //   openTimeDialog(time);


                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public  void gettimeslott(String currentdatecroped, String libdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate = dateFormat.parse(currentdatecroped);
            convertedDate2 = dateFormat.parse(libdate);

            if (convertedDate2.before(convertedDate)) {
                time.clear();
                time.add("Please Select Time Slot");
                time.add("8:00 AM - 10:00 AM");
                time.add("10:00 AM - 12:00 PM");
                time.add("12:00 PM - 3:00 PM");
                time.add("3:00 PM - 6:00 PM");
                time.add("6:00 PM - 9:00 PM");

                txt_timeSlot.setText("Please Select Time Slot");


//                spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                spinnerDialogFrom.setCancellable(true); // for cancellable
//                spinnerDialogFrom.setShowKeyboard(false); // for open keyboard by default
//                spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                    @Override
//                    public void onClick(String item, int position) {
//                        txt_timeSlot.setText(item);
//                    }
//                });



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

                    // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 8) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("10:00 AM - 12:00 PM");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 10) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("12:00 PM - 3:00 PM");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");


                    // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 12) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("3:00 PM - 6:00 PM");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    // openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else if (Integer.parseInt(timecrop) < 15) {
                    time.clear();
                    time.add("Please Select Time Slot");
                    time.add("6:00 PM - 9:00 PM");
                    txt_timeSlot.setText("Please Select Time Slot");

                    //  openTimeDialog(time);

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                } else {
                    time.clear();
                    time.add("Slot not available, Please choose another date!");
                    txt_timeSlot.setText("Slot not available, Please choose another date!");

//                    spinnerDialogFrom = new SpinnerDialog(ServiceAcitvity.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
//                    spinnerDialogFrom.setCancellable(true); // for cancellable
//                    spinnerDialogFrom.setShowKeyboard(false);// for open keyboard by default
//                    spinnerDialogFrom.bindOnSpinerListener(new OnSpinerItemClick() {
//                        @Override
//                        public void onClick(String item, int position) {
//                            txt_timeSlot.setText(item);
//                        }
//                    });

                    //   openTimeDialog(time);


                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void getCategory(String id, String childname) {
        childCategory = childname;
        logAdClickEvent(category, serviceName, childname);

    }

    private void openTimeDialog(List<String> stateList) {
        final Dialog dialog = new Dialog(ServiceAcitvity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_time_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        rv_simple_list = dialog.findViewById(R.id.rv_simple_list);
        txt_close = dialog.findViewById(R.id.txt_close);

        TimeAdapter adapter = new TimeAdapter(stateList, ServiceAcitvity.this,this,dialog);
        rv_simple_list.setHasFixedSize(true);
        rv_simple_list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_simple_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void logAdClickEvent(String category, String serviceName, String childCategory) {
        Bundle params = new Bundle();
        params.putString("Category Name ", category);
        params.putString("Sub Category Name ", serviceName);
        params.putString("Child Category Name ", childCategory);
        logger.logEvent("Child Category Name Click", params);

    }

    public void logAdClickEvent2() {
        Bundle params = new Bundle();
        params.putString("Confirm Button", "Clicked");
        logger.logEvent("Confirm Click", params);

    }

    @Override
    public void timeClickListner(String name) {
        txt_timeSlot.setText(name);
    }

    @Override
    public void planListner(String id, String validity, String money) {
        Toast.makeText(this, "Membership Added", Toast.LENGTH_SHORT).show();
        memberShipdialog.dismiss();
        memberShipId = id;
        membership_status="2";

        img_forword.setVisibility(View.GONE);
        img_clear.setVisibility(View.VISIBLE);
        apply_MemberShip_Layout.setVisibility(View.GONE);
        applyed_MemberShipLayout.setVisibility(View.VISIBLE);
        memberShipLayout.setBackground(ServiceAcitvity.this.getResources().getDrawable(R.color.orenge));
        membership_charge_layout.setVisibility(View.VISIBLE);
        txt_membership_charge.setText("\u20B9" + " " + money);
        YourPreference yourPrefrence = YourPreference.getInstance(ServiceAcitvity.this);
        String dis_amt=yourPrefrence.getData("Discount_Amount");
        yourPrefrence.saveData("membership_charge_current",money);
        membership_discount_layout.setVisibility(View.VISIBLE);
        membershipView.setVisibility(View.VISIBLE);
        membershipDiscountView.setVisibility(View.VISIBLE);

        if (yourPrefrence.getData("Discount_Type").equalsIgnoreCase("flat"))
        {
            txt_membership_discount.setText("\u20B9" + " " + dis_amt);
            String total_amt = txt_payTotalMoney.getText().toString();
            String split[] = total_amt.split(" ");
            int total_amtt = Integer.parseInt(split[1]);
            int disvalue = total_amtt - Integer.parseInt(dis_amt);
            int nestAmount  = disvalue + Integer.parseInt(money);
            txt_payTotalMoney.setText("\u20B9" + " " + nestAmount+"");

        } else {
            String total_amt = txt_payTotalMoney.getText().toString();
            String split[] = total_amt.split(" ");
            int total_amtt = Integer.parseInt(split[1]);
            int disvalue = total_amtt*Integer.parseInt(dis_amt)/100;
            txt_membership_discount.setText("\u20B9" + " " + disvalue+"");
            int disvaluee = total_amtt - disvalue;
            int nestAmount  = disvaluee + Integer.parseInt(money);
            txt_payTotalMoney.setText("\u20B9" + " " + nestAmount+"");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        YourPreference  yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        isMemberShipPlan(userId);
    }


}