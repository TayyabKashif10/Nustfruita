package com.nustfruta.models;

public class User {

    UserType userType;
    String phoneNumber;
    String fullName;
    String email;
    String hostelAddress;

    public User(UserType userType, String phoneNumber, String fullName, String email, String hostelAddress) {
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.hostelAddress = hostelAddress;
    }


    // required for realtime Database
    public User() {
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHostelAddress() {
        return hostelAddress;
    }

    public void setHostelAddress(String hostelAddress) {
        this.hostelAddress = hostelAddress;
    }
}


