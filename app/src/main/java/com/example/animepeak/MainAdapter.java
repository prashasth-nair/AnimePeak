package com.example.animepeak;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainAdapter extends  RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    private List<String> TitleUrlList;
    private List<String> imageUrlList;
    private List<String> UrlList;

    public MainAdapter(Activity activity, List<String> TitleUrlList,List<String> imageUrlList,List<String> UrlList) {
        this.activity = activity;
        this.TitleUrlList = TitleUrlList;
        this.imageUrlList = imageUrlList;
        this.UrlList = UrlList;

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
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {

        holder.ani_title.setText(TitleUrlList.get(position)); // Setting title of the anime
        String imageUrl = imageUrlList.get(position);


        // Set a placeholder image or background color for the ImageView
        holder.ani_image.setImageResource(R.drawable.baseline_home_24);
        Glide.with(activity)
                .load(imageUrl)
                .into(holder.ani_image);



    }



    @Override
    public int getItemCount() {
        return TitleUrlList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ani_image;
        TextView ani_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ani_image = itemView.findViewById(R.id.ani_img);
            ani_title = itemView.findViewById(R.id.ani_title);
        }
    }
}
