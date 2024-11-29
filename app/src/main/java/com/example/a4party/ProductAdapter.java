package com.example.a4party;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a4party.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    // Constructor
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Seteamos los datos del producto
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        // Mostrar la imagen principal (la primera imagen de la lista)
        if (product.getImages().size() > 0) {
            String imageUrl = product.getImages().get(0);  // Tomamos la primera imagen como principal
            // Usar una librería como Glide o Picasso para cargar la imagen en ImageView
            /*Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.productImage);*/
        }
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
