package com.shoppers_era.Model;

import java.io.Serializable;


public class Categories implements Serializable{

    private String cat_name;
    private String cat_no;
    private String cat_desc;

    public Categories(String cat_name, String cat_no, String cat_desc) {
        this.cat_name = cat_name;
        this.cat_no = cat_no;
        this.cat_desc = cat_desc;
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
}
