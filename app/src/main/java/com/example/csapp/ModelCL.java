package com.example.csapp;

public class ModelCL {

    String firstName, lastName, image, uid, state, age, phoneNumber, tnc, otnc;

    public ModelCL() {
    }

    public ModelCL(String firstName, String lastName, String image, String uid, String state, String age, String phoneNumber, String tnc, String otnc) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.uid = uid;
        this.state = state;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.tnc = tnc;
        this.otnc = otnc;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
}
