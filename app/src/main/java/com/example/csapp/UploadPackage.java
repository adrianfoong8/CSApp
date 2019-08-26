package com.example.csapp;

public class UploadPackage {

    String packageName;
    String packagePrice;
    String packageDescription;

    public UploadPackage() {
    }

    public UploadPackage(String packageName, String packagePrice, String packageDescription) {
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageDescription = packageDescription;
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
