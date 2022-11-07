package com.example.parkingapp.ui.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.parkingapp.CONSTANTS;
import com.example.parkingapp.Reservation;
import com.example.parkingapp.URL;
import com.example.parkingapp.VolleySingleton;
import com.example.parkingapp.databinding.FragmentHistoryBinding;
import com.example.parkingapp.ui.Active.GeneralStatusAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private RecyclerView recyclerViewHistory;
    private GeneralStatusAdapter generalStatusAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerViewHistory = binding.recyclePastReserve;
        populateRecyclerLayout();
        return root;
    }

    private void populateRecyclerLayout() {
//       current time and date


        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ;
        String now = simpleTimeFormat.format(currentTime);
        String date = simpleDateFormat.format(currentTime);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL.getUserReservation(CONSTANTS.USERID, date, now, "history"),
                response -> {

//                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                reservationList.add(new Reservation(
                                        object.getString("place_name"),
                                        object.getString("lot_number"),
                                        object.getString("reserved_date"),
                                        object.getString("reserved_start_time"),
                                        object.getString("reserved_stop_time"),
                                        object.getString("reservation_fees")
                                ));
//
                                System.out.println(reservationList);
                                generalStatusAdapter = new GeneralStatusAdapter(reservationList);
                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                                recyclerViewHistory.setLayoutManager(manager);
                                recyclerViewHistory.setAdapter(generalStatusAdapter);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

        }
        );

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}