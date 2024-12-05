package com.example.a4party;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.a4party.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDescriptionActivity extends AppCompatActivity {

    private TextView productName, productPrice;
    private ImageView productImage;
    private LinearLayout variationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        // Inicializamos las vistas
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);
        variationContainer = findViewById(R.id.variation_container);  // Contenedor para las variaciones
        ImageButton likeBtn = findViewById(R.id.likeBtn);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(950909)); // Color del like
            }
        });

        // Recuperamos los datos del Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String price = intent.getStringExtra("product_price");
        String image = intent.getStringExtra("product_image");

        // Establecemos los valores de nombre y precio
        productName.setText(name);
        productPrice.setText(price);

        // Botón para volver a la pantalla anterior
        Button backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Verificamos si la URL de la imagen está presente
        if (image != null && !image.isEmpty()) {
            // Cargar la imagen con Picasso
            Picasso.get()
                    .load(image)  // La URL de la imagen pasada desde el intent
                    .into(productImage);  // Establece la imagen en el ImageView
        } else {
            // Si no hay imagen, ponemos una imagen predeterminada
            productImage.setImageResource(R.drawable.profile_icon); // Imagen predeterminada
        }

        // Llamamos al método para obtener los colores y variaciones
        getColours(name);
    }

    // Método para obtener las variaciones de colores (u otras variaciones del producto)
    private void getColours(String productName) {
        String url = "http://10.0.2.2/4PARTY/getVariations.php?product_name=" + productName;  // Asegúrate de pasar el nombre correcto

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("product")) {
                                JSONObject product = jsonResponse.getJSONObject("product");
                                String name = product.getString("name");
                                String price = product.getString("price");
                                String imageUrl = product.getString("image_url");

                                // Establecer los datos del producto en la UI
                                productPrice.setText(price);
                                Picasso.get().load(imageUrl).into(productImage);  // Cargar imagen

                                // Procesar las variaciones si existen
                                JSONArray variations = jsonResponse.getJSONArray("variations");
                                for (int i = 0; i < variations.length(); i++) {
                                    JSONObject variation = variations.getJSONObject(i);
                                    String color = variation.getString("color");
                                    String variationImage = variation.getString("image_url");

                                    // Crear un botón para cada variación
                                    Button colorButton = new Button(ProductDescriptionActivity.this);
                                    colorButton.setText(color);  // Establecer el nombre del color como texto
                                    colorButton.setBackgroundColor(getColorFromName(color));  // Establecer un color de fondo basado en el nombre (si es aplicable)

                                    // Establecer un listener para que cambie la imagen cuando se seleccione una variación
                                    colorButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // Cambiar la imagen al hacer clic en el botón
                                            Picasso.get().load(variationImage).into(productImage);
                                        }
                                    });

                                    // Añadir el botón al contenedor de variaciones
                                    variationContainer.addView(colorButton);
                                }
                            } else {
                                // Manejar el caso cuando no se encuentren resultados
                                Log.e("Product", "No se encontraron productos");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    // Método auxiliar para obtener un color a partir del nombre (puedes personalizar esto)
    private int getColorFromName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "rojo":
                return getResources().getColor(android.R.color.holo_red_light);
            case "azul":
                return getResources().getColor(android.R.color.holo_blue_light);
            case "verde":
                return getResources().getColor(android.R.color.holo_green_light);
            // Añadir más colores según sea necesario
            default:
                return getResources().getColor(android.R.color.darker_gray);  // Color por defecto
        }
    }
}
