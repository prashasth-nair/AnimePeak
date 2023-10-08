package com.example.animepeak.Adapters;

import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Fragments.FavouriteFragment.fav_recycler;
import static com.example.animepeak.Fragments.FavouriteFragment.no_fav;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.example.animepeak.Activity.Anime_Details;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;

import java.util.ArrayList;
import java.util.Objects;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    public static Activity fav_activity;
    ArrayList<Fav_object> fav_list;

    public FavAdapter(Activity activity, ArrayList<Fav_object> fav_list) {
        this.fav_activity = activity;
        this.fav_list = fav_list;


        if (source_list_size()==0){
            no_fav.setVisibility(View.VISIBLE);
        }else {
            no_fav.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list, parent, false);
        FavAdapter.ViewHolder viewHolder = new FavAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ani_title.setText(fav_list.get(position).getTitle());
        Glide.with(fav_activity)
                .load(fav_list.get(position).getImg())
                .into(holder.ani_image);
        holder.main_ani_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fav_activity, Anime_Details.class);

                intent.putExtra("Title", fav_list.get(position).getTitle());
                intent.putExtra("Image", fav_list.get(position).getImg());
                intent.putExtra("ID", fav_list.get(position).getID());
                fav_activity.startActivity(intent);

            }
        });

    }

    public int source_list_size(){
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
