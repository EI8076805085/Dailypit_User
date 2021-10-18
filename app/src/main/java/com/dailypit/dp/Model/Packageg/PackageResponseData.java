package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageResponseData {
    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("package_image")
    @Expose
    private String packageImage;
    @SerializedName("taken_package")
    @Expose
    private String taken_package;

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

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getTaken_package() {
        return taken_package;
    }

    public void setTaken_package(String taken_package) {
        this.taken_package = taken_package;
    }
}
