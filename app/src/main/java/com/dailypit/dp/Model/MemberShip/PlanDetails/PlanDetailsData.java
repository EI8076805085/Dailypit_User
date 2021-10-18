package com.dailypit.dp.Model.MemberShip.PlanDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanDetailsData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parent_name")
    @Expose
    private String parentName;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("category_data")
    @Expose
    private List<PlanDetailsSubData> categoryData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<PlanDetailsSubData> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<PlanDetailsSubData> categoryData) {
        this.categoryData = categoryData;
    }
}
