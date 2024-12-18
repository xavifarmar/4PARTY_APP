package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        viewCart();
        getTotalPriceCart();

        // Inicializamos el RecyclerView
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Configuramos el adaptador con la lista de productos
        cartAdapter = new CartAdapter(cartList);
        recyclerView.setAdapter(cartAdapter);

        // Referencia a los botones
        ImageButton likes_btn = findViewById(R.id.heart_btn);
        ImageButton profile_btn = findViewById(R.id.profile_btn);
        ImageButton home_btn = findViewById(R.id.home_btn);
        ImageButton search_btn = findViewById(R.id.search_btn);

        // Configuración de botones
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        likes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, LikesActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    public void viewCart(){

        String url = "http://10.0.2.2/4PARTY/viewCart.php?getCartItems=true" ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CartData", response);  // Ver respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray cartItems = jsonResponse.getJSONArray("cart_items");

                            List<Cart> cartList = new ArrayList<>();

                            // Procesar cada item del carrito
                            for (int i = 0; i < cartItems.length(); i++) {
                                JSONObject cartItem = cartItems.getJSONObject(i);

                                String name = cartItem.getString("product_name");
                                String price = cartItem.getString("price");


                                String imageUrl = cartItem.getString("image_url");
                                String size = cartItem.getString("size");
                                int quantity = cartItem.getInt("quantity");
                                String color = cartItem.getString("color");


                                //ESTRUCTURA DEL CONSTRUCTOR
                                //(String product_name, int quantity, String size, String price, String color, String image_url)

                                // Crear un objeto CartItem con los datos obtenidos
                                Cart cartProduct = new Cart(name, quantity ,size, price, color, imageUrl);

                                // Agregar el producto al carrito
                                cartList.add(cartProduct);
                            }

                            // Llamamos a una función para actualizar el RecyclerView con los datos
                            updateCartRecyclerView(cartList);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error in request: " + error.toString());
                    }
                });

        // Crear una cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void updateCartRecyclerView(List<Cart> cartList) {
        // Inicializamos el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creamos el adaptador con la lista de productos
        CartAdapter cartAdapter = new CartAdapter(cartList);
        recyclerView.setAdapter(cartAdapter);
    }

    public void getTotalPriceCart(){
        String url = "http://10.0.2.2/4PARTY/viewCart.php?getTotalPriceCart=true";
        TextView totalPriceTxt;
        totalPriceTxt = findViewById(R.id.totalPrice);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    String totalPrice = jsonResponse.getString("total_price");

                    totalPriceTxt.setText(totalPrice + "€");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", "Error in request: " + error.toString());
                }
            });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        }
}

