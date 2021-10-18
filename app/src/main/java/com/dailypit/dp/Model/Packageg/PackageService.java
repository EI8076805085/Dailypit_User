package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageService {
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("n_services")
    @Expose
    private String nServices;

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getnServices() {
        return nServices;
    }

    public void setnServices(String nServices) {
        this.nServices = nServices;
    }
}
