package com.dailypit.dp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dailypit.dp.Adapter.WalletAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Wallet.WalletTransactionData;
import com.dailypit.dp.Model.Wallet.WalletTransactionResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.YourPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AllTransaction extends AppCompatActivity {
    RecyclerView wallet_recyclerView;
    String user_id;
    ProgressBar mainProgressbar;
    ImageView back_img;
    List<WalletTransactionData> walletTransactionData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transaction);

        wallet_recyclerView = findViewById(R.id.wallet_recyclerView);
        mainProgressbar = findViewById(R.id.mainProgressbar);
        back_img = findViewById(R.id.back_img);

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        user_id = yourPrefrence.getData("USERID");

        getTrasactionList(user_id);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private  void getTrasactionList(String user_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<WalletTransactionResponse> call = serviceInterface.getWalletTransactionList(map);
        call.enqueue(new Callback<WalletTransactionResponse>() {
            @Override
            public void onResponse(Call<WalletTransactionResponse> call, retrofit2.Response<WalletTransactionResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        walletTransactionData = response.body().getData();
                        Collections.reverse(walletTransactionData);
                        WalletAdapter adapter = new WalletAdapter(walletTransactionData);
                        wallet_recyclerView.setHasFixedSize(true);
                        wallet_recyclerView.setLayoutManager(new LinearLayoutManager(AllTransaction.this));
                        wallet_recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletTransactionResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);
            }
        });
    }
}