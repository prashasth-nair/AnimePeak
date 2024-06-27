package com.example.animepeak.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.animepeak.Utils.Fav_object;
import com.example.animepeak.R;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
Context context;
ArrayList<Fav_object> fav_list;

    public FavAdapter(Context context, ArrayList<Fav_object> fav_list) {
        this.context = context;
        this.fav_list = fav_list;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ani_title.setText(fav_list.get(position).getTitle());
        Log.d("FavAdapter", "onBindViewHolder: " + fav_list.get(position).getTitle());
        Glide.with(context)
                .load(fav_list.get(position).getImg())
                .into(holder.ani_image);
        holder.main_ani_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnimeDetailsActivity.class);

                intent.putExtra("Title", fav_list.get(position).getTitle());
                intent.putExtra("Image", fav_list.get(position).getImg());
                intent.putExtra("ID", fav_list.get(position).getID());
                context.startActivity(intent);

            }
        });

    }

    public int source_list_size() {
        return fav_list.size();
    }

    @Override
    public int getItemCount() {
        return source_list_size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
