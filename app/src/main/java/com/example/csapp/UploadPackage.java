package com.example.csapp;

public class UploadPackage {

    String packageId;
    String packagePhoto;
    String packageUserTestimony;
    String packageName;
    String packagePrice;
    String packageDescription;
    String packageDuration;
    String packageService;
    String packageAvailableStartDate;
    String packageAvailableEndDate;

    public UploadPackage() {
    }

    public UploadPackage(String packageId, String packagePhoto, String packageUserTestimony, String packageName, String packagePrice, String packageDescription, String packageDuration, String packageService, String packageAvailableStartDate, String packageAvailableEndDate) {
        this.packageId = packageId;
        this.packagePhoto = packagePhoto;
        this.packageUserTestimony = packageUserTestimony;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageDescription = packageDescription;
        this.packageDuration = packageDuration;
        this.packageService = packageService;
        this.packageAvailableStartDate = packageAvailableStartDate;
        this.packageAvailableEndDate = packageAvailableEndDate;
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

    public String getPackageUserTestimony() {
        return packageUserTestimony;
    }

    public void setPackageUserTestimony(String packageUserTestimony) {
        this.packageUserTestimony = packageUserTestimony;
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

    public String getPackageDuration() {
        return packageDuration;
    }

    public void setPackageDuration(String packageDuration) {
        this.packageDuration = packageDuration;
    }

    public String getPackageService() {
        return packageService;
    }

    public void setPackageService(String packageService) {
        this.packageService = packageService;
    }

    public String getPackageAvailableStartDate() {
        return packageAvailableStartDate;
    }

    public void setPackageAvailableStartDate(String packageAvailableStartDate) {
        this.packageAvailableStartDate = packageAvailableStartDate;
    }

    public String getPackageAvailableEndDate() {
        return packageAvailableEndDate;
    }

    public void setPackageAvailableEndDate(String packageAvailableEndDate) {
        this.packageAvailableEndDate = packageAvailableEndDate;
    }
}
