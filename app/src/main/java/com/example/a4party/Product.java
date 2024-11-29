package com.example.a4party;
import java.util.List;

public class Product {
    private String name;
    private String price;
    private List<String> images;

    // Constructor
    public Product(String name, String price, List<String> images) {
        this.name = name;
        this.price = price;
        this.images = images;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
