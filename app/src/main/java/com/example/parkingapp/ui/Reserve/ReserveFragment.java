package com.example.parkingapp.ui.Reserve;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.CONSTANTS;
import com.example.parkingapp.URL;
import com.example.parkingapp.VolleySingleton;
import com.example.parkingapp.databinding.FragmentReserveBinding;
import com.example.parkingapp.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReserveFragment extends Fragment {

    private FragmentReserveBinding binding;
    private RecyclerView recyclerViewReserve;
    private Spinner spinnerPlace;
    //        adapter variable to populate to the spinner dropdown
    // adapter variable to populate to the spinner dropdown
    ArrayAdapter<String> spinnerArrayAdapterPlace;
    ArrayAdapter<String> spinnerArrayAdapterCounty;

    ArrayList<County> county = new ArrayList<>();
    ArrayList<Place> place = new ArrayList<>();
    private List<ReserveLot> lotList = new ArrayList<>();
    ReserveAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReserveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerViewReserve = binding.recycleLot;

        final TextView textView = binding.textViewSearch;
        final Spinner spinnerCounty = binding.spinnerCounty;
        spinnerPlace = binding.spinnerPlace;
         //logout if app crashes
         if (CONSTANTS.USERID == 0){
             Intent intent = new Intent(getContext(), LoginActivity.class);
             startActivity(intent);
         }
         // clear the  data of county to avoid appending
        county.clear();
//        show the available listing of the counties to show parking lots
        populateSpinnerCounty(spinnerCounty);

        return root;
    }


    private void populateSpinnerPlace(Spinner spinnerPlace, String countyName) {

        //the place name collection in order to be displayed into the spinner
        ArrayList<String> placeName = new ArrayList<>();

        County chosenCounty = county.stream().filter(e->e.getCountyName().matches(countyName)).collect(Collectors.toList()).get(0);
        int countyId  = chosenCounty.getCountyId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.getPlaceUrl(countyId), response -> {

            try {
                JSONObject jsonObject = new JSONObject((response));
                if (jsonObject.getBoolean("success") ) {

                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                  //  System.out.println(jsonArray);
                    //  list of Places objects where parking are available
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //sample places object
                        JSONObject object = jsonArray.getJSONObject(i);
                        place.add(new Place(object.getInt("id"), object.getString("place_name"), object.getInt("county_id")));
                        //int placeId, String placeName, int countyId


                    }
                    for (Place object:
                         place) {
                        placeName.add(object.getPlaceName());

                    }

                    //setting & inflating to spinner adapter with place_name collection
                    spinnerArrayAdapterPlace = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, placeName);

                    //selecting supported spinner style/type
                    spinnerArrayAdapterPlace.setDropDownViewResource(android.R.layout.simple_spinner_item);
                   //       inflating spinner adapter into the spinner
                    spinnerPlace.setAdapter(spinnerArrayAdapterPlace);
                    spinnerArrayAdapterCounty.getDropDownViewTheme();



                }else{
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlace = spinnerPlace.getSelectedItem().toString();
                //clear cache to keep the state the same
                lotList.clear();
                recyclerViewReserve.setAdapter(null);
                setReserveLot(selectedPlace);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinnerCounty(Spinner spinnerCounty) {

       //        list of counties object where parking are available

        //the county name collection inorder to e dispalyed into the spiiner
        ArrayList<String> countyName = new ArrayList<>();

        //sample county object
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL.getCountyUrl(), (Response.Listener<String>) response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success") ) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
//                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);


                        county.add( new County(object.getInt("id"),object.getString("county_name")));
                    }
//                    System.out.println(county);


                    //  adding the county name into the collection for display into the spinnner
                    for (County countyOb :
                            county) {
                        countyName.add(countyOb.getCountyName());

                    }
                    //setting & inflating to spinner adapter with county name collection
                    spinnerArrayAdapterCounty = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, countyName);
                    //selecting supported spinner style/type
                    spinnerArrayAdapterCounty.setDropDownViewResource(android.R.layout.simple_spinner_item);
//       inflating spinner adapter into the spinner
                    spinnerCounty.setAdapter(spinnerArrayAdapterCounty);

                    //updating the place spinner

                    spinnerCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedCountyName = spinnerCounty.getSelectedItem().toString();
                            // Toast.makeText(getContext(), "Hello county "+selectedCountyName, Toast.LENGTH_SHORT).show();
                            // show the available listing of the Place with parking lot based on county selected
                            //below 2 lines clear the initial values of the cached variables to be maintai a current state
                            spinnerPlace.setAdapter(null);
                            place.clear();
                            populateSpinnerPlace(spinnerPlace,selectedCountyName);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }else{
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }){

        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }


    private void setReserveLot(String placeName) {
//        get place id

        Place chosenPlace = place.stream().filter(e->e.getPlaceName().matches(placeName)).collect(Collectors.toList()).get(0);
        int placeId  = chosenPlace.getPlaceId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.getLotUrl(placeId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject =new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                       JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        System.out.println(jsonArray);
                        for (int i = 0; i < jsonArray.length() ; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            lotList.add(new ReserveLot(object.getInt("place_id"),
                                    object.getInt("id"),
                                    object.getString("current_use"),
                                    object.getString("lot_number"),
                                    object.getDouble("lot_price")
                                    ));

                        }

                        System.out.println(lotList.toString()+" nev say die");
                        adapter = new ReserveAdapter(lotList);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerViewReserve.setLayoutManager(manager);
                        recyclerViewReserve.setAdapter(adapter);


                    }else {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}