package com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckMemberShipPlanData {
    @SerializedName("plan_id")
    @Expose
    private String planId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("activation_date")
    @Expose
    private String activationDate;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("validity")
    @Expose
    private String validity;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
