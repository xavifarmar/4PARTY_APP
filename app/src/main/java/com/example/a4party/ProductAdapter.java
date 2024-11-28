package com.example.a4party;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {

    private List<Product> productoList;

    // Constructor
    public ProductAdapter(List<Product> productoList) {
        this.productoList = productoList;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_individual_product, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Product producto = productoList.get(position);
        holder.nombre.setText(producto.getProduct_name());
        holder.precio.setText(producto.getProduct_price());
        holder.imagen.setImageResource(producto.getProduct_img());
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView precio;
        ImageView imagen;
        ImageButton likeButton;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.product_title1);
            precio = itemView.findViewById(R.id.price_product1);
            imagen = itemView.findViewById(R.id.image_product1);
            likeButton = itemView.findViewById(R.id.like1);
        }
    }
}
