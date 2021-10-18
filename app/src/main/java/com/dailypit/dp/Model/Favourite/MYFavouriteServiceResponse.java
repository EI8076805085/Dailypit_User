package com.dailypit.dp.Model.Favourite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MYFavouriteServiceResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<MyFavouriteServiceResponseData> data = null;

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

    public List<MyFavouriteServiceResponseData> getData() {
        return data;
    }

    public void setData(List<MyFavouriteServiceResponseData> data) {
        this.data = data;
    }
}
