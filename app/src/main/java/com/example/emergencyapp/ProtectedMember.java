package com.example.emergencyapp;

public class ProtectedMember {
    String firstName;
    String lastName;
    String gender;
    String adress;
    String pathologies;
    String email ;
    String password;
    int phoneNumber;
    int yearOfBith;
    BloodGroups bloodGroup;

    public ProtectedMember(String firstName, String lastName, String gender, String adress, int yearOfBirth, int phoneNumber, String email, String password, String pathologies, BloodGroups bloodGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.yearOfBith = yearOfBirth;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.email=email;
        this.password = password;
        this.pathologies = pathologies;
        this.bloodGroup = bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPathologies() {
        return pathologies;
    }

    public void setPathologies(String pathologies) {
        this.pathologies = pathologies;
    }

    public BloodGroups getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroups bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getYearOfBith() {
        return yearOfBith;
    }

    public void setYearOfBith(int yearOfBith) {
        this.yearOfBith = yearOfBith;
    }


    @Override
    public String toString() {
        return "ProtectedMember{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", adress='" + adress + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", pathologies='" + pathologies + '\'' +
                ", password='" + password + '\'' +
                ", yearOfBith=" + yearOfBith +
                ", bloodGroup=" + bloodGroup +
                '}';
    }
}
