package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LikesAdapter likesAdapter;
    private List<Likes> likesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        // Inicializamos la lista
        likesList = new ArrayList<>();
        recyclerView = findViewById(R.id.likesRecyclerView);

        // Configuramos RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configuramos el adaptador
        likesAdapter = new LikesAdapter(likesList);
        recyclerView.setAdapter(likesAdapter);

        // Llamamos al método para obtener los likes
        viewLikes();

        // Referencias a los botones
        ImageButton profile_btn = findViewById(R.id.profile_btn);
        ImageButton cart_btn = findViewById(R.id.cart_btn);
        ImageButton home_btn = findViewById(R.id.home_btn);
        ImageButton search_btn = findViewById(R.id.search_btn);

        home_btn.setOnClickListener(v -> startActivity(new Intent(LikesActivity.this, HomeActivity.class)));
        profile_btn.setOnClickListener(v -> startActivity(new Intent(LikesActivity.this, ProfileActivity.class)));
        cart_btn.setOnClickListener(v -> startActivity(new Intent(LikesActivity.this, CartActivity.class)));
        search_btn.setOnClickListener(v -> startActivity(new Intent(LikesActivity.this, SearchActivity.class)));
    }

    // Método para obtener los likes desde el servidor
    public void viewLikes() {
        String url = "http://10.0.2.2/4PARTY/likes.php?getLikes=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Soy el puto problema", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray likesItems = jsonResponse.getJSONArray("likes_items");

                    // Limpiamos la lista antes de agregar los nuevos elementos
                    likesList.clear();

                    for (int i = 0; i < likesItems.length(); i++) {
                        JSONObject likesItem = likesItems.getJSONObject(i);

                        String name = likesItem.getString("name");
                        String image_url = likesItem.getString("image_url");
                        Boolean liked = true;

                        // Agregamos el nuevo producto a la lista
                        Likes likedProduct = new Likes(name, image_url, liked);
                        likesList.add(likedProduct);
                    }

                    // Actualizamos el RecyclerView con la lista
                    updateLikesRecyclerView();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error in request: " + error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Método para actualizar el RecyclerView con los nuevos datos
    public void updateLikesRecyclerView() {
        // Notificamos al adaptador que la lista ha cambiado
        likesAdapter.notifyDataSetChanged();
    }
}
