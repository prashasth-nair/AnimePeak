package com.example.animepeak.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animepeak.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class Ani_Details_Genre_Adapter extends RecyclerView.Adapter<Ani_Details_Genre_Adapter.ViewHolder>{
    Activity activity;
    JSONArray genre;

    public Ani_Details_Genre_Adapter(Activity activity, JSONArray genre) {
        this.activity = activity;
        this.genre = genre;
    }

    @NonNull
    @Override
    public Ani_Details_Genre_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.genre_list, parent, false);
        Ani_Details_Genre_Adapter.ViewHolder viewHolder = new Ani_Details_Genre_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ani_Details_Genre_Adapter.ViewHolder holder, int position) {
        try {
            holder.Anime_Genre.setText(genre.getString(position));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return genre.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Anime_Genre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Anime_Genre = itemView.findViewById(R.id.genre_name);
        }
    }
}
