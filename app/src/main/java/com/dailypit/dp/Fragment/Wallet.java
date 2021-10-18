package com.dailypit.dp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dailypit.dp.Activity.AddNewAddress;
import com.dailypit.dp.Activity.AllTransaction;
import com.dailypit.dp.Activity.SignUp;
import com.dailypit.dp.Adapter.WalletAdapter;
import com.dailypit.dp.Interface.ServiceInterface;
import com.dailypit.dp.Model.Profile.UserProfileResponse;
import com.dailypit.dp.Model.Profile.UserProfileResponseData;
import com.dailypit.dp.Model.Wallet.AddAmountResponse;
import com.dailypit.dp.Model.Wallet.WalletMoneyResponse;
import com.dailypit.dp.Model.Wallet.WalletTransactionData;
import com.dailypit.dp.Model.Wallet.WalletTransactionResponse;
import com.dailypit.dp.R;
import com.dailypit.dp.Utils.ApiClient;
import com.dailypit.dp.Utils.Helper;
import com.dailypit.dp.Utils.YourPreference;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Wallet extends Fragment  {

    public static RecyclerView wallet_recyclerView;
    public static String user_id,userName, userEmail,userMobile;
    public static ProgressBar mainProgressbar;
    public static TextView txt_walletAmount,txt_addPoint;
    public static EditText ed_amount;
    public static ImageView img_transaction,img_dataNotFound;
    LinearLayout txt_50RupeesLayout,txt_100RupeesLayout,txt_500RupeesLayout;
    public static List<WalletTransactionData> walletTransactionData = new ArrayList<>();
    public static List<UserProfileResponseData> userProfileResponseData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        wallet_recyclerView = view.findViewById(R.id.wallet_recyclerView);
        mainProgressbar = view.findViewById(R.id.mainProgressbar);
        txt_walletAmount = view.findViewById(R.id.txt_walletAmount);
        txt_addPoint = view.findViewById(R.id.txt_addPoint);
        ed_amount = view.findViewById(R.id.ed_amount);
        img_transaction = view.findViewById(R.id.img_transaction);
        txt_50RupeesLayout = view.findViewById(R.id.txt_50RupeesLayout);
        txt_100RupeesLayout = view.findViewById(R.id.txt_100RupeesLayout);
        txt_500RupeesLayout = view.findViewById(R.id.txt_500RupeesLayout);
        img_dataNotFound = view.findViewById(R.id.img_dataNotFound);

        txt_50RupeesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_amount.setText("50");
            }
        });

        txt_100RupeesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_amount.setText("100");
            }
        });

        txt_500RupeesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_amount.setText("500");
            }
        });

        img_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllTransaction.class);
                startActivity(intent);
            }
        });

        txt_addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), " Invalid User ", Toast.LENGTH_SHORT).show();
                } else if (ed_amount.getText().toString().equalsIgnoreCase("")) {
                      ed_amount.setError("*Required");
                } else if(ed_amount.getText().toString().length() == 1) {
                      ed_amount.setError("* Please add minimum 10 points");
                }  else {
                    if (Helper.INSTANCE.isNetworkAvailable(getActivity())){
                        addAmount(ed_amount.getText().toString());
                    } else {
                        Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
                    }
                }
            }
        });

        YourPreference yourPrefrence = YourPreference.getInstance(getApplicationContext());
        user_id = yourPrefrence.getData("USERID");
        getWalletAmount(user_id);
        getTrasactionList(user_id);
        getUserProfile(user_id);

        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                txt_addPoint.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_background));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        return view;
        
    }

    private void addAmount(String amount) {
        Checkout checkout = new Checkout();
        final Activity activity = getActivity();
        int totalPay = Integer.parseInt(amount);
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

    private void getUserProfile(String user_id) {
        HashMap<String, String> map = new HashMap<>();
        mainProgressbar.setVisibility(View.VISIBLE);
        map.put("user_id", user_id);
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
                        for(int i =0 ; i< userProfileResponseData.size(); i++){
                            userName = userProfileResponseData.get(i).getName();
                            userMobile = userProfileResponseData.get(i).getMobile();
                            userEmail = userProfileResponseData.get(i).getEmail();
                        }
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainProgressbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something is wrong try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("ff", t.toString());
                mainProgressbar.setVisibility(View.GONE);

            }
        });
    }

    public static void addPoints(String user_id, String transaction_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("transaction_id", transaction_id);
        map.put("amount", ed_amount.getText().toString());
        mainProgressbar.setVisibility(View.VISIBLE);
        ServiceInterface serviceInterface = ApiClient.getClient().create(ServiceInterface.class);
        Call<AddAmountResponse> call = serviceInterface.getAmountinWallet(map);
        call.enqueue(new Callback<AddAmountResponse>() {
            @Override
            public void onResponse(Call<AddAmountResponse> call, retrofit2.Response<AddAmountResponse> response) {
                if (response.isSuccessful()) {
                    mainProgressbar.setVisibility(View.GONE);
                    String status = response.body().getStatus().toString();
                    if (status.equals("1")) {
                        getWalletAmount(user_id);
                        getTrasactionList(user_id);
                        setEditTest();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddAmountResponse> call, Throwable t) {
                mainProgressbar.setVisibility(View.GONE);
                Log.d("ff", t.toString());
            }
        });
    }

    private static void setEditTest() {
        ed_amount.setText("");
    }

    private static void getTrasactionList(String user_id) {
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

                        if (wallet_recyclerView.getVisibility() == View.GONE) {
                            wallet_recyclerView.setVisibility(View.VISIBLE);
                        }

                        if (img_dataNotFound.getVisibility() == View.VISIBLE) {
                            img_dataNotFound.setVisibility(View.GONE);
                        }

                        walletTransactionData = response.body().getData();
                        Collections.reverse(walletTransactionData);
                        WalletAdapter adapter = new WalletAdapter(walletTransactionData);
                        wallet_recyclerView.setHasFixedSize(true);
                        wallet_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        wallet_recyclerView.setAdapter(adapter);
                    }
                    else {
                        wallet_recyclerView.setVisibility(View.GONE);
                        img_dataNotFound.setVisibility(View.VISIBLE);
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

    private static void getWalletAmount(String user_id) {
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
                        txt_walletAmount.setText(response.body().getBalance());
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

}