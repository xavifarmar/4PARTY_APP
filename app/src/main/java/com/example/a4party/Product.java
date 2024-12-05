package com.example.a4party;
import java.util.List;

public class Product {
    private String name;
    private String price;
    private String image;
    private String is_primary;


    // Constructor
    public Product(String name, String price, String images) {
        this.name = name;
        this.price = price;
        this.image = images;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
