package com.shoppers_era.Model;


import java.io.Serializable;


public  class Selection implements Serializable  {

    private String img_url;
    private Boolean select;
    private String img_id;
    private String price;
    private String actual_price;
    private String cat_name;
    private String count_all;
    private Boolean isLiked;
    private String image_name;

    public Selection(String img_url, Boolean select,String img_id,String price,String actual_price,String cat_name,String count_all,Boolean isLiked,String image_name) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
        this.price = price;
        this.actual_price = actual_price;
        this.cat_name = cat_name;
        this.isLiked = isLiked;
        this.count_all = count_all;
        this.image_name = image_name;
    }


    public Selection(String img_url, Boolean select,String img_id,String price,String actual_price,String cat_name,String count_all,Boolean isLiked) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
        this.price = price;
        this.actual_price = actual_price;
        this.cat_name = cat_name;
        this.isLiked = isLiked;
        this.count_all = count_all;
    }

    public Selection(String img_url, Boolean select,String img_id) {
        this.img_url = img_url;
        this.select = select;
        this.img_id = img_id;
    }


    public String getImg_url() {
        return img_url;
    }

    public Boolean getSelect() {
        return select;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getPrice() {
        return price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getCat_name() {
        return cat_name;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public String getCount_all() {
        return count_all;
    }

    public String getImage_name() {
        return image_name;
    }
}
