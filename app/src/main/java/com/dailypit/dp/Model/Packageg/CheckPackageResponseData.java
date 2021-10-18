package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckPackageResponseData {
    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("package_name")
    @Expose
    private String package_name;
    @SerializedName("category_data")
    @Expose
    private List<PackageCategory> categoryData = null;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public List<PackageCategory> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<PackageCategory> categoryData) {
        this.categoryData = categoryData;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}
