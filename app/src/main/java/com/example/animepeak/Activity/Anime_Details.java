package com.example.animepeak.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.Ani_Details_Adapter;


import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;

import org.json.JSONArray;

import io.github.glailton.expandabletextview.ExpandableTextView;

public class Anime_Details extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static ImageView Anime_Image;
    public static TextView Release;
    public static TextView Status;
    public static CardView anime_details;
    public static RelativeLayout episode_text;

    public static ImageView details_loading;
    public static String Title;
    public static String Ani_ID;
    public static String img;
    public static String releasedDate;
    public static ExpandableTextView expandableTextView;
    public static String status;
    public static RecyclerView details_recyclerView;
    public static JSONArray episodes = new JSONArray();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime_details);


        Toolbar customToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);


        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        details_loading = findViewById(R.id.loading);
        Release = findViewById(R.id.Anime_release);
        Status = findViewById(R.id.Anime_status);
        anime_details = findViewById(R.id.ani_details);
        Ani_ID = intent.getStringExtra("ID");
        expandableTextView = findViewById(R.id.expand_txt);
        episode_text = findViewById(R.id.episode_text);
        getSupportActionBar().setTitle(Title);

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

    public void load() {
        if (!isDestroyed() && episodes.length() == 0) {
            // Load the image using Glide or Picasso here

            System.setProperty("okio.buffer-size", "16384");
            SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            String Source = sharedpreferences.getString("Source_Name", "GogoAnime");


            if (Source.equals("GogoAnime")) {
                GogoAnime.Gogoanime_details gogoanime_details = new GogoAnime.Gogoanime_details(this);
                if (gogoanime_details.getStatus() != AsyncTask.Status.RUNNING) {

                    gogoanime_details.execute();
                }

            } else if (Source.equals("Zoro")) {
                Zoro.Zoro_details zoro_details = new Zoro.Zoro_details(this);
                if (zoro_details.getStatus() != AsyncTask.Status.RUNNING) {

                    zoro_details.execute();
                }
            }else if (Source.equals("Hanime")) {
                Hanime.Hanime_details hanime_details = new Hanime.Hanime_details(this);
                if (hanime_details.getStatus() != AsyncTask.Status.RUNNING) {

                    hanime_details.execute();
                }
            }


        } else if (episodes.length() != 0) {
            Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, Anime_Details.this);
            details_recyclerView.setAdapter(ani_details_adapter);
            Release.setText(releasedDate);
            Anime_Details.Status.setText(status);
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}