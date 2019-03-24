package com.shoppers_era.Model;

import java.util.ArrayList;

public class Home {

    private String cat_name;
    private String cat_no;
    private String cat_desc;
    private String tag_name;
    private String tag_name_2;
    private String tag_color;
    private String tag_color_2;
    private String cat_count;
    private ArrayList<Selection> images;

    public Home(String cat_name, String cat_no,String cat_count,String tag_name,String tag_name_2,String tag_color,String tag_color_2, String cat_desc,ArrayList<Selection> images) {
        this.cat_name = cat_name;
        this.cat_no = cat_no;
        this.cat_count = cat_count;
        this.tag_name = tag_name;
        this.tag_name_2 = tag_name_2;
        this.tag_color = tag_color;
        this.tag_color_2 = tag_color_2;
        this.cat_desc = cat_desc;
        this.images = images;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getCat_no() {
        return cat_no;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public ArrayList<Selection> getImages() {
        return images;
    }

    public String getTag_name() {
        return tag_name;
    }

    public String getTag_color() {
        return tag_color;
    }

    public String getCat_count() {
        return cat_count;
    }

    public String getTag_name_2() {
        return tag_name_2;
    }

    public String getTag_color_2() {
        return tag_color_2;
    }
}

