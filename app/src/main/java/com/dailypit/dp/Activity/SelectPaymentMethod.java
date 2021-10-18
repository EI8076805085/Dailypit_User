package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dailypit.dp.R;

public class SelectPaymentMethod extends AppCompatActivity {

    LinearLayout other_linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);

        other_linearLayout = findViewById(R.id.other_linearLayout);
        other_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }
}