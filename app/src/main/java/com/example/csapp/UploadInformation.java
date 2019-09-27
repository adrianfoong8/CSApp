package com.example.csapp;

public class UploadInformation {

    String accountType;
    String verified;
    String firstName;
    String lastName;
    String dateOfBirth;
    String age;
    String phoneNumber;
    String state;
    String image;

    public UploadInformation() {
    }

    public UploadInformation(String accountType,String verified, String firstName, String lastName, String dateOfBirth,
                             String age, String phoneNumber, String state, String image) {
        this.accountType = accountType;
        this.verified= verified;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.image = image;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getVerified() {
        return verified;
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
