package com.example.emergencyapp;

public class RescueAgent {
    String firstName, lastName, wilaya, dayra, phoneNumber;
    String password;
    RescueTypes rescueType;
    boolean disponible;

    public RescueAgent(String firstName, String lastName, String wilaya, String dayra, String phoneNumber, String password, RescueTypes rescueType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.wilaya = wilaya;
        this.dayra = dayra;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.rescueType = rescueType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getDayra() {
        return dayra;
    }

    public void setDayra(String dayra) {
        this.dayra = dayra;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RescueTypes getRescueType() {
        return rescueType;
    }

    public void setRescueType(RescueTypes rescueType) {
        this.rescueType = rescueType;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

