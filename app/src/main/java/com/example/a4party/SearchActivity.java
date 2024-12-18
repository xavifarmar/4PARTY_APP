package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter searchAdapter;
    private List<Product> productSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        // Inicializar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.searchRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // Dos columnas
        recyclerView.setLayoutManager(gridLayoutManager);

        // Añadir espaciado entre los ítems
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 12, true)); // 16dp de espaciado horizontal y vertical


        // Inicializar la lista de productos
        productSearchList = new ArrayList<>();

        // Configurar el adaptador y asignarlo al RecyclerView
        searchAdapter = new ProductAdapter(this, productSearchList);
        recyclerView.setAdapter(searchAdapter);

        // Llamar a la función para obtener los productos
        getProductsFiltered();



        // Referencia a los botones
        ImageButton likes_btn = findViewById(R.id.heart_btn);
        ImageButton cart_btn = findViewById(R.id.cart_btn);
        ImageButton home_btn = findViewById(R.id.home_btn);
        ImageButton profile_btn = findViewById(R.id.profile_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el OnClickListener para el botón likes_btn
        likes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(SearchActivity.this, LikesActivity.class);
                startActivity(intent);
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    // Método para obtener los productos de la base de datos

    private void getProductsFiltered() {

        // URL de tu archivo PHP que devuelve los productos en formato JSON
        String url = "http://10.0.2.2/4PARTY/searchFilters.php"; // Cambia por la URL de tu servidor

        Log.d("API Request", "Haciendo solicitud a: " + url); // Log para ver la URL

        // Crear la solicitud de tipo StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response); // Log para mostrar la respuesta

                        try {
                            // Convertir la respuesta a un JSONArray
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("JSON Array Size", String.valueOf(jsonArray.length())); // Ver el tamaño del array

                            // Limpiar la lista de productos
                            productSearchList.clear();

                            // Recorrer el JSON y agregar productos a la lista
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productObject = jsonArray.getJSONObject(i);
                                String name = productObject.getString("name");
                                String price = productObject.getString("price");
                                String image = productObject.getString("image_url");
                                String color = productObject.getString("color_id");
                                int liked = productObject.optInt("liked");

                                // Crear un objeto Product y agregarlo a la lista
                                Product product = new Product(name, price, image, color, liked);
                                productSearchList.add(product);
                            }

                            // Notificar al adaptador que los datos han cambiado
                            searchAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SearchActivity.this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString()); // Log para errores de Volley
                        Toast.makeText(SearchActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }
}