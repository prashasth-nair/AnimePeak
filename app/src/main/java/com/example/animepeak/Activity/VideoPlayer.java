package com.example.animepeak.Activity;


import static com.example.animepeak.Activity.Anime_Details.episodes;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.Ani_Details_Adapter;
import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


public class VideoPlayer extends AppCompatActivity {
    public static String EpisodeID;
    int Length;
    int Current;
    public static PlayerView videoView;
    public static ImageView video_loading;
    public static LinearLayout previous_eps;
    public static LinearLayout next_eps;
    public static LinearLayout exo_track_selection_view;
    public static WeakReference<VideoView> mVideoViewRef;
    public static ExoPlayer player;

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
        EpisodeID = intent.getStringExtra("ID");
        Length = intent.getIntExtra("Length", 0);
        Current = intent.getIntExtra("current_episode", 0);

        SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Source = sharedpreferences.getString("Source_Name", "GogoAnime");

        player = new ExoPlayer.Builder(this).build();
        ImageButton back = findViewById(R.id.back);
        previous_eps = findViewById(R.id.previousEpisode);
        next_eps = findViewById(R.id.nextEpisode);
        exo_track_selection_view = findViewById(R.id.exo_track_selection_view);


        exo_track_selection_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a TrackSelectionDialogBuilder
//                TrackSelectionDialogBuilder builder = new TrackSelectionDialogBuilder(VideoPlayer.this, "Video Quality");
//
//// get the current media source
//                MediaSource mediaSource = exoPlayer.getCurrentMediaSource();
//
//// get the available track groups from the media source
//                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = exoPlayer.getTrackSelector().getCurrentMappedTrackInfo();
//                if (mappedTrackInfo != null) {
//                    int rendererIndex = 0; // the index of the renderer for which to show track selection dialog (e.g. video renderer)
//                    int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
//                    TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
//                    builder.setTrackNameProvider(new AdaptiveTrackNameProvider(trackGroups));
//                    builder.build().show();
//                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        previous_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Current", String.valueOf(Current));
                if ((Current - 1) >= 0) {

                    try {

                        JSONObject firstElement = episodes.getJSONObject(Current - 1);
                        String name = firstElement.getString("number");
                        String id = firstElement.getString("id");
                        intent.putExtra("ID", id);
                        intent.putExtra("Length", episodes.length());

                        if (!Source.equals("Hanime")) {
                            intent.putExtra("current_episode", Current-1);
                        }
                        finish();
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        int next_int = Current + 1;
        next_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (next_int < Length) {
                    try {
                        Intent intent = new Intent(VideoPlayer.this, VideoPlayer.class);
                        JSONObject firstElement = episodes.getJSONObject(next_int);
                        String name = firstElement.getString("number");
                        String id = firstElement.getString("id");
                        Log.d("NextID", String.valueOf(next_int));
                        intent.putExtra("ID", id);
                        intent.putExtra("Length", episodes.length());
                        if (!Source.equals("Hanime")) {
                            intent.putExtra("current_episode", next_int);
                        }
                        finish();

                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        if (Source.equals("GogoAnime")) {
            new GogoAnime.Gogoanime_stream(this).execute();
        } else if (Source.equals("Zoro")) {

            new Zoro.Zoro_stream(this).execute();
        } else if (Source.equals("Hanime")) {
            new Hanime.Hanime_stream(this).execute();
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
                player.release();

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();

            }
        }

    }
}