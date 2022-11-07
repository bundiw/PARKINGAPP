package com.example.parkingapp.ui.Reserve;

public class ReserveLot {
    private int placeId,id;
    private String currentUseState,lotNumber;
    private double lotPrice;

    public ReserveLot(int placeId, int id, String currentUseState, String lotNumber, double lotPrice) {
        this.placeId = placeId;
        this.id = id;
        this.currentUseState = currentUseState;
        this.lotNumber = lotNumber;
        this.lotPrice = lotPrice;
    }

    @Override
    public String toString() {
        return "ReserveLot{" +
                "placeId=" + placeId +
                ", id=" + id +
                ", currentUseState='" + currentUseState + '\'' +
                ", lotNumber='" + lotNumber + '\'' +
                ", lotPrice=" + lotPrice +
                '}';
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurrentUseState(String currentUseState) {
        this.currentUseState = currentUseState;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public void setLotPrice(double lotPrice) {
        this.lotPrice = lotPrice;
    }

    public int getPlaceId() {
        return placeId;
    }

    public int getId() {
        return id;
    }

    public String getCurrentUseState() {
        return currentUseState;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public double getLotPrice() {
        return lotPrice;
    }
}