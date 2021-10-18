package com.dailypit.dp.Model.MemberShip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberShipPlanListdata {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("membership_amount")
    @Expose
    private String membershipAmount;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("recommendation")
    @Expose
    private String recommendation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembershipAmount() {
        return membershipAmount;
    }

    public void setMembershipAmount(String membershipAmount) {
        this.membershipAmount = membershipAmount;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
