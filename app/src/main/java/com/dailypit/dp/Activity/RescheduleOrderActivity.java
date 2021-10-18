package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dailypit.dp.MainActivity;
import com.dailypit.dp.R;

public class RescheduleOrderActivity extends AppCompatActivity {

    ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_eschedule_order);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.MainObject.setFromActivity(false);
                Intent intent = new Intent(RescheduleOrderActivity.this, MainActivity.class);
                intent.putExtra("page", "1");
                startActivity(intent);
                finish();

            }
        });



    }


    @Override
    public void onBackPressed() {

        MainActivity.MainObject.setFromActivity(false);
        Intent intent = new Intent(RescheduleOrderActivity.this, MainActivity.class);
        intent.putExtra("page", "1");
        startActivity(intent);
        finish();

    }
}