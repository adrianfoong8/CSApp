package com.example.csapp;

public class Model {

    String lastName, description, distance, image;

    public Model() {
    }

    public Model(String lastName, String description, String distance, String image) {
        this.lastName = lastName;
        this.description = description;
        this.distance = distance;
        this.image = image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
