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
    String image;

    public UploadInformation() {
    }

    public UploadInformation(String uid, String accountType, String firstName, String lastName, String dateOfBirth, String age, String phoneNumber, String state, String image) {
        this.uid = uid;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getState() {
        return state;
    }

    public String getImage() {
        return image;
    }
}
