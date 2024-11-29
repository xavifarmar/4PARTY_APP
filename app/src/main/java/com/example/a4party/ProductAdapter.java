package com.example.a4party;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;  // Importa Picasso

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    // Constructor del adaptador
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Establece el nombre y precio del producto
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        // Usar una URL estática para la imagen
        String imageUrl = "https://static.bershka.net/4/photos2/2024/I/0/2/p/5342/171/800//06/5342171800_2_4_3.jpg?t=1689779857435";

        // Cargar la imagen con Picasso desde la URL estática
        Picasso.get()
                .load(imageUrl)  // URL estática de la imagen
                //.placeholder(R.drawable.placeholder)  // Imagen de carga
                //.error(R.drawable.error)  // Imagen en caso de error
                .into(holder.productImage);  // Establece la imagen en el ImageView

        // Configuración del clic en un producto
        holder.itemView.setOnClickListener(v -> {
            // Pasa la información del producto a la actividad ProductDescriptionActivity
            Intent intent = new Intent(context, ProductDescriptionActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", imageUrl); // Usamos la URL estática aquí
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price_product);
            productImage = itemView.findViewById(R.id.image_product);
        }
    }
}
