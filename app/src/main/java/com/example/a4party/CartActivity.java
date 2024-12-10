package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inicializamos el RecyclerView
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Usamos el Singleton para obtener la lista del carrito
        cartList = CartSingleton.getInstance().getCartList();

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
}
