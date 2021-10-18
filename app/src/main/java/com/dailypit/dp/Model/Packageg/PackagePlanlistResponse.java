package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackagePlanlistResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("package_amount")
    @Expose
    private String packageAmount;
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @SerializedName("key_point")
    @Expose
    private String keyPoint;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("service_details")
    @Expose
    private List<PackagePlanlistResponseData> serviceDetails = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public List<PackagePlanlistResponseData> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<PackagePlanlistResponseData> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

}
