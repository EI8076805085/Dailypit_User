package com.dailypit.dp.Model.MemberShip.CheckMemberShipPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckMemberShipPlan {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CheckMemberShipPlanData> data = null;

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

    public List<CheckMemberShipPlanData> getData() {
        return data;
    }

    public void setData(List<CheckMemberShipPlanData> data) {
        this.data = data;
    }
}
