package com.example.animepeak.Adapters;


import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.AnimeDetailsActivity;
import com.example.animepeak.Model.AnimeResponseModel;
import com.example.animepeak.R;


import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
   Context context;
   List<AnimeResponseModel> animeResponseModelList;

    public AnimeAdapter(Context context, List<AnimeResponseModel> animeResponseModelList) {
        this.context = context;
        this.animeResponseModelList = animeResponseModelList;
    }

    @NonNull
    @Override
    public AnimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.ViewHolder holder, int position) {
        String title = animeResponseModelList.get(position).getResults().get(position).getTitle();
        holder.ani_title.setText(title); // Setting title of the anime

        String imageUrl = animeResponseModelList.get(position).getResults().get(position).getImage();

        // Set a placeholder image or background color for the ImageView
        holder.ani_image.setImageResource(R.drawable.baseline_home_24);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.ani_image);

        holder.main_ani_item.setOnClickListener(view -> {
            Intent intent = new Intent(context, AnimeDetailsActivity.class);
            intent.putExtra("Title", title);
            intent.putExtra("Image", imageUrl);
            intent.putExtra("ID", animeResponseModelList.get(position).getResults().get(position).getId());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return animeResponseModelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ani_image;
        TextView ani_title;
        CardView main_ani_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ani_image = itemView.findViewById(R.id.ani_img);
            ani_title = itemView.findViewById(R.id.ani_title);
            main_ani_item = itemView.findViewById(R.id.main_ani_item);
        }
    }
}
