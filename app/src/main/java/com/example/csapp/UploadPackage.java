package com.example.csapp;

public class UploadPackage {

    String packageId;
    String packagePhoto;
    String packageName;
    String packagePrice;
    String packageDescription;

    public UploadPackage() {
    }

    public UploadPackage(String packageId, String packagePhoto, String packageName, String packagePrice, String packageDescription) {
        this.packageId = packageId;
        this.packagePhoto = packagePhoto;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageDescription = packageDescription;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getPackagePhoto() {
        return packagePhoto;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public String getPackageDescription() {
        return packageDescription;
    }
}
