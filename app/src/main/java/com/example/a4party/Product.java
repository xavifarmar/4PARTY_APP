package com.example.a4party;

public class Product {
    private String name;
    private String price;
    private String image;
    private String is_primary;
    private String color;
    private int liked;


    // Constructor
    public Product(String name, String price, String images, String color, int liked) {
        this.name = name;
        this.price = price;
        this.image = images;
        this.color = color;
        this.liked = liked;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }
}
