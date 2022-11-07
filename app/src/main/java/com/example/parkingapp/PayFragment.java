package com.example.parkingapp;

import static android.text.format.DateFormat.is24HourFormat;

import static com.example.parkingapp.CONSTANTS.RESERVATIONFEES;
import static com.example.parkingapp.CONSTANTS.RESERVATIONID;
import static com.example.parkingapp.CONSTANTS.USERID;
import static java.text.MessageFormat.format;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.databinding.FragmentStartPayBinding;
import com.example.parkingapp.ui.Reserve.ReserveLot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayFragment extends Fragment  {

    int day, month, year, hour, minute;
    int myDay, myMonth, myYear, myHour, myMinute;
    String todayDate;
    Button buttonDate, buttonStart, buttonEnd;
    EditText editTextDate, editTextStart, editTextEnd,editTextPlateNumber;
    Spinner spinnerCarCategory;
    TextView textViewError;
    private FragmentStartPayBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStartPayBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonDate = binding.buttonDate;
        buttonStart = binding.buttonStartTime;
        buttonEnd = binding.buttonEndTime;
        editTextDate = binding.editTextDate;
        editTextStart = binding.editTextStart;
        editTextEnd = binding.editTextEnd;
        spinnerCarCategory = binding.spinnerCarCategory;
        editTextPlateNumber = binding.editTextPlateNumber;
        textViewError = binding.textViewError;



        initialiseVehicleCategory(spinnerCarCategory);

        binding.buttonFirst.setOnClickListener(view12 -> {


            if (editTextPlateNumber.getText().toString().isEmpty() ){
                editTextPlateNumber.requestFocus();
                editTextPlateNumber.setError("ENTER VALID VEHICLE PLATE NUMBER ALSO INDICATE APPROPRIATE VEHICLE CATEGORY");
            }else if (editTextPlateNumber.getText().toString().length() <4){
                editTextPlateNumber.setError("ENTER VALID VEHICLE PLATE NUMBER AND INDICATE APPROPRIATE VEHICLE CATEGORY");

            }

            else if (editTextDate.getText().toString().isEmpty() ){
                editTextDate.requestFocus();
                editTextDate.setError("ENTER RESERVE DATE");
            }
            else if (editTextStart.getText().toString().isEmpty() ){
                editTextStart.requestFocus();
                editTextStart.setError("ENTER START TIME");
            }
            else if (editTextEnd.getText().toString().isEmpty()) {
                editTextEnd.requestFocus();
                editTextEnd.setError("ENTER ENDING TIME");

            }else{
                Intent intent = getActivity().getIntent();
                String currentUse= intent.getStringExtra("currentUse");

                String placeId= String.valueOf(intent.getIntExtra ("placeID",0));
                String lotId= intent.getStringExtra("lotID");
                String lotNumber= intent.getStringExtra("lotNumber");
                String lotPrice = intent.getStringExtra("lotPrice");

                //save the data to the database
                StringRequest  stringRequest =   new StringRequest(Request.Method.POST, URL.getReserveUrl(), response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject object = jsonObject.getJSONObject("data");

                            CONSTANTS.RESERVATIONID = object.getString("id");
                            String endTime = object.getString("end_time");
                            String startTime = object.getString("start_time");

                            SimpleDateFormat sdf
                                    = new SimpleDateFormat("HH:mm");
                            try {
                                double diff = (sdf.parse(endTime).getTime()- sdf.parse(startTime).getTime())/(1000.00 * 60 * 60);


                                diff =  Math.ceil(diff);

                                CONSTANTS.RESERVATIONFEES = String.valueOf(diff * Float.parseFloat(lotPrice));

                                NavHostFragment.findNavController(PayFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);



                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show()){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params  = new HashMap<>();


                        params.put("lot_id",lotId);
                        params.put("vehicle_category",spinnerCarCategory.getSelectedItem().toString());
                        params.put("vehicle_plate",editTextPlateNumber.getText().toString());
                        params.put("reserve_date",editTextDate.getText().toString());
                        params.put("start_time",editTextStart.getText().toString());
                        params.put("end_time",editTextEnd.getText().toString());
                        params.put("user_id",String.valueOf(CONSTANTS.USERID));
                        return params;
                    }
                };
                VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
            }

        });


        buttonStart.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH)+1;
            day = calendar.get(Calendar.DAY_OF_MONTH);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                String selectedHour = String.valueOf( hourOfDay).length()< 2?"0"+hourOfDay: String.valueOf(hourOfDay);
                String selectedMinutes = String.valueOf( minute).length()< 2?"0"+minute: String.valueOf(minute);

                editTextStart.setText(format("{0}:{1}",
                         selectedHour, selectedMinutes));
                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String now = simpleDateFormat.format(currentTime);

                if (editTextStart.getText().toString().compareTo(now) < 0 && editTextDate.getText().toString().compareTo(todayDate) == 0 ){
                    editTextStart.setText(null);
                    editTextEnd.setText(null);

                    Toast.makeText(getContext(), "Time must be now or future", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = getActivity().getIntent();
                    String lotId= intent.getStringExtra("lotID");
                    textViewError.setText(null);
                    editTextEnd.setText(null);
                    textViewError.setBackgroundColor(Color.WHITE);
                    String startTime = editTextStart.getText().toString();
                    String reserveDate = editTextDate.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.getReservationUrl(Integer.parseInt(lotId)), (Response.Listener<String>) response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if (array.length() == 0) {
                                    editTextStart.setText(startTime);

                                }else{
                                    String message ="Booked times include";
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        message += object.getString("start_time");
                                        message += "-";
                                        message += object.getString("end_time");
                                        message += ", ";

                                    }

                                    editTextStart.setText(null);
                                    editTextEnd.setText(null);
                                    textViewError.setText(message);
                                    textViewError.setBackgroundColor(Color.RED);

                                }



                            }else{
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, (Response.ErrorListener) error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show()){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("start_time",startTime);
                            params.put("reserve_date",reserveDate);

                            return params;
                        }
                    };
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

