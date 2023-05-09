package com.example.animepeak.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.Anime_Details;
import com.example.animepeak.R;


import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    private List<String> TitleUrlList;
    private List<String> imageUrlList;
    private List<String> IDList;

    public MainAdapter(Activity activity, List<String> TitleUrlList, List<String> imageUrlList, List<String> IDList) {
        this.activity = activity;
        this.TitleUrlList = TitleUrlList;
        this.imageUrlList = imageUrlList;
        this.IDList = IDList;

    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.ani_title.setText(TitleUrlList.get(position)); // Setting title of the anime


        String imageUrl = imageUrlList.get(position);


        // Set a placeholder image or background color for the ImageView
        holder.ani_image.setImageResource(R.drawable.baseline_home_24);
        Glide.with(activity)
                .load(imageUrl)

                .into(holder.ani_image);


        // Set the animation on the item view
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_recycler);
        holder.itemView.startAnimation(animation);

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
