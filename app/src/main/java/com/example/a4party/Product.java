package com.example.a4party;

public class Product {
    private String product_name;
    private String product_price;
    private int product_img;

    public Product(String product_name, String product_price, int product_img) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public int getProduct_img() {
        return product_img;
    }

}
