package com.dailypit.dp.Model;

public class AddressResponse {

    private String txtDefault;
    private String address;
    private int imgId;

    public AddressResponse(String txtDefault, String address, int imgId) {
        this.txtDefault = txtDefault;
        this.address = address;
        this.imgId = imgId;
    }

    public String getTxtDefault() {
        return txtDefault;
    }

    public void setTxtDefault(String txtDefault) {
        this.txtDefault = txtDefault;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
