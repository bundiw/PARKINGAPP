package com.example.parkingapp;

public class URL {
    private final static String baseUrl ="https://smartpark-2.herokuapp.com/api";
    private final static String registerUrl = baseUrl+"/users/create";
    private final static String loginUrl = baseUrl+"/login";
    private final static String countyUrl = baseUrl+"/counties";
    private final static String placeUrl = baseUrl+"/counties";
    private final static String lotUrl = baseUrl+"/places";
    private final static String reserveUrl = baseUrl+"/reservations/create";
    private final static String reservations = baseUrl+"/lots/";
    private final static String activeUrl = baseUrl+"/active";
    private final static String historyUrl = baseUrl+"/history";
    private final static String profileUrl = baseUrl+"/users/";
    private final static String payments = baseUrl+"/payments/create";
    private final static String userReservation = baseUrl+"/users";
    private static String getMpesaConfirmation = baseUrl+"/payments/mpesa";


    public static String getRegisterUrl() {
        return registerUrl;
    }

    public static String getLoginUrl() {
        return loginUrl;
    }

    public static String getCountyUrl() {
        return countyUrl;
    }


    public static String getPlaceUrl(int countyId) {
        return placeUrl+"/"+countyId+"/places";
    }

    public static String getLotUrl(int placeId) {
        return lotUrl+"/"+placeId+"/lots";
    }

    public static String getReserveUrl() {
        return reserveUrl;
    }

    public static String getActiveUrl() {
        return activeUrl;
    }

    public static String getHistoryUrl() {
        return historyUrl;
    }

    public static String getProfileUrl(int userId) {
        return profileUrl+userId+"/edit";
    }

    public static String getReservationUrl(int lotId) {
        return reservations+lotId+"/reservations";
    }

    public static String savePayment() {
        return payments;
    }

    public static String getUserReservation(int userId,String dateToday, String timeNow, String reserveState) {
        return userReservation+"/"+userId+"/reservations/"+dateToday+"/"+timeNow+"?reserve_state="+reserveState;
    }

    public static String getMpesaConfirmation() {
        return getMpesaConfirmation;
    }
}
