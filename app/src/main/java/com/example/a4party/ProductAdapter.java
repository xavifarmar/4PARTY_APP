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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        ImageButton productLike;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.price_product);
            productImage = itemView.findViewById(R.id.image_product);
            productLike = itemView.findViewById(R.id.like_button);

            // Al hacer clic en el botón de like
            productLike.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = productList.get(position);  // Obtener el producto en la posición correcta

                    // Cambiar imagen del botón de like dependiendo del estado actual
                    if (product.getLiked() == 1) {
                        // Si ya está "liked", eliminamos el like
                        removeLike(product);
                        product.setLiked(0);  // Cambiar el estado a no "liked"
                        productLike.setImageResource(R.drawable.icon_heart_regular);  // Corazón vacío
                          // Llamamos a la función para quitar el like del servidor
                    } else {
                        // Si no está "liked", agregamos el like
                        product.setLiked(1);  // Cambiar el estado a "liked"
                        productLike.setImageResource(R.drawable.icon_heart_solid);  // Corazón lleno
                        addLike(product);  // Llamamos a la función para agregar el like en el servidor
                    }
                }
            });
        }

        // Método para añadir el like a la base de datos
        private void addLike(Product product) {
            String url = "http://10.0.2.2/4PARTY/likes.php?addLike";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Aquí puedes manejar la respuesta si es necesario
                    System.out.println("Like agregado: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Aquí puedes manejar el error de la solicitud
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_name", product.getName());
                    params.put("action", "addLike");;// Pasar el nombre del producto
                    return params;
                }
            };

            // Agregar la solicitud a la cola de Volley
            Volley.newRequestQueue(context).add(request);
        }

        // Método para eliminar el like de la base de datos
        private void removeLike(Product product) {
            String url = "http://10.0.2.2/4PARTY/likes.php?removeLike=true";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Aquí puedes manejar la respuesta si es necesario
                    System.out.println("Like eliminado: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Aquí puedes manejar el error de la solicitud
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_name", product.getName());
                    params.put("action", "removeLike");  // Pasar el nombre del producto
                    return params;
                }
            };

            // Agregar la solicitud a la cola de Volley
            Volley.newRequestQueue(context).add(request);
        }
    }
}
