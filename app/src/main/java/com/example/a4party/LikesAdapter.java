package com.example.a4party;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikeViewHolder>{

    private List<Likes> likesList;

    public LikesAdapter(List<Likes> likesList) {
        this.likesList = likesList;
    }

    @Override
    public LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_like, parent, false);
    return new LikeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LikeViewHolder holder, int position) {
        Likes like = likesList.get(position);

        holder.product_name.setText(like.getProduct_name());
        String image_url = like.getImage_url();

        if (image_url != null && !image_url.isEmpty()){
            Picasso.get().load(image_url).into(holder.product_image);
        }else{
            holder.product_image.setImageResource(R.drawable.icon_heart_regular);
        }
    }

    @Override
    public int getItemCount() {
        return likesList.size();
    }


    public static class LikeViewHolder extends RecyclerView.ViewHolder {
    TextView product_name;
    ImageView product_image;
    ImageButton like_btn;

    public LikeViewHolder(View itemView) {
        super(itemView);

        product_name = itemView.findViewById(R.id.product_name_txt);
        product_image = itemView.findViewById(R.id.product_image);
        like_btn = itemView.findViewById(R.id.likeBtn);
    }


    }
}
