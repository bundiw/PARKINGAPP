package com.example.parkingapp.ui.login;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.CONSTANTS;
import com.example.parkingapp.MainActivity;
import com.example.parkingapp.URL;
import com.example.parkingapp.VolleySingleton;
import com.example.parkingapp.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    //defining variable to store xml layout
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView textViewRegister;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //auto log in - remembered users


        // initialise the view with the xml layout
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        textViewRegister = binding.textViewRegister;
        loadingProgressBar = binding.loading;

        loginButton.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            initialiseValidateViews();
        } else {
//            start the register activity
            Intent startMainPage = new Intent(this, RegisterActivity.class);
            startActivity(startMainPage);


        }

    }


    //initialise view with the user data
    private void initialiseValidateViews() {

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        Pattern patterns = Pattern.compile("^(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");

        //code meaning
//            Pattern.compile(“^” +
//
//                   “(?=.*[@#$%^&+=])” +     // at least 1 special character
//
//                   “(?=\\S+$)” +                     // no white spaces
//
//                   “.{4,}” +                              // at least 4 characters
//
//                   “$”)

        if (username.isEmpty()) {
            usernameEditText.requestFocus();
            usernameEditText.setError("ENTER A VALID NAME");

        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usernameEditText.requestFocus();
            usernameEditText.setError("ENTER A VALID EMAIL");

        } else if (!patterns.matcher(password).matches()) {
            passwordEditText.requestFocus();
            passwordEditText.setError("WEAK PASSWORD. INCLUDE SPECIAL CHARACTER");

        } else if (password.isEmpty()) {
            passwordEditText.requestFocus();
            passwordEditText.setError("ENTER A VALID PASSWORD");
        } else if (password.length() < 5) {
            passwordEditText.requestFocus();
            passwordEditText.setError("FIELD VALUE MUST BE AT LEAST 5 CHARACTERS");

        } else {


            //validation success
            // TODO select user from database
            //start the reserve main activity

            /*
            * to be uncommented

            */
            userLogin();



            /*
            * To be commented
            * */
//            Intent startMainPage = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(startMainPage);


            //reset to null password and  email value before moving to new activity
            try {
                sleep(200);

                passwordEditText.setText(null);
                usernameEditText.setText(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void userLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL.getLoginUrl(),
                response -> {
                    //api response
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject object = jsonObject.getJSONObject("data");
//                            System.out.println(object);
                            CONSTANTS.FULLNAME = object.getString("full_name");
                            CONSTANTS.USERID = object.getInt("id");
                            CONSTANTS.PHONENO = object.getString("phone_number");
                            CONSTANTS.UNAME = object.getString("email");

                            Intent startMainPage = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(startMainPage);

                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
                    //on error
                    Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();

                }){
            //request details

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email", usernameEditText.getText().toString().trim());
                params.put("password",passwordEditText.getText().toString().trim());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}