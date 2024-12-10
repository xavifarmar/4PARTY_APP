package com.example.a4party;
import android.content.Intent;
import android.content.res.ColorStateList;
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

public class ProductDescriptionActivity extends AppCompatActivity {
    private TextView productName, productPrice;
    private ImageView productImage;
    private LinearLayout variationContainer;
    private Button addToCart;
    private List<Cart> cartList;
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

        //Iniciar el carrito
        cartList = new ArrayList<>();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(950909)); // Color del like
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!isProductInCart){
                    addToCart.setText("Eliminar del carrito");
                    addToCart(name, price, color);
                    isProductInCart = true;
                }else {
                    addToCart.setText("Añadir al carrito");
                    removeFromCart(name, color);
                    isProductInCart = false;
                }

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
            productImage.setImageResource(R.drawable.profile_icon);
        }

        // Llamamos al método para obtener los colores y variaciones
        getColours(name);
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


                                    // Crear un botón con la plantilla de fondo
                                    Button colorButton = new Button(ProductDescriptionActivity.this);

                                    colorButton.setBackgroundResource(R.drawable.button_template);  // Aplicar la plantilla de botón
                                    colorButton.setBackgroundColor(colorInt);

                                    // Establecer un tamaño específico para los botones
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,  // Ancho
                                            LinearLayout.LayoutParams.WRAP_CONTENT   // Alto
                                    );
                                    params.setMargins(10, 10, 10, 10);  // Márgenes (izquierda, arriba, derecha, abajo)
                                    colorButton.setLayoutParams(params);


                                    // Establecer un listener para cambiar la imagen cuando se seleccione una variación
                                    colorButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // Cambiar la imagen al hacer clic en el botón
                                            Picasso.get().load(variationImage).into(productImage);
                                        }
                                    });

                                    // Añadir el botón al contenedor de variaciones
                                    LinearLayout variationContainer = findViewById(R.id.variation_container);
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

    // Método auxiliar para obtener un color a partir del nombre
    public int getColor(String color) {
        switch (color.toLowerCase()) {
            case "1": // Rojo
                return ContextCompat.getColor(this, R.color.color_rojo);
            case "2": // Azul Marino
                return ContextCompat.getColor(this, R.color.color_azul_marino);
            case "3": // Negro
                return ContextCompat.getColor(this, R.color.color_negro);
            case "4": // Blanco
                return ContextCompat.getColor(this, R.color.color_blanco);
            case "5": // Dorado
                return ContextCompat.getColor(this, R.color.color_dorado);
            case "6": // Plateado
                return ContextCompat.getColor(this, R.color.color_plateado);
            case "7": // Verde
                return ContextCompat.getColor(this, R.color.color_verde);
            case "8": // Rosa
                return ContextCompat.getColor(this, R.color.color_rosa);
            case "9": // Morado
                return ContextCompat.getColor(this, R.color.color_morado);
            case "10": // Gris
                return ContextCompat.getColor(this, R.color.color_gris);
            case "11": // Amarillo
                return ContextCompat.getColor(this, R.color.color_amarillo);
            case "12": // Naranja
                return ContextCompat.getColor(this, R.color.color_naranja);
            case "13": // Beige
                return ContextCompat.getColor(this, R.color.color_beige);
            case "14": // Vino
                return ContextCompat.getColor(this, R.color.color_vino);
            case "15": // Turquesa
                return ContextCompat.getColor(this, R.color.color_turquesa);
            case "16": // Marrón
                return ContextCompat.getColor(this, R.color.color_marron);
            default:
                return ContextCompat.getColor(this, android.R.color.darker_gray);  // Color por defecto
        }
    }


    public void addToCart(String name, String price, String color){
        Cart cartProduct = new Cart( name, 1, "M", price, color);
        cartList.add(cartProduct);



    }

    public void removeFromCart(name, color){

    }




}
