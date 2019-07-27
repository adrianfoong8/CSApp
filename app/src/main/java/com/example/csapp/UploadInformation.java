package com.example.csapp;

public class UploadInformation {

    String name;
    String description;
    String image;

    public UploadInformation() {
    }

    public UploadInformation(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
