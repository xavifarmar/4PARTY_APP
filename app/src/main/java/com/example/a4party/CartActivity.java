package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangedListener{

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Iniciar la lista de productos del carrito
        cartList = new ArrayList<>();

        //Configurar el recycler View
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Crea el adaptador y asigna al RecyclerView
        cartAdapter = new CartAdapter(cartList, this);
        recyclerView.setAdapter(cartAdapter);

        // Añadir productos al carrito de ejemplo
        addProductToCart("Producto 1", "M", "$20", "Rojo");
        addProductToCart("Producto 2", "L", "$15", "Azul");

        // Referencia a los botones
        ImageButton likes_btn = findViewById(R.id.heart_btn);
        ImageButton profile_btn = findViewById(R.id.profile_btn);
        ImageButton home_btn = findViewById(R.id.home_btn);
        ImageButton search_btn = findViewById(R.id.search_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el OnClickListener para el botón likes_btn
        likes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(CartActivity.this, LikesActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(CartActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }



    @Override
    public void onQuantityChanged(Cart cart) {
        // Aquí puedes manejar los cambios de cantidad, por ejemplo, actualizar la base de datos
        // o la lista que contiene el carrito de compras.
        // Si quisieras actualizar la vista en general o realizar algún otro cambio, este es el lugar.
    }
    private void addProductToCart(String productName, String size, String price, String color) {
        // Añadir producto al carrito
        cartList.add(new Cart(productName, 1, size, price, color));
    }
}
