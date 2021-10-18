package com.dailypit.dp.Model.Packageg;

public class PackageServicesList {
    private int img;
    private String content;
    private String number;

    public PackageServicesList(int img, String content, String number) {
        this.img = img;
        this.content = content;
        this.number = number;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
