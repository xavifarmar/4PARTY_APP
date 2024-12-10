package com.example.a4party;

import android.os.Parcel;
import android.os.Parcelable;

public class Cart implements Parcelable{

    private String product_name;
    private int quantity;
    private String size;
    private String price;
    private String color;
    private String image_url;

    public Cart(String product_name, int quantity, String size, String price, String color, String image_url) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
        this.color = color;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    // Método para escribir los datos de la clase al Parcel
    protected Cart(Parcel in) {
        product_name = in.readString();
        quantity = in.readInt();
        size = in.readString();
        price = in.readString();
        color = in.readString();
    }

    // Método para escribir los datos al Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_name);
        dest.writeInt(quantity);
        dest.writeString(size);
        dest.writeString(price);
        dest.writeString(color);
        dest.writeString(image_url);
    }

    // Método necesario para crear instancias de la clase desde un Parcel
    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    // Método necesario para describir el tipo de objetos
    @Override
    public int describeContents() {
        return 0;
    }

}
