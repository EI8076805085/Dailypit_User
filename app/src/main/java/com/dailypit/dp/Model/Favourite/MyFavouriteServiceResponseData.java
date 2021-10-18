package com.dailypit.dp.Model.Favourite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyFavouriteServiceResponseData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("subcat_id")
    @Expose
    private String subcatId;
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("descriptions")
    @Expose
    private String descriptions;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("multi_coupon")
    @Expose
    private String multiCoupon;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("child_data")
    @Expose
    private List<FavouriteListdata> childData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMultiCoupon() {
        return multiCoupon;
    }

    public void setMultiCoupon(String multiCoupon) {
        this.multiCoupon = multiCoupon;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public List<FavouriteListdata> getChildData() {
        return childData;
    }

    public void setChildData(List<FavouriteListdata> childData) {
        this.childData = childData;
    }
}
