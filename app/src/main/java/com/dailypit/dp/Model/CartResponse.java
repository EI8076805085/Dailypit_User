package com.dailypit.dp.Model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartResponse {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id;
    private String s_id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String s_name;
    private String qty;
    private String s_fees;
    private String s_total;

    public CartResponse(String s_id,String qty, String s_name, String s_fees, String s_total) {
        this.s_id = s_id;
        this.qty = qty;
        this.s_name = s_name;
        this.s_fees = s_fees;
        this.s_total = s_total;

    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getS_fees() {
        return s_fees;
    }

    public void setS_fees(String s_fees) {
        this.s_fees = s_fees;
    }

    public String getS_total() {
        return s_total;
    }

    public void setS_total(String s_total) {
        this.s_total = s_total;
    }
}