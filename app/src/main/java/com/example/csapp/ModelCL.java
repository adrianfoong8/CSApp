package com.example.csapp;

public class ModelCL {

    String firstName, lastName, image, uid;

    public ModelCL() {
    }

    public ModelCL(String firstName, String lastName, String image, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.uid = uid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
