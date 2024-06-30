package com.example.animepeak.Adapters;


import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animepeak.Activity.VideoPlayerActivity;
import com.example.animepeak.Model.EpisodeModel;
import com.example.animepeak.R;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    Context context;
    List<EpisodeModel> episodeModelList;

    public EpisodeAdapter(Context context, List<EpisodeModel> episodeModelList) {
        this.context = context;
        this.episodeModelList = episodeModelList;
    }

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.episode_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
        holder.episode_name.setText(String.valueOf(episodeModelList.get(position).getNumber()));
        holder.episode_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("Title",episodeModelList.get(position).getId().toUpperCase());
                    intent.putExtra("link",episodeModelList.get(position).getUrl());
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeModelList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView episode_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            episode_name = itemView.findViewById(R.id.episode_name);
        }
    }
}
