package com.dailypit.dp.Model.OrderStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteOrderResponseData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("category_name")
    @Expose
    private String CategoryName;
    @SerializedName("child_category_name")
    @Expose
    private String childCategoryName;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("net_amount")
    @Expose
    private String netAmount;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("partner_id")
    @Expose
    private String partnerId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("adress_type")
    @Expose
    private String adressType;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("partner_name")
    @Expose
    private String partnerName;
    @SerializedName("partner_mobile")
    @Expose
    private String partnerMobile;
    @SerializedName("temprature")
    @Expose
    private String temprature;
    @SerializedName("partner_photo")
    @Expose
    private String partnerPhoto;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("time_slot")
    @Expose
    private String timeSlot;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getChildCategoryName() {
        return childCategoryName;
    }

    public void setChildCategoryName(String childCategoryName) {
        this.childCategoryName = childCategoryName;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAdressType() {
        return adressType;
    }

    public void setAdressType(String adressType) {
        this.adressType = adressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerMobile() {
        return partnerMobile;
    }

    public void setPartnerMobile(String partnerMobile) {
        this.partnerMobile = partnerMobile;
    }

    public String getTemprature() {
        return temprature;
    }

    public void setTemprature(String temprature) {
        this.temprature = temprature;
    }

    public String getPartnerPhoto() {
        return partnerPhoto;
    }

    public void setPartnerPhoto(String partnerPhoto) {
        this.partnerPhoto = partnerPhoto;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
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
}
