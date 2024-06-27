package com.example.animepeak.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animepeak.R;

import java.util.List;

public class Genre_Adapter extends RecyclerView.Adapter<Genre_Adapter.ViewHolder>{
List<String> genresList;

    public Genre_Adapter(List<String> genresList) {
        this.genresList = genresList;
    }

    @NonNull
    @Override
    public Genre_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Genre_Adapter.ViewHolder holder, int position) {
            holder.Anime_Genre.setText(genresList.get(position));

    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Anime_Genre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Anime_Genre = itemView.findViewById(R.id.genre_name);
        }
    }
}
