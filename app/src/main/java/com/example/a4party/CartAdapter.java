package com.example.a4party;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;
    private OnQuantityChangedListener quantityChangedListener;

    public CartAdapter(List<Cart> cartList, OnQuantityChangedListener quantityChangedListener) {
        this.cartList = cartList;
        this.quantityChangedListener = quantityChangedListener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.productName.setText(cart.getProduct_name());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        holder.size.setText(cart.getSize());
        holder.price.setText(cart.getPrice());
        holder.color.setText(cart.getColor());

        // Listener para los botones de aumentar/disminuir cantidad
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);
            holder.quantity.setText(String.valueOf(newQuantity));
            quantityChangedListener.onQuantityChanged(cart);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                int newQuantity = cart.getQuantity() - 1;
                cart.setQuantity(newQuantity);
                holder.quantity.setText(String.valueOf(newQuantity));
                quantityChangedListener.onQuantityChanged(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, quantity, size, price, color;
        Button btnIncrease, btnDecrease;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTxt);
            quantity = itemView.findViewById(R.id.ProductQuantityTxt);
            size = itemView.findViewById(R.id.ProductSizeTxt);
            price = itemView.findViewById(R.id.product_price);
            color = itemView.findViewById(R.id.ProductColorTxt);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged(Cart cart);
    }
}
