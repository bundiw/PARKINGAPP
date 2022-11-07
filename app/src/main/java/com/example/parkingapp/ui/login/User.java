package com.example.parkingapp.ui.login;

public class User {
    private int userId;
    private String fullName;
    private int phoneNumber;
    private String email;
    private String Password;

    public User(String email, String password) {
        this.email = email;
        Password = password;
    }

    public User(String fullName, int phoneNumber, String email, String password) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        Password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
