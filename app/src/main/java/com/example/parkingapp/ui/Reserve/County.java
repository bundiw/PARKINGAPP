package com.example.parkingapp.ui.Reserve;

public class County {
    private int countyId;
    private String CountyName;

    public County(int countyId, String countyName) {
        this.countyId = countyId;
        CountyName = countyName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String countyName) {
        CountyName = countyName;
    }
}
