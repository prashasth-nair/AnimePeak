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
import com.example.animepeak.Model.PopularAnimeResponse;
import com.example.animepeak.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    List<PopularAnimeResponse> animeInfoModelList;

    public SearchAdapter(Context context, List<PopularAnimeResponse> animeInfoModelList) {
        this.context = context;
        this.animeInfoModelList = animeInfoModelList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.ani_title.setText(animeInfoModelList.get(position).getResults().get(position).getTitle()); // Setting title of the anime
        String imageUrl = animeInfoModelList.get(position).getResults().get(position).getImage();

        // Set a placeholder image or background color for the ImageView
        holder.ani_image.setImageResource(R.drawable.baseline_home_24);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.ani_image);

        holder.main_ani_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnimeDetailsActivity.class);
                intent.putExtra("Title",animeInfoModelList.get(position).getResults().get(position).getTitle());
                intent.putExtra("Image", imageUrl);
                intent.putExtra("ID", animeInfoModelList.get(position).getResults().get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeInfoModelList.size();
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
