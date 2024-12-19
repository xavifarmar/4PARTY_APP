package com.example.a4party;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;

    // Constructor que recibe la lista de productos en el carrito
    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Imprimir valores para depurar
        Log.d("CartAdapter", "Producto: " + cart.getProduct_name());
        Log.d("CartAdapter", "Cantidad: " + cart.getQuantity());
        Log.d("CartAdapter", "Tamaño: " + cart.getSize());
        Log.d("CartAdapter", "Precio: " + cart.getPrice());
        Log.d("CartAdapter", "Color: " + cart.getColor());
        Log.d("CartAdapter", "Imagen URL: " + cart.getImage_url());

        // Asignar los datos del producto al viewHolder
        holder.productName.setText(cart.getProduct_name());
        holder.price.setText(cart.getPrice() + "€");
        holder.color.setText(cart.getColor());
        holder.size.setText(cart.getSize());
        String imageUrl = cart.getImage_url();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.icon_user_solid); // Imagen por defecto
        }
        holder.quantity.setText(String.valueOf(cart.getQuantity()));

        // Listener para los botones de aumentar/disminuir cantidad
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);
            holder.quantity.setText(String.valueOf(newQuantity));

            // Notificar al adaptador que se actualizó el item
            notifyItemChanged(position); // Solo actualizar el item que se modificó
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                int newQuantity = cart.getQuantity() - 1;
                cart.setQuantity(newQuantity);
                holder.quantity.setText(String.valueOf(newQuantity));

                // Notificar al adaptador que se actualizó el item
                notifyItemChanged(position); // Solo actualizar el item que se modificó
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList == null) {
            return 0;
        } else {
            return cartList.size();
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, quantity, size, price, color;
        ImageButton btnIncrease, btnDecrease;
        ImageView productImage;

        public CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.productNameTxt);
            quantity = itemView.findViewById(R.id.ProductQuantityTxt);
            size = itemView.findViewById(R.id.ProductSizeTxt);
            price = itemView.findViewById(R.id.ProductPriceTxt);
            color = itemView.findViewById(R.id.ProductColorTxt);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }

    // Método para actualizar la lista del carrito (puedes usarlo si cambias la lista desde la actividad)
    public void updateCartList(List<Cart> newCartList) {
        this.cartList = newCartList;
        notifyDataSetChanged();  // Actualiza toda la lista en el RecyclerView
    }
}
