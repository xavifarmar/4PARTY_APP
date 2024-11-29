package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;  // Importa Picasso

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
                    //.placeholder(R.drawable.placeholder)  // Imagen de carga
                    //.error(R.drawable.error)  // Imagen en caso de error
                    .into(productImage);  // Establece la imagen en el ImageView
        } else {
            // Si no hay imagen, ponemos una imagen predeterminada
            productImage.setImageResource(R.drawable.profile_icon); // Imagen predeterminada
        }
    }
}
