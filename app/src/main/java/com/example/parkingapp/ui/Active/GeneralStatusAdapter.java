package com.example.parkingapp.ui.Active;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.PayActivity;
import com.example.parkingapp.R;
import com.example.parkingapp.Reservation;
import com.example.parkingapp.ui.Reserve.ReserveLot;

import java.util.List;

public class GeneralStatusAdapter extends RecyclerView.Adapter<GeneralStatusAdapter.ViewHolder> {
    private List<Reservation> reservationsList;
    private Context context;
    public GeneralStatusAdapter(List<Reservation> reservationsList) {
        this.reservationsList = reservationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_model,parent, false);
        context =parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        this.textViewPlaceName = itemView.findViewById(R.id.textViewPlace) ;
//        this.textViewLotNumber = itemView.findViewById(R.id.textViewLotNumber) ;
//        this.textViewReserveDate = itemView.findViewById(R.id.textViewReserveDate)  ;
//        this.textViewAppointment = itemView.findViewById(R.id.textViewAppointment) ;
//        this.textViewPayableFee = itemView.findViewById(R.id.textViewPaymentFees);
        holder.textViewPlaceName.setText(reservationsList.get(position).getPlaceName());
        holder.textViewLotNumber.setText(String.format("Lot %s",reservationsList.get(position).getLotNumber()));
        holder.textViewReserveDate.setText(String.format("On Date %s",reservationsList.get(position).getReservedDate()));
        holder.textViewAppointment.setText(String.format("Between %s-%s",reservationsList.get(position).getReservedTimeStart(),reservationsList.get(position).getReservedTimeStop()));
        holder.textViewPayableFee.setText(String.format("Paid: sh %s",reservationsList.get(position).getReserveFees()));


    }

    @Override
    public int getItemCount() {
        return reservationsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
//        Reservation(String placeName, String lotNumber, String reservedDate,
//                       String reservedTimeStart, String reservedTimeStop, String activeState, String reserveFees) {
//
        private final TextView textViewPlaceName, textViewLotNumber, textViewReserveDate, textViewAppointment, textViewPayableFee;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewPlaceName = itemView.findViewById(R.id.textViewReservePlace) ;
            this.textViewLotNumber = itemView.findViewById(R.id.textViewReserveLot) ;
            this.textViewReserveDate = itemView.findViewById(R.id.textViewReserveDate)  ;
            this.textViewAppointment = itemView.findViewById(R.id.textViewAppointment) ;
            this.textViewPayableFee = itemView.findViewById(R.id.textViewPaymentFees);
        }
    }
}
