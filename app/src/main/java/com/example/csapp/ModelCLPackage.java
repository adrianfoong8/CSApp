package com.example.csapp;

public class ModelCLPackage {

    String packageId, packagePhoto, packageName, packagePrice, packageDescription;

    public ModelCLPackage() {
    }

    public ModelCLPackage(String packageId, String packagePhoto, String packageName, String packagePrice, String packageDescription) {
        this.packageId = packageId;
        this.packagePhoto = packagePhoto;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageDescription = packageDescription;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackagePhoto() {
        return packagePhoto;
    }

    public void setPackagePhoto(String packagePhoto) {
        this.packagePhoto = packagePhoto;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }
}
