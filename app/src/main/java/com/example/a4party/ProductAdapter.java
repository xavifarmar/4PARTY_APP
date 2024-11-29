package com.example.a4party;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

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

        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        if (product.getImages().size() > 0) {
            String imageUrl = product.getImages().get(0);
            /*Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.productImage);*/
        }

        // Configuración del clic en un producto
        holder.itemView.setOnClickListener(v -> {
            // Pasa la información del producto a la actividad ProductDescriptionActivity
            Intent intent = new Intent(context, ProductDescriptionActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", product.getImages().get(0)); // Primera imagen del producto
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
