package com.example.csapp;

public class UploadInformation {

    String accountType;
    String firstName;
    String lastName;
    String dateOfBirth;
    String age;
    String phoneNumber;
    String nationality;
    String state;
    String description;
    String image;

    public UploadInformation() {
    }

    public UploadInformation(String accountType, String firstName, String lastName, String dateOfBirth,
                             String age, String phoneNumber, String nationality, String state,
                             String description, String image) {
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.state = state;
        this.description = description;
        this.image = image;
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

    public String getNationality() {
        return nationality;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
