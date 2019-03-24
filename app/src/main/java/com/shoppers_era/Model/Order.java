package com.shoppers_era.Model;


public class Order {

    private String order_id;
    private String invoice_total;
    private String margin;
    private String product_id;
    private String img_url;
    private String tracking_id;

    public Order(String order_id, String invoice_total, String margin, String product_id, String img_url, String tracking_id) {
        this.order_id = order_id;
        this.invoice_total = invoice_total;
        this.margin = margin;
        this.product_id = product_id;
        this.img_url = img_url;
        this.tracking_id = tracking_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getInvoice_total() {
        return invoice_total;
    }

    public String getMargin() {
        return margin;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTracking_id() {
        return tracking_id;
    }
}
