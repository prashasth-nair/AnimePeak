package com.example.animepeak.Activity;

import static com.example.animepeak.Activity.MainActivity.fav_list;

import static com.example.animepeak.Functions.Fav_object.removeFavByID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.MenuItem;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.Ani_Details_Adapter;


import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.glailton.expandabletextview.ExpandableTextView;
@SuppressLint("StaticFieldLeak")
public class Anime_Details extends AppCompatActivity {

    public static ImageView Anime_Image;

    public static ImageButton favoriteButton;
    public static TextView Release;
    public static TextView Status;
    public static TextView net_error_ani_details;
    public static CardView anime_details;
    public static RelativeLayout episode_text;
    public static RelativeLayout anime_details_main;

    public static ImageView details_loading;
    public static String Title;
    public static String Ani_ID;
    public static String img;
    public static String releasedDate;
    public static ExpandableTextView expandableTextView;
    public static String status;
    public static RecyclerView details_recyclerView;
    public static JSONArray episodes = new JSONArray();
    public static String desc;

    boolean is_fav = false;
    public static List<String> episodeID_list = new ArrayList<>();
    GogoAnime.Gogoanime_details gogoanime_details;
    Zoro.Zoro_details zoro_details;
    Hanime.Hanime_details hanime_details;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime_details);


        Toolbar customToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolbar);


        AppCompatTextView toolbar_title = findViewById(R.id.toolbar_title);


        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        Ani_ID = intent.getStringExtra("ID");

        details_loading = findViewById(R.id.loading);
        Release = findViewById(R.id.Anime_release);
        Status = findViewById(R.id.Anime_status);
        anime_details = findViewById(R.id.ani_details);
        expandableTextView = findViewById(R.id.expand_txt);
        episode_text = findViewById(R.id.episode_text);
        anime_details_main = findViewById(R.id.anime_details_main);
        net_error_ani_details = findViewById(R.id.net_error_ani_details);
        favoriteButton = findViewById(R.id.fav_button);
        if (fav_list!=null) {
            for (Fav_object favObject : fav_list) {
                if (favObject.getID().contains(Ani_ID)) {
                    is_fav = true;
                    favoriteButton.setColorFilter(Color.RED);
                    favoriteButton.setImageResource(R.drawable.baseline_favorite_24_selected);
                    break;
                }
            }
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change heart icon's color and/or image
                if (!is_fav) {
                    is_fav = true;
                    favoriteButton.setColorFilter(Color.RED);
                    favoriteButton.setImageResource(R.drawable.baseline_favorite_24_selected);
                    SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                    String Source = sharedpreferences.getString("Source_Name", "GogoAnime");
                    fav_list.add(new Fav_object(Title, Ani_ID,img,Source));
                }else{
                    is_fav = false;
                    favoriteButton.setColorFilter(Color.WHITE);
                    favoriteButton.setImageResource(R.drawable.baseline_favorite_unselected);
                    removeFavByID(Ani_ID);

                }

                save_Fav_List();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            toolbar_title.setText(Title);
            toolbar_title.setSelected(true);
        }

        Anime_Image = findViewById(R.id.Anime_Image);
        details_recyclerView = findViewById(R.id.episode_list);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation

            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            load();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation

            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            load();
        }
    }

    public void save_Fav_List(){
        // Convert the fav_list ArrayList to a JSON string
        Gson gson = new Gson();
        String favListJson = gson.toJson(fav_list);

        // Save the JSON string to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favListJson", favListJson);
        editor.apply();
    }

    public static List<String> extractEpisodeIds(String json) {
        List<String> episodeIds = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray episodesArray = jsonObject.getJSONArray("episodes");
            for (int i = 0; i < episodesArray.length(); i++) {
                JSONObject episodeObject = episodesArray.getJSONObject(i);
                String episodeId = episodeObject.getString("id");
                episodeIds.add(episodeId);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return episodeIds;
    }

    @SuppressLint("SetTextI18n")
    public void load() {
        if (!isDestroyed() && episodes.length() == 0) {
            // Load the image using Glide or Picasso here

            System.setProperty("okio.buffer-size", "16384");
            SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            String Source = sharedpreferences.getString("Source_Name", "GogoAnime");


            switch (Source) {
                case "GogoAnime":
                    gogoanime_details = new GogoAnime.Gogoanime_details(this);
                    if (gogoanime_details.getStatus() != AsyncTask.Status.RUNNING) {

                        gogoanime_details.execute();
                    }

                    break;
                case "Zoro":
                    zoro_details = new Zoro.Zoro_details(this);
                    if (zoro_details.getStatus() != AsyncTask.Status.RUNNING) {

                        zoro_details.execute();
                    }
                    break;
                case "Hanime":
                    hanime_details = new Hanime.Hanime_details(this);
                    if (hanime_details.getStatus() != AsyncTask.Status.RUNNING) {

                        hanime_details.execute();
                    }
                    break;
            }


        } else if (episodes.length() != 0) {
            Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, Anime_Details.this);
            details_recyclerView.setAdapter(ani_details_adapter);
            Release.setText("Release Date: " + releasedDate);
            Anime_Details.Status.setText("Status: " + status);
            expandableTextView.setText(desc);
            expandableTextView.setReadMoreText("More");
            expandableTextView.setReadLessText("Less");
//
            expandableTextView.setAnimationDuration(500);
            Glide.with(this)
                    .load(img)
                    .into(Anime_Image);
        } else {
            // The activity has been destroyed, so don't perform any operation here

            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        episodes = new JSONArray();
        episodeID_list.clear();

        if (gogoanime_details != null) {
            gogoanime_details.cancel(true);
            gogoanime_details = null;
        }
        if (zoro_details != null) {
            zoro_details.cancel(true);
            zoro_details=null;
        }
        if (hanime_details != null) {
            hanime_details.cancel(true);
            hanime_details =null;
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, Anime_Details.this);
            details_recyclerView.setAdapter(ani_details_adapter);


        } else {
            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, Anime_Details.this);
            details_recyclerView.setAdapter(ani_details_adapter);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        episodes = new JSONArray();
        episodeID_list.clear();

        if (gogoanime_details != null) {
            gogoanime_details.cancel(true);
            gogoanime_details=null;
        }
        if (zoro_details != null) {
            zoro_details.cancel(true);
            zoro_details =null;
        }
        if (hanime_details != null) {
            hanime_details.cancel(true);
            hanime_details =null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            episodes = new JSONArray();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}