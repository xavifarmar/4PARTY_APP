package com.example.a4party;

public class Likes {

    private String product_name;
    private String image_url;
    private boolean isLiked;

    public Likes(String product_name, String image_url, boolean isLiked) {
        this.product_name = product_name;
        this.image_url = image_url;
        this.isLiked = isLiked;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

}
