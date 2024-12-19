package com.example.a4party;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    private Spinner sizeSpinner, colorSpinner;
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
        sizeSpinner = findViewById(R.id.size_spinner);
        colorSpinner = findViewById(R.id.color_spinner);
        ImageButton likeBtn = findViewById(R.id.likeBtn);

        // Recuperamos los datos del Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("product_name");
        price = intent.getStringExtra("product_price");
        String image = intent.getStringExtra("product_image");
        color_name = intent.getStringExtra("product_color");

        // Establecemos los valores de nombre y precio
        productName.setText(name);
        productPrice.setText(price + "€");

        // Verificamos si la URL de la imagen está presente
        if (image != null && !image.isEmpty()) {
            Picasso.get().load(image).error(R.drawable.icon_user_solid).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.icon_user_solid);
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
        ImageButton backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
            startActivity(intent1);
        });

        // Listener para tamaño seleccionado
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Asignamos el tamaño seleccionado
                switch (position) {
                    case 0: selectedSize = "S"; break;
                    case 1: selectedSize = "M"; break;
                    case 2: selectedSize = "L"; break;
                    default: selectedSize = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedSize = null;
            }
        });

        // Listener para color seleccionado
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Asignamos el color seleccionado
                if (position > 0) {
                    color_name = parentView.getItemAtPosition(position).toString();
                } else {
                    color_name = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                color_name = null;
            }
        });

        // Configuramos los adaptadores para los Spinners
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

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
                            List<String> colorList = new ArrayList<>();
                            List<String> colorIds = new ArrayList<>();
                            List<String> imageUrls = new ArrayList<>();

                            // Agregar colores y IDs a las listas
                            for (int i = 0; i < variations.length(); i++) {
                                JSONObject variation = variations.getJSONObject(i);
                                String colorId = variation.getString("color"); // ID del color
                                String colorName = getColorName(colorId); // Convertir el color_id a nombre
                                String colorImage = variation.getString("image_url"); // URL de la imagen por color

                                colorList.add(colorName); // Agregar el nombre del color
                                colorIds.add(colorId); // Agregar el ID del color
                                imageUrls.add(colorImage); // Agregar la imagen correspondiente
                            }

                            // Si no hay colores disponibles, mostrar "No disponible"
                            if (colorList.isEmpty()) {
                                colorList.add("No disponible");
                                colorSpinner.setEnabled(false); // Deshabilitar el Spinner;
                            }

                            // Actualizar el Spinner con los colores
                            ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(ProductDescriptionActivity.this,
                                    android.R.layout.simple_spinner_item, colorList);
                            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            colorSpinner.setAdapter(colorAdapter);

                            // Listener para color seleccionado
                            colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    if (position > 0) { // Si se selecciona un color válido
                                        color_name = colorList.get(position); // Asignar el nombre del color
                                        colorInt = Integer.parseInt(colorIds.get(position)); // Asignar el ID del color
                                        String selectedColorImage = imageUrls.get(position); // Obtener la URL de la imagen del color seleccionado
                                        Picasso.get().load(selectedColorImage).into(productImage); // Actualizar la imagen
                                    } else {
                                        color_name = null; // No color seleccionado
                                        colorInt = 0; // No color seleccionado
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    color_name = null; // No color seleccionado
                                    colorInt = 0; // No color seleccionado
                                }
                            });

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
    public String getColorName(String colorId) {
        switch (colorId) {
            case "1": return "Rojo";
            case "2": return "Azul Marino";
            case "3": return "Negro";
            case "4": return "Blanco";
            case "5": return "Dorado";
            case "6": return "Plateado";
            case "7": return "Verde";
            case "8": return "Rosa";
            case "9": return "Morado";
            case "10": return "Gris";
            case "11": return "Amarillo";
            case "12": return "Naranja";
            case "13": return "Beige";
            case "14": return "Vino";
            case "15": return "Turquesa";
            case "16": return "Marrón";
            default: return "Desconocido"; // En caso de que el ID no coincida
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
                // Verificar los parámetros antes de enviarlos
                Log.d("AddToCart", "Product Name: " + name);
                Log.d("AddToCart", "Color ID: " + colorInt);
                Log.d("AddToCart", "Size: " + selectedSize);

                // Crear los parámetros que se enviarán al servidor
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("product_name", name);
                params.put("color_id", String.valueOf(colorInt));
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
