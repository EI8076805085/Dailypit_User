package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Adapter.PackageServiceChildAdapter;
import com.dailypit.dp.Adapter.ServiceChildCategoryAdapter;
import com.dailypit.dp.Adapter.TimeAdapter;
import com.dailypit.dp.Interface.PackageCategory;
import com.dailypit.dp.Interface.PackageQuantity;
import com.dailypit.dp.Interface.SelectTimeListner;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.Model.Address.UserAddressListResponse;
import com.dailypit.dp.Model.Address.UserAddressListResponseData;
import com.dailypit.dp.Model.CartResponse;
import com.dailypit.dp.Model.Packageg.PackageServiceChildCategoryResponse;
import com.dailypit.dp.Model.Packageg.YourPackageResponse;
import com.dailypit.dp.Model.Payment.PackageCartResponse;
import com.dailypit.dp.Model.Payment.PaymentResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.Model.Service.ServiceChildCategoryResponse;
import com.dailypit.dp.Model.Service.ServicesChildCategoryData;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.DatabaseHandler;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.PackageDB;
import com.dailypit.dp.Utils.YourPreference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;

public class BookPackageServiceActivity extends AppCompatActivity implements SelectTimeListner, PackageCategory, PackageQuantity {

    private int month = 0;
    private int year  = 0;
    private int day = 0 ;
    int price_rs;
    ProgressBar mainProgressbar;
    List<UserAddressListResponseData> userAddressListResponseData = new ArrayList<>();
    List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();
    ArrayList<String> childCategoryId = new ArrayList<>();
    ArrayList<String> subCategoryId = new ArrayList<>();
    ArrayList<String> itemQuantity = new ArrayList<>();
    TextView txt_defaultAddress,txt_addressType,txt_ChangeAddresss,txt_timeSlot,txt_close,txt_payTotalMoney,txt_amount,btn_ProceedToPay,txt_checkOrder;
    String address_id = "",DOB,totalAmount,address_latitude="",address_longitude="",timeslot,packagesubCategory="",unique_id= "",package_id="";
    ImageView address_img,img_clock,back_img;
    String currentDateTimeString,libdate,selectedDate = "",
            childCategory,subCategory,totalitemQuantity,userName, userEmail, userMobile,subcategory_id = "",CategoryID ="";
    ArrayList<String> time = new ArrayList<>();
    RecyclerView rv_simple_list,service_childCategoryRecyclerView;
    PackageDB packageDB;
    android.location.Address location;
    List<PackageCartResponse> packageCartResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_package_service);

        mainProgressbar = findViewById(R.id.mainProgressbar);
        txt_defaultAddress = findViewById(R.id.txt_defaultAddress);
        txt_addressType = findViewById(R.id.txt_addressType);
        address_img = findViewById(R.id.address_img);
        txt_ChangeAddresss = findViewById(R.id.txt_ChangeAddresss);
        txt_timeSlot = findViewById(R.id.txt_timeSlot);
        img_clock = findViewById(R.id.img_clock);
        service_childCategoryRecyclerView = findViewById(R.id.service_childCategoryRecyclerView);
        txt_payTotalMoney = findViewById(R.id.txt_payTotalMoney);
        txt_amount = findViewById(R.id.txt_amount);
        btn_ProceedToPay = findViewById(R.id.btn_ProceedToPay);
        back_img = findViewById(R.id.back_img);


        setUppackageDB();

     //   getServiceChildCategory("serviceSubCategoryID");

        Intent intent = getIntent();
        totalAmount = intent.getStringExtra("totalAmount");
        subcategory_id = intent.getStringExtra("category_id");
        unique_id = intent.getStringExtra("unique_id");
        package_id = intent.getStringExtra("package_id");

       // txt_payTotalMoney.setText(totalAmount);
        txt_amount.setText(totalAmount);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        SimpleDateFormat dateee = new SimpleDateFormat("dd-MM-yyyy");
        currentDateTimeString = sdf.format(d);
        String datefomated = dateee.format(d);
        //currentdatecroped = datefomated.substring(0,2);
        libdate = datefomated;
        selectedDate = datefomated;
        gettimeslot(datefomated,libdate);

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);

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
                //libdate = DOB.substring(0,2);
                gettimeslot(libdate,DOB);

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_ChangeAddresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookPackageServiceActivity.this, Address.class);
                startActivity(intent);
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


        packageCartResponses = packageDB.packageInterface().getallpackagecartdata();
        PackageServiceChildAdapter adapter = new PackageServiceChildAdapter(packageCartResponses,getApplicationContext(),this,this);
        service_childCategoryRecyclerView.setHasFixedSize(true);
        service_childCategoryRecyclerView.setLayoutManager(new GridLayoutManager(BookPackageServiceActivity.this, 1));
        service_childCategoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        btn_ProceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
                String childCategory = yourPrefrence.getData("packageChildCategory");
                String totalitemQuantity = yourPrefrence.getData("packageTotalitemQuantity");
                packagesubCategory = yourPrefrence.getData("packagesubCategory");

                if (packagesubCategory.equalsIgnoreCase("") || packagesubCategory.equalsIgnoreCase("0")) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Service", Toast.LENGTH_SHORT).show();
                } else if(selectedDate.isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("")){
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("Please Select Time Slot")) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if(txt_timeSlot.getText().toString().equals("Slot not available, Please choose another date!")){
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if (childCategory.isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please Select Type", Toast.LENGTH_SHORT).show();
                }  else if (totalAmount.isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Total Amount is required", Toast.LENGTH_SHORT).show();
                } else if (totalitemQuantity.isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Item is required", Toast.LENGTH_SHORT).show();
                } else if (address_id.isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please add order delivery address", Toast.LENGTH_SHORT).show();
                } else if (userName == null || userName.trim().isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please add name in your profile", Toast.LENGTH_SHORT).show();
                } else if (userMobile == null || userMobile.trim().isEmpty()) {
                    Toast.makeText(BookPackageServiceActivity.this, "Please add mobile number in your profile", Toast.LENGTH_SHORT).show();
                } else {
                    getLocationFromAddress(getApplicationContext(),txt_defaultAddress.getText().toString());
                    paymentsuccess("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddressList();
        getUserProfile();
    }

    private void getUserProfile() {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        YourPreference yourPrefrence = YourPreference.getInstance(BookPackageServiceActivity.this);
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
                    Toast.makeText(BookPackageServiceActivity.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
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

    private void openTimeDialog(List<String> stateList) {
        final Dialog dialog = new Dialog(BookPackageServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_time_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        rv_simple_list = dialog.findViewById(R.id.rv_simple_list);
        txt_close = dialog.findViewById(R.id.txt_close);

        TimeAdapter adapter = new TimeAdapter(stateList, BookPackageServiceActivity.this,this,dialog);
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

    @Override
    public void timeClickListner(String name) {
        txt_timeSlot.setText(name);
    }

    private void setUppackageDB() {
        packageDB = Room.databaseBuilder(BookPackageServiceActivity.this, PackageDB.class, "package").allowMainThreadQueries().build();
    }


    @Override
    public void getpackageCategory(String child_CatID, String subcategory) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());

        subCategoryId.add(subcategory);
        String sub_Cat = subCategoryId.toString();
        subCategory = sub_Cat.replaceAll("[\\[\\]\\(\\)]", "");
        Log.d("stan", "" + subCategory);
        yourPrefrence.saveData("packagesubCategory", subCategory);


        childCategoryId.add(child_CatID);
        String vtype = childCategoryId.toString();
        childCategory = vtype.replaceAll("[\\[\\]\\(\\) ]", "");
        Log.d("stan", "" + childCategory);
        yourPrefrence.saveData("packageChildCategory", childCategory);

    }

    @Override
    public void getpackageQuentity(String quentity) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        itemQuantity.add(quentity);
        String quantity = itemQuantity.toString();
        totalitemQuantity = quantity.replaceAll("[\\[\\]\\(\\) ]", "");
        Log.d("totalitemQuantity", "" + totalitemQuantity);
        yourPrefrence.saveData("packageTotalitemQuantity", totalitemQuantity);
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

    private void paymentsuccess(String razorpayPaymentID) {
        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        String userId = yourPrefrence.getData("USERID");
        CategoryID = yourPrefrence.getData("CategoryID");
        String walletDiductAmount = "0";
        String memberShipId = "0";
        String coupon_id = "0";
        String discount = "0";

        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", userId);
        map.put("category_id", CategoryID);
        map.put("sub_category_id", subCategory);
        map.put("child_category_id", childCategory);
        map.put("coupon_id", coupon_id);
        map.put("transaction_id", razorpayPaymentID);
        map.put("discount_amount", discount);
        map.put("total_amount", txt_payTotalMoney.getText().toString());
        map.put("net_amount", txt_payTotalMoney.getText().toString());
        map.put("quantity", totalitemQuantity);
        map.put("address_id", address_id);
        map.put("order_date",selectedDate);
        map.put("time_slot", txt_timeSlot.getText().toString());
        map.put("payment_mode", "Pay Online");
        map.put("payment_status", "Paid");
        map.put("wallet_amount", walletDiductAmount);
        map.put("latitude", address_latitude);
        map.put("longitude", address_longitude);
        map.put("payment_for", "order");
        map.put("membership_id", memberShipId);
        map.put("is_package", "1");
        map.put("package_id", package_id);
        map.put("unique_id", unique_id);

        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<PaymentResponse> call = serviceInterface.placeOrder(map);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                         final Dialog dialog = new Dialog(BookPackageServiceActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.cnf_bottom_sheet_layout);
                        dialog.setCancelable(false);
                        txt_checkOrder = dialog.findViewById(R.id.txt_checkOrder);
                        txt_checkOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                YourPreference yourPrefrence = YourPreference.getInstance(BookPackageServiceActivity.this);
                                com.dailypit.dp.MainActivity.MainObject.setFromActivity(false);
                                Intent intent = new Intent(BookPackageServiceActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    } else {
                        mainProgressbar.setVisibility(View.GONE);
                        Toast.makeText(BookPackageServiceActivity.this, "Something is wrong try again later", Toast.LENGTH_SHORT).show();
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
}