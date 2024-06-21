package com.example.animepeak.Adapters;

import android.app.Activity;
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
import com.example.animepeak.Activity.Anime_Details;
import com.example.animepeak.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Activity activity;
    private List<String> TitleUrlList;
    private List<String> imageUrlList;
    private List<String> IDList;

    public SearchAdapter(Activity activity, List<String> titleUrlList, List<String> imageUrlList, List<String> IDList) {
        this.activity = activity;
        TitleUrlList = titleUrlList;
        this.imageUrlList = imageUrlList;
        this.IDList = IDList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list, parent, false);
        SearchAdapter.ViewHolder viewHolder = new SearchAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.ani_title.setText(TitleUrlList.get(position)); // Setting title of the anime
        String imageUrl = imageUrlList.get(position);


        // Set a placeholder image or background color for the ImageView
        holder.ani_image.setImageResource(R.drawable.baseline_home_24);
        Glide.with(activity)
                .load(imageUrl)
                .into(holder.ani_image);

        holder.main_ani_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Anime_Details.class);

                intent.putExtra("Title", TitleUrlList.get(position));
                intent.putExtra("Image", imageUrlList.get(position));
                intent.putExtra("ID", IDList.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return TitleUrlList.size();
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
