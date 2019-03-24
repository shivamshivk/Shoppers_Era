package com.shoppers_era.Model;

public class Slider {

    private String img_url;
    private String base_activity;
    private String cat_id;

    public Slider(String img_url, String base_activity, String cat_id) {
        this.img_url = img_url;
        this.base_activity = base_activity;
        this.cat_id = cat_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getBase_activity() {
        return base_activity;
    }

    public String getCat_id() {
        return cat_id;
    }

}
