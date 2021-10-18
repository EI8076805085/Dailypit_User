package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckPackageResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("package_data")
    @Expose
    private List<CheckPackageResponseData> packageData = null;

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

    public List<CheckPackageResponseData> getPackageData() {
        return packageData;
    }

    public void setPackageData(List<CheckPackageResponseData> packageData) {
        this.packageData = packageData;
    }
}
