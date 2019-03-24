package com.shoppers_era.Model;

import java.io.Serializable;

public class Diary implements Serializable{

    private String img_url;
    private String cat_name;
    private String desc;
    private String content1;
    private String content2;
    private Boolean select;

    public Diary(String img_url, String cat_name, String desc, String content1, String content2,Boolean select) {
        this.img_url = img_url;
        this.cat_name = cat_name;
        this.desc = desc;
        this.content1 = content1;
        this.content2 = content2;
        this.select = select;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getDesc() {
        return desc;
    }

    public String getContent1() {
        return content1;
    }

    public String getContent2() {
        return content2;
    }


    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }
}
