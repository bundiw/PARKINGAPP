package com.example.parkingapp.ui.Reserve;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.parkingapp.MainActivity;
import com.example.parkingapp.PayActivity;
import com.example.parkingapp.R;
import com.google.android.material.divider.MaterialDivider;

import org.intellij.lang.annotations.JdkConstants;

import java.util.List;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {
    private List<ReserveLot> lotList;
    private Context context;
    public ReserveAdapter(List<ReserveLot> lotList) {
        this.lotList = lotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent, false);
        context =parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        placeName, lotNumber, currentUseState; "Fees sh 50 per Hour "
//        holder.textViewParkPlace.setText(String.format("Situated @ %s", lotList.get(position).getPlaceId()));
        holder.textViewLotNumber.setText(String.format("Lot# %s", lotList.get(position).getLotNumber()));
        holder.textViewLotUnitPrice.setText(String.format("Fees sh %s per Hour ", lotList.get(position).getLotPrice()));
        String color = lotList.get(position).getCurrentUseState();
        int colorChoice =0;
        switch(color){
            case "yellow":
                colorChoice =Color.YELLOW;
                break;
            case "red":
                colorChoice = Color.RED;
                break;
            default:
                colorChoice = Color.GREEN;

        }
        int placeID = lotList.get(position).getPlaceId();
        String lotNumber = lotList.get(position).getLotNumber();
        String currentUse = lotList.get(position).getCurrentUseState();
        String lotPrice = String.valueOf( lotList.get(position).getLotPrice());
        String lotID = String.valueOf(lotList.get(position).getId());

        holder.dividerIndicator.setBackgroundColor(colorChoice);
        holder.buttonReserve.setOnClickListener(v -> {
            Intent startPay = new Intent(context, PayActivity.class);
            startPay.putExtra("lotID",lotID);
            startPay.putExtra("placeID",placeID);
            startPay.putExtra("lotNumber",lotNumber);
            startPay.putExtra("currentUse",currentUse);
            startPay.putExtra("lotPrice",lotPrice);
            context.startActivity(startPay);
        });
    }

    @Override
    public int getItemCount() {
        return lotList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewLotNumber, textViewLotUnitPrice;
        private final View dividerIndicator;
        private Button buttonReserve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewLotNumber = itemView.findViewById(R.id.textViewLotNumber) ;
//            this.textViewParkPlace = itemView.findViewById(R.id.textViewParkPlace);
            this.textViewLotUnitPrice = itemView.findViewById(R.id.textViewLotUnitPrice);
            this.dividerIndicator = itemView.findViewById(R.id.dividerIndicator);

            this.buttonReserve = itemView.findViewById(R.id.buttonReserve);

        }
    }
}
