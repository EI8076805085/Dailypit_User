package com.dailypit.dp.Model;

public class OrderStatusResponse {
    private String description;
    private String money;
    private String date;
    private String time;
    private String status;

    public OrderStatusResponse(String description, String money, String date, String time, String status) {
        this.description = description;
        this.money = money;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
