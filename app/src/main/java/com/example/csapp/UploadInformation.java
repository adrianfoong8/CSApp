package com.example.csapp;

public class UploadInformation {

    String uid;
    String accountType;
    String firstName;
    String lastName;
    String dateOfBirth;
    String age;
    String phoneNumber;
    String state;
    String tnc;
    String otnc;
    String image;

    public UploadInformation() {
    }

    public UploadInformation(String uid, String accountType, String firstName, String lastName, String dateOfBirth, String age, String phoneNumber, String state, String tnc, String otnc, String image) {
        this.uid = uid;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.tnc = tnc;
        this.otnc = otnc;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getOtnc() {
        return otnc;
    }

    public void setOtnc(String otnc) {
        this.otnc = otnc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
