package com.example.emergencyapp;

public class RescueAgent extends ProtectedMember {

    String wilaya, dayra;
    RescueTypes rescueType;
    boolean disponible;
    int id ;

    public RescueAgent(String firstName, String lastName, String gender, String adress, int Age, int phoneNumber, String email, String password, String pathologies, BloodGroups bloodGroup,
                       String wilaya, String dayra, RescueTypes rescueType) {
        super(firstName, lastName, gender, adress, Age, phoneNumber, email, password, pathologies, bloodGroup);
        this.wilaya = wilaya;
        this.dayra = dayra;
        this.rescueType = rescueType;
        id = 2;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", age=" + Age +
                ", bloodGroup=" + bloodGroup +
                '}';
    }
}

