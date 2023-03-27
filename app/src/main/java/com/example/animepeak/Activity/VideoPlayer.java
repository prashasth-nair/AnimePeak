package com.example.animepeak.Activity;


import static com.example.animepeak.Activity.Anime_Details.episodeID_list;


import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;



public class VideoPlayer extends AppCompatActivity {

    public static int Current;
    public static PlayerView videoView;
    public static ImageView video_loading;
    public static LinearLayout previous_eps;
    public static LinearLayout next_eps;
    public static LinearLayout exo_track_selection_view;
    TextView AnimeName;
    TextView EpisodeName;

    public static ExoPlayer player;
    private GogoAnime.Gogoanime_stream gogoanime_stream;
    private  Zoro.Zoro_stream zoro_stream;
    private Hanime.Hanime_stream hanime_stream;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // Hide the status bar and navigation bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = findViewById(R.id.videoView);
        video_loading = findViewById(R.id.video_loading);

        Intent intent = getIntent();
        Current = intent.getIntExtra("current_episode", 0);
        String AnimeTitle = intent.getStringExtra("Title");

        SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Source = sharedpreferences.getString("Source_Name", "GogoAnime");

        player = new ExoPlayer.Builder(this).build();
        AnimeName = findViewById(R.id.animeName);
        EpisodeName = findViewById(R.id.episodeName);
        ImageButton back = findViewById(R.id.back);
        previous_eps = findViewById(R.id.previousEpisode);
        next_eps = findViewById(R.id.nextEpisode);
        exo_track_selection_view = findViewById(R.id.exo_track_selection_view);

        AnimeName.setText(AnimeTitle);
        int Episode = Current+1;
        EpisodeName.setText("Episode: "+Episode);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        previous_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((Current - 1) >= 0) {
                    Current = Current - 1;
                    int Episode = Current+1;
                    EpisodeName.setText("Episode: "+Episode);
                        if (gogoanime_stream!=null){
                            gogoanime_stream.cancel(true);
                        } if (zoro_stream!=null){
                            zoro_stream.cancel(true);
                        } if (hanime_stream!=null){
                            hanime_stream.cancel(true);
                        }
                        if (player != null) {
                            player.stop();
                        }
                        if (Source.equals("GogoAnime")) {

                            gogoanime_stream = new GogoAnime.Gogoanime_stream(VideoPlayer.this);
                            gogoanime_stream.execute();
                        } else if (Source.equals("Zoro")) {

                            zoro_stream = new Zoro.Zoro_stream(VideoPlayer.this);
                            zoro_stream.execute();
                        } else if (Source.equals("Hanime")) {
                            hanime_stream = new Hanime.Hanime_stream(VideoPlayer.this);
                            hanime_stream.execute();
                        }

                }
            }
        });

        next_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int next_int = Current + 1;
                int Episode = next_int+1;
                EpisodeName.setText("Episode: "+Episode);
                if (next_int < episodeID_list.size()) {
                    Current = next_int;
                    if (gogoanime_stream!=null){
                        gogoanime_stream.cancel(true);
                    }
                    if (zoro_stream!=null){
                        zoro_stream.cancel(true);
                    }
                    if (hanime_stream!=null){
                        hanime_stream.cancel(true);
                    }
                    if (player != null) {
                        player.stop();
                    }
                    if (Source.equals("GogoAnime")) {

                        gogoanime_stream = new GogoAnime.Gogoanime_stream(VideoPlayer.this);
                        gogoanime_stream.execute();
                    } else if (Source.equals("Zoro")) {

                        zoro_stream = new Zoro.Zoro_stream(VideoPlayer.this);
                        zoro_stream.execute();
                    } else if (Source.equals("Hanime")) {
                        hanime_stream = new Hanime.Hanime_stream(VideoPlayer.this);
                        hanime_stream.execute();
                    }
                }
            }
        });

        if (Source.equals("GogoAnime")) {

            gogoanime_stream = new GogoAnime.Gogoanime_stream(this);
            gogoanime_stream.execute();
        } else if (Source.equals("Zoro")) {

            zoro_stream = new Zoro.Zoro_stream(this);
            zoro_stream.execute();
        } else if (Source.equals("Hanime")) {
            hanime_stream = new Hanime.Hanime_stream(this);
            hanime_stream.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            if (player.isPlaying()) {

                player.stop();


            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gogoanime_stream!=null) {
            gogoanime_stream.cancel(true);
        } if (zoro_stream!=null){
            zoro_stream.cancel(true);
        } if (hanime_stream!=null){
            hanime_stream.cancel(true);
        }
        if (player != null) {
            if (player.isPlaying()) {

                player.stop();
                player.release();
                player = null;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gogoanime_stream!=null) {
            gogoanime_stream.cancel(true);
        } if (zoro_stream!=null){
            zoro_stream.cancel(true);
        } if (hanime_stream!=null){
            hanime_stream.cancel(true);
        }

        if (player != null) {
            if (player.isPlaying()) {
                Log.d("Debug", "ExoPlayer is still playing!");
                player.stop();
                player.release();
                player = null;
            }

        }

    }
}