package com.example.a4party;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ProductDescriptionActivity extends AppCompatActivity{
    private TextView productName, productPrice;
    private ImageView productImage;
    private LinearLayout variationContainer;
    private Button addToCart;
    private String name, price, color, imageUrl;
    private Boolean isProductInCart = false;

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
        addToCart = findViewById(R.id.addToCartBtn);

        // Recuperamos los datos del Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("product_name");
        price = intent.getStringExtra("product_price");
        String image = intent.getStringExtra("product_image");

        // Establecemos los valores de nombre y precio
        productName.setText(name);
        productPrice.setText(price);

        // Verificamos si la URL de la imagen está presente
        if (image != null && !image.isEmpty()) {
            // Cargar la imagen con Picasso
            Picasso.get()
                    .load(image)  // La URL de la imagen pasada desde el intent
                    .into(productImage);  // Establece la imagen en el ImageView
        } else {
            // Si no hay imagen, ponemos una imagen predeterminada
            productImage.setImageResource(R.drawable.profile_icon);
        }

        // Llamamos al método para obtener los colores y variaciones
        getColours(name);

        // Botón para agregar el producto al carrito
        addToCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!isProductInCart){
                    addToCart.setText("Eliminar del carrito");
                    addToCartToSingleton(name, price, color, imageUrl);  // Usamos el Singleton para añadir el producto
                    isProductInCart = true;
                } else {
                    addToCart.setText("Añadir al carrito");
                    removeFromCart();  // Aquí implementaremos la lógica para eliminar del carrito si es necesario
                    isProductInCart = false;
                }
            }
        });

        // Botón de "Atrás" para regresar a la actividad anterior
        Button backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para obtener las variaciones de colores (u otras variaciones del producto)
    private void getColours(String productName) {
        String url = "http://10.0.2.2/4PARTY/getVariations.php?product_name=" + Uri.encode(productName);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                for (int i = 0; i < variations.length(); i++) {
                                    JSONObject variation = variations.getJSONObject(i);
                                    String color = variation.getString("color");
                                    String variationImage = variation.getString("image_url");

                                    int colorInt = getColor(color);

                                    // Primero, carga la plantilla del botón
                                    Button colorButton = new Button(ProductDescriptionActivity.this);
                                    colorButton.setBackgroundResource(R.drawable.button_template);  // Aplica la plantilla de fondo

// Modificar dinámicamente el color de fondo del botón
                                    GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
                                    drawable.setColor(colorInt);  // Cambiar el color del fondo usando colorInt (el color dinámico)

                                    // Establecer un tamaño específico para los botones
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,  // Ancho
                                            LinearLayout.LayoutParams.WRAP_CONTENT   // Alto
                                    );
                                    params.setMargins(10, 10, 10, 10);  // Márgenes
                                    colorButton.setLayoutParams(params);

                                    // Establecer un listener para cambiar la imagen cuando se seleccione una variación
                                    colorButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Picasso.get().load(variationImage).into(productImage);
                                        }
                                    });

                                    // Añadir el botón al contenedor de variaciones
                                    variationContainer.addView(colorButton);
                                }
                            } else {
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

    // Método para añadir el producto al carrito utilizando el Singleton
    public void addToCartToSingleton(String name, String price, String color, String imageUrl) {
        Cart cartProduct = new Cart(name, 1, "M", price, color, imageUrl);
        CartSingleton.getInstance().addProductToCart(cartProduct);  // Usamos el Singleton para añadir al carrito
    }

    // Método para eliminar el producto del carrito
    public void removeFromCart() {
        Cart cartProduct = new Cart(name, 1, "M", price, color, imageUrl);
        CartSingleton.getInstance().removeProductFromCart(cartProduct);  // Eliminar producto usando Singleton
    }
}
