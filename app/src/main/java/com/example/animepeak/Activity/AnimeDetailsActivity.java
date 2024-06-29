package com.example.animepeak.Activity;

import static com.example.animepeak.Activity.MainActivity.fav_list;

import static com.example.animepeak.Activity.MainActivity.is_login;
import static com.example.animepeak.Activity.MainActivity.removeFavByID;
import static com.example.animepeak.Activity.MainActivity.storeArrayToFirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;


import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.EpisodeAdapter;


import com.example.animepeak.Adapters.GenreAdapter;
import com.example.animepeak.Model.AnimeInfoModel;
import com.example.animepeak.Model.EpisodeModel;
import com.example.animepeak.RestApiClient.ApiInterface;
import com.example.animepeak.Utils.Fav_object;
import com.example.animepeak.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.github.glailton.expandabletextview.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeDetailsActivity extends AppCompatActivity {
    ImageView Anime_Image;
    FloatingActionButton favoriteButton;
    TextView Release,Status,net_error_ani_details;
    CardView anime_details;
    RelativeLayout episode_text;
    ExpandableTextView expandableTextView;
    ProgressBar details_loading;
    String Title,Ani_ID,img,releasedDate,status,desc;
    RecyclerView details_recyclerView,genre_recyclerView;
    ArrayList<EpisodeModel> episodeModelArrayList = new ArrayList<>();
    ArrayList<String> genres = new ArrayList<>();
    boolean is_fav = false;

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
        img = intent.getStringExtra("Image");

        details_loading = findViewById(R.id.loading);
        Release = findViewById(R.id.Anime_release);
        Status = findViewById(R.id.Anime_status);
        anime_details = findViewById(R.id.ani_details);
        expandableTextView = findViewById(R.id.expand_txt);
        episode_text = findViewById(R.id.episode_text);
        net_error_ani_details = findViewById(R.id.net_error_ani_details);
        favoriteButton = findViewById(R.id.fav_button);
        genre_recyclerView = findViewById(R.id.genre_recycler);

        if (fav_list != null) {
            for (Fav_object favObject : fav_list) {
                if (favObject.getID().contains(Ani_ID)) {
                    is_fav = true;
                    favoriteButton.setColorFilter(Color.RED);
                    favoriteButton.setImageResource(R.drawable.baseline_favorite_24_selected);
                    break;
                }
            }
        }

        favoriteButton.setOnClickListener(v -> {
            // Change heart icon's color and/or image
            if (is_login) {
                if (!is_fav) {
                    is_fav = true;
                    favoriteButton.setColorFilter(Color.RED);
                    favoriteButton.setImageResource(R.drawable.baseline_favorite_24_selected);

                    Fav_object favObject = new Fav_object(Title, Ani_ID, img);
                    fav_list.add(favObject);

                } else {
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
            genre_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            load();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            genre_recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
            load();
        }
    }

    public void save_Fav_List() {
        // Convert the fav_list ArrayList to a JSON string
        Gson gson = new Gson();
        String favListJson = gson.toJson(fav_list);
//        Log.d("")

        // Save the JSON string to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favListJson", favListJson);
        editor.apply();
        storeArrayToFirebase();
    }

    public void load() {
            // Load the image using Glide or Picasso here
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-consumet-org-mu.vercel.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        service.getAnimeInfo(Ani_ID).enqueue(new Callback<AnimeInfoModel>() {
            @Override
            public void onResponse(Call<AnimeInfoModel> call, Response<AnimeInfoModel> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    details_loading.setVisibility(View.GONE);
                    AnimeInfoModel animeInfoModel = response.body();
                    genres.addAll(animeInfoModel.getGenres());
                    episodeModelArrayList.addAll(animeInfoModel.getEpisodes());

                    EpisodeAdapter episode_adapter = new EpisodeAdapter(AnimeDetailsActivity.this,episodeModelArrayList);
                    details_recyclerView.setAdapter(episode_adapter);

                    GenreAdapter _genre_adapter = new GenreAdapter(genres);
                    genre_recyclerView.setAdapter(_genre_adapter);

                    Release.setText("Release Date: " + animeInfoModel.getReleaseDate());
                    Status.setText("Status: " + animeInfoModel.getStatus());
                    expandableTextView.setText(animeInfoModel.getDescription());
                    expandableTextView.setReadMoreText("More");
                    expandableTextView.setReadLessText("Less");
                    expandableTextView.setAnimationDuration(500);
                    Glide.with(AnimeDetailsActivity.this)
                            .load(img)
                            .into(Anime_Image);
                }
            }

            @Override
            public void onFailure(Call<AnimeInfoModel> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        episodeModelArrayList.clear();
        finish();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            EpisodeAdapter episode_adapter = new EpisodeAdapter(this,episodeModelArrayList);
            details_recyclerView.setAdapter(episode_adapter);


        } else {
            details_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            EpisodeAdapter episode_adapter = new EpisodeAdapter(this,episodeModelArrayList);
            details_recyclerView.setAdapter(episode_adapter);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        episodeModelArrayList.clear();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            episodeModelArrayList = new ArrayList<>();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}