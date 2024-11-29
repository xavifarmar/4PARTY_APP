package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;

public class ProductDescriptionActivity extends AppCompatActivity {

    private TextView productName, productPrice;
    private ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        // Inicializamos las vistas
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);

        // Recuperamos los datos del Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String price = intent.getStringExtra("product_price");
        String image = intent.getStringExtra("product_image");

        // Establecemos los valores
        productName.setText(name);
        productPrice.setText(price);

        // Verificamos si la imagen está presente antes de intentar cargarla
        if (image != null && !image.isEmpty()) {
            //Glide.with(this).load(image).into(productImage);
        } else {
            // Si no hay imagen, puedes poner una imagen predeterminada o dejar el ImageView vacío
            productImage.setImageResource(R.drawable.profile_icon); // default_image es una imagen predeterminada
        }
    }
}
