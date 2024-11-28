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

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productoAdapter;
    private List<Product> productoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creamos una lista de productos (puedes obtenerla de una base de datos o API)
        productoList = new ArrayList<>();
        productoList.add(new Product("Producto 1", "$100", R.drawable.cart_red_icon));
        productoList.add(new Product("Producto 2", "$200", R.drawable.cart_red_icon));
        productoList.add(new Product("Producto 3", "$300", R.drawable.cart_red_icon));
        productoList.add(new Product("Producto 1", "$100", R.drawable.cart_red_icon));
        productoList.add(new Product("Producto 2", "$200", R.drawable.cart_red_icon));
        productoList.add(new Product("Producto 3", "$300", R.drawable.cart_red_icon));

        // Creamos el adaptador y lo asignamos al RecyclerView
        productoAdapter = new ProductAdapter(productoList);
        recyclerView.setAdapter(productoAdapter);

        // Referencia a los botones
        ImageButton likes_btn = findViewById(R.id.heart_btn);
        ImageButton profile_btn = findViewById(R.id.profile_btn);
        ImageButton cart_btn = findViewById(R.id.cart_btn);
        ImageButton search_btn = findViewById(R.id.search_btn);
        ImageButton homeBtn = findViewById(R.id.home_btn);

        homeBtn.setSelected(true);  // Selecciona el botón de inicio
        search_btn.setSelected(false);  // Desactiva el botón de búsqueda
        cart_btn.setSelected(false);  // Y así con los demás botones
        likes_btn.setSelected(false);
        profile_btn.setSelected(false);


        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el OnClickListener para el botón likes_btn
        likes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(HomeActivity.this, LikesActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    public void navigateToProduct(View v){
        Intent intent = new Intent(HomeActivity.this, ProductDescriptionActivity.class);
        startActivity(intent);
    }
}
