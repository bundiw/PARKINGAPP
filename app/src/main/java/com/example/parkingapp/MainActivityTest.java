package com.example.parkingapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.parkingapp.databinding.ActivityMainTestBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityTest extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_activity_test);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here
                    String jsonString = "{\"BusinessShortCode" +
                            "\":174379,\"Password\":\"MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjIxMDAxMTk0NTE4\",\"Timestamp\":\"20221001194518\",\"TransactionType\":\"CustomerPayBillOnline\",\"Amount\":1,\"PartyA\":254723629277,\"PartyB\":174379,\"PhoneNumber\":254723629277,\"CallBackURL\":\"https://ce97-2c0f-fe38-240a-f72-69d0-cc7e-d288-5e3e.eu.ngrok.io/api/payments/mpesa\",\"AccountReference\":\"CompanyXLTD\",\"TransactionDesc\":\"PaymentofX\"}";


                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body =
                            RequestBody.create(mediaType, jsonString);
                    Request request = new Request.Builder()
                            .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer sN3TRzpTmX4wDkcDoS3JIPBGjKm4")
                            .build();
                    try {
                        Response response =
                                client.newCall(request).execute();
                        Log.i("BST",response.toString()+
                                " Wonder");
                        System.out.println(response.toString()+" Wonder");
                    } catch (IOException e) {
                        System.out.println("STRT");
                        e.printStackTrace();
                        System.out.println("END");
                    }

                }



            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_activity_test);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}