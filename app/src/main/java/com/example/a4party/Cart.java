package com.example.a4party;

public class Cart {

    private String product_name;
    private int quantity;
    private String size;
    private String price;
    private String color;

    public Cart(String product_name, int quantity, String size, String price, String color) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
        this.color = color;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
