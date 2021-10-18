package com.dailypit.dp.Model;

public class ApplyCouponResponse {
    private String couponName;
    private String couponDiscription;

    public ApplyCouponResponse(String couponName, String couponDiscription) {
        this.couponName = couponName;
        this.couponDiscription = couponDiscription;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponDiscription() {
        return couponDiscription;
    }

    public void setCouponDiscription(String couponDiscription) {
        this.couponDiscription = couponDiscription;
    }
}
