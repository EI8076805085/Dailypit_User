package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dailypit.dp.MainActivity;
import com.dailypit.dp.R;

public class CancleOrderActivity extends AppCompatActivity {

    TextView txt_cancleOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_order);
        txt_cancleOrder = findViewById(R.id.txt_cancleOrder);

        txt_cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.MainObject.setFromActivity(false);
                Intent intent = new Intent(CancleOrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}