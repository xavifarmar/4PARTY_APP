package com.example.a4party;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionActivity extends AppCompatActivity {
    private TextView productName, productPrice;
    private ImageView productImage;
    private LinearLayout variationContainer;
    private Button addToCart;
    private RadioGroup sizeGroup;
    private RadioButton sizeS, sizeM, sizeL;
    private String name, price, color_name, imageUrl, selectedSize;
    private Boolean isProductInCart = false;
    private int colorInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        // Inicializamos las vistas
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);
        variationContainer = findViewById(R.id.variation_container);
        addToCart = findViewById(R.id.addToCartBtn);
        sizeGroup = findViewById(R.id.size_options);
        sizeS = findViewById(R.id.size_s);
        sizeM = findViewById(R.id.size_m);
        sizeL = findViewById(R.id.size_l);
        ImageButton likeBtn = findViewById(R.id.likeBtn);

        // Recuperamos los datos del Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("product_name");
        price = intent.getStringExtra("product_price");
        String image = intent.getStringExtra("product_image");
        color_name = intent.getStringExtra("product_color");

        // Establecemos los valores de nombre y precio
        productName.setText(name);
        productPrice.setText(price);

        // Verificamos si la URL de la imagen está presente
        if (image != null && !image.isEmpty()) {
            Picasso.get().load(image).error(R.drawable.profile_icon).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.profile_icon);
        }

        // Llamamos al método para obtener los colores y variaciones
        getColours(name);

        // Botón para agregar el producto al carrito
        addToCart.setOnClickListener(view -> {
            if (!isProductInCart) {
                if (selectedSize == null || selectedSize.isEmpty()) {
                    Toast.makeText(ProductDescriptionActivity.this, "Por favor seleccione una talla.", Toast.LENGTH_LONG).show();
                } else if (color_name == null || color_name.isEmpty()) {
                    Toast.makeText(ProductDescriptionActivity.this, "Por favor seleccione un color.", Toast.LENGTH_LONG).show();
                } else {
                    addToCart();
                    isProductInCart = true;
                    addToCart.setText("Eliminar del carrito");
                }
            } else {
                // Lógica para eliminar del carrito
                isProductInCart = false;
                addToCart.setText("Añadir al carrito");
            }
        });

        // Botón de "Atrás"
        Button backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
            startActivity(intent1);
        });

        // Listener para tamaño seleccionado
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.size_s) {
                selectedSize = "S";
            } else if (checkedId == R.id.size_m) {
                selectedSize = "M";
            } else if (checkedId == R.id.size_l) {
                selectedSize = "L";
            }
        });
    }

    // Método para obtener las variaciones de colores (u otras variaciones del producto)
    private void getColours(String productName) {
        String url = "http://10.0.2.2/4PARTY/getVariations.php?product_name=" + Uri.encode(productName);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("API Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("product")) {
                            JSONObject product = jsonResponse.getJSONObject("product");
                            name = product.getString("name");
                            price = product.getString("price");
                            imageUrl = product.getString("image_url");

                            // Establecer los datos del producto en la UI
                            productPrice.setText(price);
                            Picasso.get().load(imageUrl).into(productImage);

                            // Procesar las variaciones si existen
                            JSONArray variations = jsonResponse.getJSONArray("variations");
                            if (variations.length() > 1) {
                                // Si hay más de un color, mostrar las variaciones
                                for (int i = 0; i < variations.length(); i++) {
                                    JSONObject variation = variations.getJSONObject(i);
                                    String color = variation.getString("color");
                                    String variationImage = variation.getString("image_url");

                                    colorInt = getColor(color);

                                    // Crear botón de color dinámicamente
                                    Button colorButton = new Button(ProductDescriptionActivity.this);
                                    colorButton.setBackgroundResource(R.drawable.button_template);  // Aplica la plantilla de fondo

                                    // Modificar dinámicamente el color de fondo del botón
                                    GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
                                    drawable.setColor(colorInt);  // Cambiar el color del fondo usando colorInt (el color dinámico)

                                    // Configuración de layout del botón
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.setMargins(10, 10, 10, 10);  // Márgenes
                                    colorButton.setLayoutParams(params);

                                    // Establecer un listener para cambiar la imagen cuando se seleccione una variación
                                    colorButton.setOnClickListener(view -> {
                                        color_name = color;
                                        Picasso.get().load(variationImage).into(productImage);
                                    });

                                    // Añadir el botón al contenedor de variaciones
                                    variationContainer.addView(colorButton);
                                }
                            } else {
                                // Si solo hay un color, ocultar el contenedor de variaciones
                                variationContainer.setVisibility(View.GONE);
                            }
                        } else {
                            Log.e("Product", "No se encontraron productos");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Error", error.toString()));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    // Método auxiliar para obtener un color a partir del nombre
    public int getColor(String color) {
        switch (color.toLowerCase()) {
            case "1": return ContextCompat.getColor(this, R.color.color_rojo);
            case "2": return ContextCompat.getColor(this, R.color.color_azul_marino);
            case "3": return ContextCompat.getColor(this, R.color.color_negro);
            case "4": return ContextCompat.getColor(this, R.color.color_blanco);
            case "5": return ContextCompat.getColor(this, R.color.color_dorado);
            case "6": return ContextCompat.getColor(this, R.color.color_plateado);
            case "7": return ContextCompat.getColor(this, R.color.color_verde);
            case "8": return ContextCompat.getColor(this, R.color.color_rosa);
            case "9": return ContextCompat.getColor(this, R.color.color_morado);
            case "10": return ContextCompat.getColor(this, R.color.color_gris);
            case "11": return ContextCompat.getColor(this, R.color.color_amarillo);
            case "12": return ContextCompat.getColor(this, R.color.color_naranja);
            case "13": return ContextCompat.getColor(this, R.color.color_beige);
            case "14": return ContextCompat.getColor(this, R.color.color_vino);
            case "15": return ContextCompat.getColor(this, R.color.color_turquesa);
            case "16": return ContextCompat.getColor(this, R.color.color_marron);
            default: return ContextCompat.getColor(this, android.R.color.darker_gray);
        }
    }

    public void addToCart() {
        // URL del archivo PHP que maneja la solicitud
        String url = "http://10.0.2.2/4PARTY/addToCart.php";

        // Crear la solicitud POST con StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("API Response", response);  // Imprime la respuesta para depuración
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if ("success".equals(status)) {
                            Log.d("AddToCart", "Product added: " + message);
                            addToCart.setText("Eliminar del carrito");
                        } else {
                            Log.e("AddToCart", "Error: " + message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("AddToCart", "Request failed: " + error.toString())) {

            @Override
            protected java.util.Map<String, String> getParams() {
                // Crear los parámetros que se enviarán al servidor
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("product_name", name);
                params.put("color_id", color_name);
                params.put("size", selectedSize);
                return params;
            }
        };

        // Crear una cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(this);
        // Añadir la solicitud a la cola
        queue.add(stringRequest);
    }
}
