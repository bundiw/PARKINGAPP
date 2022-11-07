package com.example.parkingapp;

public class Reservation {

    private String placeName;
    private String lotNumber;
    private String reservedDate;
    private String reservedTimeStart;
    private String reservedTimeStop;
    private String reserveFees;

    public Reservation(String placeName, String lotNumber, String reservedDate,
                       String reservedTimeStart, String reservedTimeStop,  String reserveFees) {
        this.placeName = placeName;
        this.lotNumber = lotNumber;
        this.reservedDate = reservedDate;
        this.reservedTimeStart = reservedTimeStart;
        this.reservedTimeStop = reservedTimeStop;
        this.reserveFees = reserveFees;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getReservedTimeStart() {
        return reservedTimeStart;
    }

    public void setReservedTimeStart(String reservedTimeStart) {
        this.reservedTimeStart = reservedTimeStart;
    }

    public String getReservedTimeStop() {
        return reservedTimeStop;
    }

    public void setReservedTimeStop(String reservedTimeStop) {
        this.reservedTimeStop = reservedTimeStop;
    }


    public String getReserveFees() {
        return reserveFees;
    }

    public void setReserveFees(String reserveFees) {
        this.reserveFees = reserveFees;
    }
}
