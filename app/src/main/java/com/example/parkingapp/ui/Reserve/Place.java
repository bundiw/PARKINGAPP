package com.example.parkingapp.ui.Reserve;

public class Place {
    private int placeId;
    private String placeName;
    private int countyId;

    public Place(int placeId, String placeName, int countyId) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.countyId = countyId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }
}
