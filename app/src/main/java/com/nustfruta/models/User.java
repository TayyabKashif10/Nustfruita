package com.nustfruta.models;

import com.google.firebase.database.Exclude;

public class User {

    private UserType userType;
    private String phoneNumber;
    private String fullName;
    private String email;
    private String hostel;
    private String roomNumber;

    public User(UserType userType, String phoneNumber, String fullName, String email, String hostel, String roomNumber) {
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.hostel = hostel;
        this.roomNumber = roomNumber;
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


    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Exclude
    public boolean isCompleteProfile()
    {
        return !this.getEmail().isEmpty() && !this.getFullName().isEmpty() && !this.getHostel().isEmpty()  && !this.getRoomNumber().isEmpty();
    }

    @Exclude
    public String getFormattedPhone() {
        return '0' + this.phoneNumber.substring(3, 6) + ' ' + this.phoneNumber.substring(6);
    }

    @Exclude
    public String getFormattedAddress() {
        return this.hostel + ' ' + this.roomNumber;
    }
}