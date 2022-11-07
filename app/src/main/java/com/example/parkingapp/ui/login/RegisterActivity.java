package com.example.parkingapp.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.CONSTANTS;
import com.example.parkingapp.MainActivity;
import com.example.parkingapp.URL;
import com.example.parkingapp.VolleySingleton;
import com.example.parkingapp.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityRegisterBinding binding;

    // Variables to hold the view defined on the xml register layout
    private EditText fullNameEditText ;
    private EditText phoneNumberEditText ;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private ProgressBar loadingProgressBar ;
    private TextView textViewRegister ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // initialise the view with the xml layout
        fullNameEditText = binding.editTextTextFullName;
        phoneNumberEditText = binding.editTextPhoneNumber;
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        confirmPasswordEditText = binding.passwordConfirm;
        registerButton = binding.register;
        loadingProgressBar = binding.loading;
        textViewRegister = binding.textViewLogin;

//        item click listener call
        registerButton.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
    }


//item click listener implementation
    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            //start MainActivity
            initialiseValidateViews();


        }else {
            //start new User Registration activity
            Intent startRegister = new Intent(this, LoginActivity.class);
            this.startActivity(startRegister);
        }
    }



    //initialise view with the user data
    private void initialiseValidateViews() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
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

        if (fullName.isEmpty() || fullName.length() < 3 || fullName.length() > 30) {
            fullNameEditText.requestFocus();
            fullNameEditText.setError("ENTER A VALID NAME BETWEEN 3 - 30 CHARACTERS");
        }
        else if(!fullName.matches("[a-zA-Z ]+")){
            fullNameEditText.requestFocus();
            fullNameEditText.setError("ENTER ONLY ALPHABETICAL CHARACTER");

        }else if (phoneNumber.isEmpty()) {
            phoneNumberEditText.requestFocus();
            phoneNumberEditText.setError("ENTER A VALID PHONE NUMBER");

        }else if (!phoneNumber.matches("[0-9]+")) {
            phoneNumberEditText.requestFocus();
            phoneNumberEditText.setError("ENTER ONLY 10 NUMERICAL CHARACTERS");

        }else if (phoneNumber.length() != 10) {
            System.out.println(phoneNumber.length());
            phoneNumberEditText.requestFocus();
            phoneNumberEditText.setError("PHONE NUMBER MUST BE 10 CHARACTERS");

        }else if (username.isEmpty()) {
            usernameEditText.requestFocus();
            usernameEditText.setError("ENTER A VALID NAME");

        }  else if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            usernameEditText.requestFocus();
            usernameEditText.setError("ENTER A VALID EMAIL");

        }else if (password.isEmpty()){
            passwordEditText.requestFocus();
            passwordEditText.setError("ENTER A VALID PASSWORD");
        }else if (password.length() < 5 ) {
            passwordEditText.requestFocus();
            passwordEditText.setError("FIELD VALUE MUST BE AT LEAST 5 CHARACTERS");

        } else if (! patterns.matcher(password).matches()) {
            passwordEditText.requestFocus();
            passwordEditText.setError("WEAK PASSWORD. INCLUDE SPECIAL CHARACTER");

        }else if(!password.equals(confirmPassword)){
            confirmPasswordEditText.requestFocus();
            confirmPasswordEditText.setError("FIELD VALUE MUST MATCH THE PASSWORD");
        }else {

            // TODO Add the new user to the database
            // To be uncommented
             userRegistration();

//            To be commented
//            Intent startMainPage = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(startMainPage);

        }

    }
    private void userRegistration() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL.getRegisterUrl(),
                response -> {
                    //api response

                    try {
                        JSONObject jsonObject =new JSONObject(response);
                        if(jsonObject.getBoolean("success")){

                            JSONObject object = jsonObject.getJSONObject("data");
                            CONSTANTS.FULLNAME = object.getString("full_name");
                            CONSTANTS.USERID = object.getInt("id");
                            CONSTANTS.PHONENO = object.getString("phone_number");

                            Intent startMainPage = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(startMainPage);

                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
                    //on error
                    Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();

                }){
             //request details
            @Override
            protected Map<String,String> getParams(){
                HashMap<String,String> params = new HashMap<>();
                params.put("full_name",fullNameEditText.getText().toString().trim());
                params.put("phone_number", phoneNumberEditText.getText().toString().trim());
                params.put("email",usernameEditText.getText().toString().trim());
                params.put("password",passwordEditText.getText().toString().trim());

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}