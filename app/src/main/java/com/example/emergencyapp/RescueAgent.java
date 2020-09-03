package com.example.emergencyapp;

public class RescueAgent extends ProtectedMember {

    String wilaya, dayra;
    RescueTypes rescueType;
    boolean disponible;

    public RescueAgent(String firstName, String lastName, String gender, String adress, int age, int phoneNumber, String email, String password, String pathologies, BloodGroups bloodGroup,
                       String wilaya, String dayra, RescueTypes rescueType) {
        super(firstName, lastName, gender, adress, age, phoneNumber, email, password, pathologies, bloodGroup);
        this.wilaya = wilaya;
        this.dayra = dayra;
        this.rescueType = rescueType;
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

    @Override
    public String toString() {
        return "RescueAgent{" +
                "wilaya='" + wilaya + '\'' +
                ", dayra='" + dayra + '\'' +
                ", rescueType=" + rescueType +
                ", disponible=" + disponible +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", adress='" + adress + '\'' +
                ", pathologies='" + pathologies + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", age=" + age +
                ", bloodGroup=" + bloodGroup +
                '}';
    }
}

