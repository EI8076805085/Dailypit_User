package com.dailypit.dp.Model.MemberShip;

public class MembershipResponse {
    private String name ;

    public MembershipResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
