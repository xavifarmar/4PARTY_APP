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

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<Product> productList;

    // Constructor del adaptador
    public SearchAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, int position) {
        Product product = productList.get(position);

        // Establece el nombre y precio del producto
        holder.productName.setText(product.getName());
        holder.productPrice.setText("€" + product.getPrice());
        String imageUrl = product.getImage();
        int liked = product.getLiked();  // Obtenemos el estado de "liked" del producto

        // Cargar la imagen del producto
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.icon_user_solid); // Imagen por defecto si no hay URL
        }

        // Establecer la imagen del botón de like dependiendo del estado (si tiene like o no)
        if (liked == 1) {
            holder.productLike.setImageResource(R.drawable.icon_heart_solid);  // Corazón lleno (liked)
        } else {
            holder.productLike.setImageResource(R.drawable.icon_heart_regular);  // Corazón vacío (no liked)
        }

        // Acción al hacer click en el producto para mostrar su descripción
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDescriptionActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", imageUrl);
            intent.putExtra("product_color", product.getColor());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        ImageButton productLike;

        public SearchViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price_product);
            productImage = itemView.findViewById(R.id.image_product);
            productLike = itemView.findViewById(R.id.like_button);


                }
            }
        }

