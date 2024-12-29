package com.example.a4party;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // Actualizar la cantidad en la base de datos
            //updateProductQuantityInCart(holder.itemView.getContext(), cart.getProduct_name(), newQuantity);

            // Notificar al adaptador que se actualizó el item
            notifyItemChanged(position);// Solo actualizar el item que se modificó
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                int newQuantity = cart.getQuantity() - 1;
                cart.setQuantity(newQuantity);
                holder.quantity.setText(String.valueOf(newQuantity));

                // Actualizar la cantidad en la base de datos
                //updateProductQuantityInCart(holder.itemView.getContext(), //cart.getProduct_name(), newQuantity);

                // Notificar al adaptador que se actualizó el item
                notifyItemChanged(position);// Solo actualizar el item que se modificó
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

    // Método para actualizar la cantidad en la base de datos
    private void updateProductQuantityInCart(Context context, String product_name, int quantity) {
        String url = "http://10.0.2.2/4PARTY/updateCartQuantity.php"; // URL de la API que actualiza la cantidad

        // Crear un objeto para enviar los parámetros (product_name y quantity)
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                Log.d("CartAdapter", "Cantidad actualizada correctamente");
                            } else {
                                Log.d("CartAdapter", "Error al actualizar la cantidad: " + jsonResponse.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CartAdapter", "Error de red: " + error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_name", product_name);  // Enviar el nombre del producto
                params.put("quantity", String.valueOf(quantity));  // Enviar la cantidad
                return params;
            }
        };

        // Añadir la solicitud a la cola de Volley
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
