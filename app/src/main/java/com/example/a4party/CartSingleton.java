package com.example.a4party;

import java.util.ArrayList;
import java.util.List;

public class CartSingleton {

    private static CartSingleton instance;
    private List<Cart> cartList;

    private CartSingleton() {
        cartList = new ArrayList<>();
    }

    public static synchronized CartSingleton getInstance() {
        if (instance == null) {
            instance = new CartSingleton();
        }
        return instance;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void addProductToCart(Cart product) {
        cartList.add(product);
    }

    public void removeProductFromCart(Cart product) {
        cartList.remove(product);
    }

    public void clearCart() {
        cartList.clear();
    }
}
