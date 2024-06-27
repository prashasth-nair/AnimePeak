package com.example.animepeak.Activity;


import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.animepeak.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.TimeUnit;

public class VideoPlayerActivity extends AppCompatActivity {
    int Current;

    PlayerView videoView;
    LinearLayout previous_eps;
    LinearLayout next_eps;
    LinearLayout exo_track_selection_view;

    TextView AnimeName;
    TextView EpisodeName;
    TextView exo_quality_txt;
    TextView exo_remaining_time;
    ImageView video_loading;

    ExoPlayer player;
    int video_quality_num = 0;
    List<String> video_quality = new ArrayList<>();
    List<String> video_subtitles = new ArrayList<>();
    JSONArray sources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // Hide the status bar and navigation bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = findViewById(R.id.videoView);
        video_loading = findViewById(R.id.loading);


        Intent intent = getIntent();
        Current = intent.getIntExtra("current_episode", 0);
        String AnimeTitle = intent.getStringExtra("Title");

        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }

        }

        player = new ExoPlayer.Builder(this).build();
        AnimeName = videoView.findViewById(R.id.animeName);
        EpisodeName = videoView.findViewById(R.id.episodeName);
        ImageButton back = videoView.findViewById(R.id.back);
        previous_eps = videoView.findViewById(R.id.previousEpisode);
        next_eps = videoView.findViewById(R.id.nextEpisode);
        exo_track_selection_view = videoView.findViewById(R.id.exo_track_selection_view);
        exo_quality_txt = videoView.findViewById(R.id.exoQuality);
        exo_remaining_time = videoView.findViewById(R.id.exo_remaining_time);

        AnimeName.setText(AnimeTitle);
        int Episode = Current + 1;

        EpisodeName.setText("Episode: " + Episode);

        videoView.setControllerVisibilityListener(new PlayerView.ControllerVisibilityListener() {
            @Override
            public void onVisibilityChanged(int visibility) {
                // Add hide visibility function
            }
        });


        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                        !player.getPlayWhenReady()) {

                    videoView.setKeepScreenOn(false);
                } else { // STATE_READY, STATE_BUFFERING
                    // This prevents the screen from getting dim/lock
                    videoView.setKeepScreenOn(true);

                }
            }
        });

        exo_track_selection_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_quality != null && !video_quality.isEmpty()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this, R.style.MyDialogTheme);
                    alertDialog.setTitle("Video Quality");

                    int checkedItem = video_quality_num;
                    String[] video_quality_items = video_quality.toArray(new String[0]);
                    alertDialog.setSingleChoiceItems(video_quality_items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Store off the last position our player was in before we paused it.
                            long LastPosition = player.getCurrentPosition();
                            video_quality_num = which;
                            player.stop();

                            try {
                                JSONObject source = sources.getJSONObject(which);
                                String Link = source.getString("url");
                                String quality = source.getString("quality");
// Create a MediaSource from the URL.
                                Uri videoUri = Uri.parse(Link);

// Set the media item to be played.
                                player.setMediaItem(MediaItem.fromUri(videoUri));

                                player.prepare();
                                player.seekTo(LastPosition);

                                videoView.setPlayer(player);
                                player.setPlayWhenReady(true);
                                exo_quality_txt.setText("Quality(" + quality + ")");
                                dialog.dismiss();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });

                    AlertDialog alert = alertDialog.create();

                    alert.setCanceledOnTouchOutside(true);
                    alert.getWindow().setLayout(500, 400);
                    alert.show();
                } else {
                    Log.d("Here", String.valueOf(video_quality));
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Handler handler = new Handler();
        Runnable updateRemainingTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateRemainingTime();
                handler.postDelayed(this, 1000);
            }
        };
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if (playbackState == Player.STATE_READY && player.isPlaying()) {
                    // Start updating the remaining time TextView on each player tick
                    handler.post(updateRemainingTimeRunnable);
                } else {
                    // Stop updating the remaining time TextView when the player is not playing
                    handler.removeCallbacks(updateRemainingTimeRunnable);
                }
            }
        });

    }

    // Define a method to update the remaining time TextView
    private void updateRemainingTime() {

        // Get the total duration of the media
        long durationMs = player.getDuration();

        // Get the current playback position of the media
        long currentPositionMs = player.getCurrentPosition();

        // Calculate the remaining time in milliseconds
        long remainingTimeMs = durationMs - currentPositionMs;

        // Format the remaining time in the desired format (e.g. HH:mm:ss)
        StringBuilder builder = new StringBuilder();
        long remainingTimeSec = Math.abs(remainingTimeMs) / 1000;
        long hours = TimeUnit.SECONDS.toHours(remainingTimeSec);
        long minutes = TimeUnit.SECONDS.toMinutes(remainingTimeSec) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = remainingTimeSec - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        if (hours == 0) {
            builder.append(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
        } else {

            builder.append(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
        }
        String remainingTimeString = builder.toString();

        // Update the TextView with the formatted remaining time
        exo_remaining_time.setText(remainingTimeString);

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
        video_quality.clear();
        video_subtitles.clear();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video_quality.clear();
        video_subtitles.clear();

        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }

        }

    }
}