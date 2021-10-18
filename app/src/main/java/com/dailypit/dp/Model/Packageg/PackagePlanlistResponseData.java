package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackagePlanlistResponseData {
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_icon")
    @Expose
    private String categoryIcon;
    @SerializedName("services")
    @Expose
    private List<PackageService> services = null;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public List<PackageService> getServices() {
        return services;
    }

    public void setServices(List<PackageService> services) {
        this.services = services;
    }

}
