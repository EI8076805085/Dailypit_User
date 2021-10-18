package com.dailypit.dp.Model.AddOnService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddOnServiceResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<AddOnServiceResponseData> data = null;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("net_amount")
    @Expose
    private String netAmount;
    @SerializedName("addon_payment_mode")
    @Expose
    private String addonpaymentmode;
    @SerializedName("addon_payment_status")
    @Expose
    private String addonpaymentstatus;


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

    public List<AddOnServiceResponseData> getData() {
        return data;
    }

    public void setData(List<AddOnServiceResponseData> data) {
        this.data = data;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }


    public String getAddonpaymentmode() {
        return addonpaymentmode;
    }

    public void setAddonpaymentmode(String addonpaymentmode) {
        this.addonpaymentmode = addonpaymentmode;
    }

    public String getAddonpaymentstatus() {
        return addonpaymentstatus;
    }

    public void setAddonpaymentstatus(String addonpaymentstatus) {
        this.addonpaymentstatus = addonpaymentstatus;
    }
}
