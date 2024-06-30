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
import com.example.animepeak.Model.PopularAnimeResponse.PopularAnime;
import com.example.animepeak.R;


import java.util.List;

public class PopularAnimeAdapter extends RecyclerView.Adapter<PopularAnimeAdapter.ViewHolder> {
   Context context;
   private List<PopularAnime> results;

    public PopularAnimeAdapter(Context context, List<PopularAnime> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public PopularAnimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAnimeAdapter.ViewHolder holder, int position) {
        PopularAnime result = results.get(position);
        holder.bind(result);
    }


    @Override
    public int getItemCount() {
        return results.size();
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

        public void bind(PopularAnime result) {
            Glide.with(itemView.getContext())
                    .load(result.getImage())
                    .into(ani_image);

            ani_title.setText(result.getTitle());

            main_ani_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), AnimeDetailsActivity.class);
                    // Assuming you want to get the first AnimeInfoModel from the results
                    intent.putExtra("Title", result.getTitle());
                    intent.putExtra("Image", result.getImage());
                    intent.putExtra("ID", result.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