//                    Intent intent = getActivity().getIntent();
//                    String currentUse= intent.getStringExtra("currentUse");
//                    if (currentUse.equalsIgnoreCase("red")) {
//                        Toast.makeText(getContext(), "The selected time is already booked", Toast.LENGTH_SHORT).show();
//                        editTextStart.setText(null);
//                    }else{
//
//
//                    }
                }

            }, year, month, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();

        });

        buttonEnd.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (OnTimeSetListener) (TimePicker view1, int hourOfDay, int minute) -> {
                String selectedHour = String.valueOf( hourOfDay).length()< 2?"0"+hourOfDay: String.valueOf(hourOfDay);
                String selectedMinutes = String.valueOf( minute).length()< 2?"0"+minute: String.valueOf(minute);

                editTextEnd.setText(format("{0}:{1}",
                         selectedHour, selectedMinutes));
                if (editTextEnd.getText().toString().compareTo(editTextStart.getText().toString()) < 1){
                    editTextEnd.setText(null);
                    Toast.makeText(getContext(), "End Time must be greater than Start time", Toast.LENGTH_SHORT).show();
                }else {


                    Intent intent = getActivity().getIntent();
                    String lotId= intent.getStringExtra("lotID");
                    textViewError.setText(null);
                    textViewError.setBackgroundColor(Color.WHITE);
                    String endTime = editTextEnd.getText().toString();
                    String reserveDate = editTextDate.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.getReservationUrl(Integer.parseInt(lotId)), (Response.Listener<String>) response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                if (array.length() == 0) {
                                    editTextEnd.setText(endTime);

                                }else{
                                    String message ="Booked times include";
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        message += object.getString("start_time");
                                        message += "-";
                                        message += object.getString("end_time");
                                        message += ", ";

                                    }

                                    editTextStart.setText(null);
                                    editTextEnd.setText(null);
                                    textViewError.setText(message);
                                    textViewError.setBackgroundColor(Color.RED);

                                }



                            }else{
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, (Response.ErrorListener) error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show()){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("start_time",endTime);
                            params.put("reserve_date",reserveDate);

                            return params;
                        }
                    };
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);



                }
            }, year, month, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();

        });

        buttonDate.setOnClickListener(v -> {


            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String selectedMonth = Integer.toString(month).length()<2?"0"+ month
                                    :Integer.toString(month+1);
                            String selectedDay = Integer.toString(day).length()<2?"0"+ day :Integer.toString(day);
                            editTextDate.setText(String.format("%s-%s-%s", year, selectedMonth, selectedDay));
                            Date currentDate = Calendar.getInstance().getTime();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            todayDate = simpleDateFormat.format(currentDate);

                            editTextStart.setText(null);
                            editTextEnd.setText(null);
                            System.out.println(editTextDate.getText().toString()+" SELECTED MNTTH" +
                                    " "+todayDate);
                            if (editTextDate.getText().toString().compareTo(todayDate) < 0 ) {
                                Toast.makeText(getContext(), "Date must be greater or equal to today", Toast.LENGTH_SHORT).show();
                                editTextDate.setText(null);
                            }

                        }
                    }, year, month, day);
            datePickerDialog.show();


        });

    }

    private void initialiseVehicleCategory(View v) {
        ArrayList<CarCategory> carCatList= new ArrayList<>();
//        list of counties object where parking are available


        //the CarCategory name collection inorder to e dispalyed into the spiiner
        ArrayList<String> carCategoryName = new ArrayList<>();

        //sample CarCategory object
        carCatList.add(0, new CarCategory(1, "Bus"));
        carCatList.add(0, new CarCategory(2, "MiniBus"));
        carCatList.add(1, new CarCategory(3, "Truck"));
        carCatList.add(2, new CarCategory(4, "Van"));
        carCatList.add(2, new CarCategory(5, "Car"));
        carCatList.add(2, new CarCategory(6, "Shuttle"));
//        adding the CarCategory name into the collection for display into the spinnner
        for (CarCategory carCategoryOb :
                carCatList) {
            carCategoryName.add(carCategoryOb.getCategoryName());

        }
        //setting & inflating to spinner adapter with CarCategory name collection
        ArrayAdapter<String> spinnerArrayAdapterCarCategory = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, carCategoryName);
        //selecting supported spinner style/type

        spinnerArrayAdapterCarCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//       inflating spinner adapter into the spinner
        spinnerCarCategory.setAdapter(spinnerArrayAdapterCarCategory);


    }

    //    bus, minibus, shuttle, van, truck,  car
//    initialiseVehicleCategory();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}