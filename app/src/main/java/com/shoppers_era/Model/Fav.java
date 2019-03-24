package com.shoppers_era.Model;

public class Fav {

    private String actual_price;
    private String selling_price;
    private String img_url;
    private Boolean select;
    private String cat_name;
    private String img_id;
    private String count_all;
    private Boolean isLiked;
    private String image_name;

    public Fav(String actual_price, String selling_price, String img_url,Boolean select,String img_id) {
        this.actual_price = actual_price;
        this.selling_price = selling_price;
        this.img_url = img_url;
        this.select= select;
        this.img_id = img_id;
    }

    public Fav(String cat_name,String actual_price, String selling_price, String img_url,Boolean select,String img_id,String count_all,Boolean isLiked,String image_name) {
        this.cat_name = cat_name;
        this.actual_price = actual_price;
        this.selling_price = selling_price;
        this.img_url = img_url;
        this.select= select;
        this.img_id = img_id;
        this.count_all = count_all;
        this.isLiked = isLiked;
        this.image_name = image_name;
    }

    public Fav(String cat_name, String actual_price,String selling_price ,String img_url, boolean select,String img_id) {
        this.cat_name = cat_name;
        this.actual_price = actual_price;
        this.selling_price = selling_price;
        this.img_url = img_url;
        this.select= select;
        this.img_id = img_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public String getImg_url() {
        return img_url;
    }

    public Boolean getSelect() {
        return select;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getImg_id() {
        return img_id;
    }

    public String getCount_all() {
        return count_all;
    }

    public Boolean getLiked() {
        return isLiked;
    }
}

