package com.dailypit.dp.Model.Packageg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponseData {

    @SerializedName("subcat_id")
    @Expose
    private String subcatId;
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("subcat_image")
    @Expose
    private String subcatImage;
    @SerializedName("child_id")
    @Expose
    private String childId;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("n_services")
    @Expose
    private String nServices;
    @SerializedName("fees")
    @Expose
    private String fees;

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getSubcatImage() {
        return subcatImage;
    }

    public void setSubcatImage(String subcatImage) {
        this.subcatImage = subcatImage;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }
}
