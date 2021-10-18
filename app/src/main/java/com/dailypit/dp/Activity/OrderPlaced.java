package com.dailypit.dp.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dailypit.dp.R;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class OrderPlaced extends AppCompatActivity {

    ImageView back_img, help_img, addressTypeImage;
    String addressType, address, ordertime, date, finalAmount,category, orderId, couponName, service_cate_name,
            ChildCatName, Quentity,payment_mode,payment_Status,DOB = "",currentDateTimeString,datefomated;

    TextView txt_date, txt_time, txt_addressType, txt_address, txt_orderId, txt_serviceChildCategory,
            txt_serviceCAtegory, txt_FinalAmount, txt_orderID, txt_paymentMode,txt_cancleOrder,txt_rescheduleOrder,
            txt_timeSlot,txt_Submit,txt_dateReschedule;

    LinearLayout txt_calendraLayout,txt_timeLayout;
    private Integer month = 0;
    private Integer year = 0;
    private Integer day = 0;
    ArrayList<String> time = new ArrayList<>();
    SpinnerDialog spinnerDialogFrom;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        back_img = findViewById(R.id.back_img);
        addressTypeImage = findViewById(R.id.addressTypeImage);
        help_img = findViewById(R.id.help_img);
        txt_addressType = findViewById(R.id.txt_addressType);
        txt_address = findViewById(R.id.txt_address);
        txt_orderId = findViewById(R.id.txt_orderId);
        txt_date = findViewById(R.id.txt_date);
        txt_orderID = findViewById(R.id.txt_orderID);
        txt_FinalAmount = findViewById(R.id.txt_FinalAmount);
        txt_time = findViewById(R.id.txt_time);
        txt_serviceChildCategory = findViewById(R.id.txt_serviceChildCategory);
        txt_serviceCAtegory = findViewById(R.id.txt_serviceCAtegory);
        txt_paymentMode = findViewById(R.id.txt_paymentMode);
        txt_cancleOrder = findViewById(R.id.txt_cancleOrder);
        txt_rescheduleOrder = findViewById(R.id.txt_rescheduleOrder);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        help_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPlaced.this, Help.class);
                intent.putExtra("Order_id", orderId);
                startActivity(intent);
            }
        });

        addressType = getIntent().getStringExtra("address_type");
        address = getIntent().getStringExtra("address");
        orderId = getIntent().getStringExtra("order_id");
        ordertime = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        finalAmount = getIntent().getStringExtra("net_amount");
        couponName = getIntent().getStringExtra("coupon_name");
        ChildCatName = getIntent().getStringExtra("child_cate_name");
        service_cate_name = getIntent().getStringExtra("service_cate_name");
        Quentity = getIntent().getStringExtra("quentity");
        payment_mode = getIntent().getStringExtra("payment_mode");
        category = getIntent().getStringExtra("category");
        payment_Status = getIntent().getStringExtra("payment_Status");

        if (addressType.equals("Work")) {
            addressTypeImage.setImageResource(R.drawable.address_work);
        }
        else if (addressType.equals("Other")) {
            addressTypeImage.setImageResource(R.drawable.edit_location);
        } else {
            addressTypeImage.setImageResource(R.drawable.ic_address_home);
        }

        List<String> itemNumberParentList = Arrays.asList(Quentity);

        List<String> itemNameParentList = Arrays.asList(ChildCatName);

        String totalValue = "";
        for (int i = 0; i < itemNameParentList.size(); i++) {
            List<String> itemNumberList = Arrays.asList(itemNumberParentList.get(i).split(","));
            List<String> itemNameList = Arrays.asList(itemNameParentList.get(i).split(","));
            for (int j = 0; j < itemNumberList.size(); j++) {
                if (j==itemNumberList.size()-1) {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n";
                } else {
                    totalValue =  totalValue + itemNameList.get(j) + " x " + itemNumberList.get(j) + "\n\n⦿";
                }
            }
        }

        txt_paymentMode.setText(payment_mode);
        txt_serviceChildCategory.setText("⦿ " +totalValue);
        txt_addressType.setText(addressType);
        txt_address.setText(address);
        txt_orderId.setText(orderId);
        txt_orderID.setText("Order Id "+orderId);
        txt_time.setText(ordertime);
        txt_serviceCAtegory.setText(category);
        txt_date.setText(date);
        txt_FinalAmount.setText("\u20B9"+" "+finalAmount);

        txt_cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPlaced.this, CancleOrderActivity.class);
                startActivity(intent);
            }
        });

        txt_rescheduleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(OrderPlaced.this);
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

                gettimeslot(datefomated,datefomated);

                txt_timeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinnerDialogFrom.showSpinerDialog();
                    }
                });

                txt_calendraLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCelender(OrderPlaced.this, year, month, day, txt_dateReschedule);
                    }
                });

                txt_Submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderPlaced.this, RescheduleOrderActivity.class);
                        startActivity(intent);
                    }
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });

    }

    private void openCelender(OrderPlaced orderPlaced, Integer year, Integer month, Integer day, TextView txt_dateReschedule) {
        final Calendar c;
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog mDatePicker = new DatePickerDialog(orderPlaced, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                if (selectedmonth < 9) {
                    DOB =  selectedday + "-" + "0" + (selectedmonth + 1) + "-" + selectedyear;
                } else {
                    DOB = selectedday + "-" + (selectedmonth + 1) + "-" + selectedyear;
                }

                txt_dateReschedule.setText(DOB);
                gettimeslot(datefomated,DOB);

                //  datetime.setText(new StringBuilder().append(selectedday).append("-").append(selectedmonth + 1).append("-").append(selectedyear));
            }
        }, year, month, day);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.setTitle("Please Select Date");
        mDatePicker.show();
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

                spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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


                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

                    spinnerDialogFrom = new SpinnerDialog(OrderPlaced.this, time, "Choose " , R.style.DialogAnimations_SmileWindow, "Close  ");// With 	Animation
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

}