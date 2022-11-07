package com.example.parkingapp;

import static com.example.parkingapp.CONSTANTS.RESERVATIONFEES;
import static com.example.parkingapp.CONSTANTS.RESERVATIONID;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.parkingapp.databinding.FragmentFinalizePayBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FinalizePayFragment extends Fragment {

    EditText editTextTotalFees;
    Button buttonPay;
    private FragmentFinalizePayBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFinalizePayBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTotalFees = binding.editTextTextTotalFee;
        buttonPay = binding.buttonPay;
        editTextTotalFees.setText(RESERVATIONFEES);
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FinalizePayFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parkingFees = editTextTotalFees.getText().toString();

                String reserveId = RESERVATIONID;
                String reserveFees = RESERVATIONFEES;


                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here
                    String jsonString = "{\"BusinessShortCode" +
                            "\":174379," +
                            "\"Password" +
                            "\":\"MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjIxMDIzMjExNTA4\"," +
                            "\"Timestamp\":\"20221023211508\"," +
                            "\"TransactionType\":\"CustomerPayBillOnline\"," +
                            "\"Amount\":1," +
                            "\"PartyA" +
                            "\":254723629277,\"PartyB\":174379,\"PhoneNumber\":254723629277," +
                            "\"CallBackURL\":\"https://smartpark-2.herokuapp.com/api/payments/mpesa\"," +
                            "\"AccountReference\":\"CompanyXLTD\"," +
                            "\"TransactionDesc\":\"PaymentofX\"}";


                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body =
                            RequestBody.create(mediaType, jsonString);
                    okhttp3.Request request = new Request.Builder()
                            .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer 0c4yAIfm5zpfuTi8UcTNB5Q1zbaO")
                            .build();
                    try {
                        Response response =
                                client.newCall(request).execute();
                        int startPos = response.toString().indexOf("code=");
                        int endPos = response.toString().indexOf(", message");
                        String jsonSubString =  response.toString().substring(startPos,endPos);
                        if (jsonSubString.equalsIgnoreCase("code=200")){
                            Toast.makeText(getContext(),
                                    "Please confirm if" +
                                            " " +
                                            "the " +
                                            "reservation " +
                                            "was added to" +
                                            " the active " +
                                            "list!!"
                                    , Toast.LENGTH_SHORT).show();
                            // finally save the payment into the database

                            savePayment(reserveId, reserveFees);
                            // Restart the   main  activity
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);


                        }else{
                            Log.i("BST","it is here");

                            Snackbar.make(getContext(),buttonPay,"Transaction Failed",
                                    Snackbar.LENGTH_LONG).show();
                        }


                    } catch (IOException  e) {

                        e.printStackTrace();

                    }

                }
            }
        });
    }

    private void savePayment(String reserveId, String reserveFees) {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL.savePayment(), response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    JSONObject object = jsonObject.getJSONObject("data");

                } else {
                    Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("reserve_id", reserveId);
                params.put("reservation_fees", reserveFees);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}