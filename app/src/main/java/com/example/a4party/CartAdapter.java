package com.example.a4party;

import android.content.Context;

import java.util.List;

public class CartAdapter  {
    private Context context;
    private List<Cart> cartList;

    public CartAdapter (Context context, List<Cart> cartList){
        this.context = context;
        this.cartList = cartList;
    }


}
