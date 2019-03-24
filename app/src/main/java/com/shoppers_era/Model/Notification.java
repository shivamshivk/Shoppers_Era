package com.shoppers_era.Model;



public class Notification {

    private String img_url;
    private String title;
    private String desc;
    private String date;
    private String time;
    private String base;
    private String cat;

    public Notification(String img_url, String title, String desc, String date, String time, String base, String cat) {
        this.img_url = img_url;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.base = base;
        this.cat = cat;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getBase() {
        return base;
    }

    public String getCat() {
        return cat;
    }
}
