package com.example.a4party;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        holder.productPrice.setText("€" + product.getPrice());
        String imageUrl = product.getImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.icon_user_solid); // Imagen por defecto
        }

        holder.itemView.setOnClickListener(v -> {
            // Pasa la información del producto a la actividad ProductDescriptionActivity
            Intent intent = new Intent(context, ProductDescriptionActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", imageUrl);
            intent.putExtra("product_color", product.getColor());// Usamos la URL estática aquí
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
        ImageButton productLike;
        boolean isRed = false;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price_product);
            productImage = itemView.findViewById(R.id.image_product);
            productLike = itemView.findViewById(R.id.like_button);
            productLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRed){
                        productLike.setImageResource(R.drawable.icon_heart_solid);
                    } else {
                        productLike.setImageResource(R.drawable.icon_heart_solid_red);
                    }
                    isRed = !isRed;
                }
            });

        }
    }

}
