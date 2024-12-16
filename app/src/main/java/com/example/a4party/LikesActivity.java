package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_likes);

        // Referencia a los botones
        ImageButton profile_btn = findViewById(R.id.profile_btn);
        ImageButton cart_btn = findViewById(R.id.cart_btn);
        ImageButton home_btn = findViewById(R.id.home_btn);
        ImageButton search_btn = findViewById(R.id.search_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(LikesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el OnClickListener para el botón likes_btn
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad LikesActivity
                Intent intent = new Intent(LikesActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(LikesActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar a la actividad
                Intent intent = new Intent(LikesActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }
    public void viewLikes(){
        String url = "http://10.0.2.2/4PARTY/likes.php?getLikes";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray likesItems = jsonResponse.getJSONArray("likes_items");

                    List<Likes> likesList= new ArrayList<>();

                    for (int i = 0; i > likesItems.length(); i++){
                        JSONObject likesItem = likesItem.getJSONObject(i);
                    }

                }
            }
        }
    }
}
