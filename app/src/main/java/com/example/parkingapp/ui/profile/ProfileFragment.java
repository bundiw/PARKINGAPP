package com.example.parkingapp.ui.profile;

import static com.example.parkingapp.CONSTANTS.UNAME;
import static com.example.parkingapp.CONSTANTS.USERID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.CONSTANTS;
import com.example.parkingapp.MainActivity;
import com.example.parkingapp.URL;
import com.example.parkingapp.VolleySingleton;
import com.example.parkingapp.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


    // Variables to hold the view defined on the xml register layout
    private EditText fullNameEditText ;
    private EditText phoneNumberEditText ;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button editButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        // initialise the view with the xml layout
        fullNameEditText = binding.editTextTextFullName;
        phoneNumberEditText = binding.editTextPhoneNumber;
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        confirmPasswordEditText = binding.passwordConfirm;
        editButton = binding.update;
        fullNameEditText.setText(CONSTANTS.FULLNAME);
        phoneNumberEditText.setText(CONSTANTS.PHONENO);
        usernameEditText.setText(UNAME);

//        item click listener call
        editButton.setOnClickListener(v -> initialiseValidateViews());


        return root;

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    private void initialiseValidateViews() {
        String fullName = fullNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
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
            phoneNumberEditText.setError("ENTER ONLY NUMERICAL CHARACTERS");

        }else if (phoneNumber.length() != 10) {
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

            // DONE Add the edited user to the database
            // To be uncommented
            editProfile();


        }

    }

    private void editProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL.getProfileUrl(USERID), response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    fullNameEditText.setText(object.getString("full_name"));
                    phoneNumberEditText.setText(object.getString("phone_number"));
                    usernameEditText.setText(object.getString("email"));

                    CONSTANTS.FULLNAME = object.getString("full_name");
                    CONSTANTS.USERID = object.getInt("id");
                    CONSTANTS.PHONENO = object.getString("phone_number");
                    UNAME = object.getString("email");

                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "User Details Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("full_name",fullNameEditText.getText().toString());
                params.put("phone_number",phoneNumberEditText.getText().toString());
                params.put("email",usernameEditText.getText().toString());
                params.put("password",passwordEditText.getText().toString());

                return  params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}