package com.example.animepeak.Activity;


import static com.example.animepeak.Activity.Anime_Details.episodeID_list;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.example.animepeak.R;
import com.example.animepeak.Sources.AniList;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.common.collect.ImmutableList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.TimeUnit;

@SuppressLint("StaticFieldLeak")
public class VideoPlayer extends AppCompatActivity {
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    public static int Current;

    public static PlayerView videoView;
    public static LinearLayout previous_eps;
    public static LinearLayout next_eps;
    public static LinearLayout exo_track_selection_view;
    ImageButton temp_ffwd;
    ImageButton temp_rewind;

    TextView AnimeName;
    TextView EpisodeName;
    public static TextView exo_quality_txt;
    public static TextView exo_remaining_time;
    public static ImageView video_loading;

    public static ExoPlayer player;
    private AniList.AniList_stream AniList_stream;
    public static int video_quality_num = 0;
    public static int video_SUBTITLE_num = 0;
    public static List<String> video_quality = new ArrayList<>();
    public static List<String> video_subtitles = new ArrayList<>();
    public static JSONArray sources;
    public static JSONArray subtitles;
    public static Uri videoUri;

    @SuppressLint("MissingInflatedId")
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


        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(this, new GestureListener());
        }
        if (scaleGestureDetector == null) {
            scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(this, videoView));
        }


        if (player != null) {
            if (player.isPlaying()) {

                player.stop();
                player.release();
                player = null;
            }

        }

        player = new ExoPlayer.Builder(this).build();
        AnimeName = findViewById(R.id.animeName);
        EpisodeName = findViewById(R.id.episodeName);
        ImageButton back = findViewById(R.id.back);
        previous_eps = findViewById(R.id.previousEpisode);
        next_eps = findViewById(R.id.nextEpisode);
        exo_track_selection_view = findViewById(R.id.exo_track_selection_view);
        exo_quality_txt = findViewById(R.id.exoQuality);
        exo_remaining_time = findViewById(R.id.exo_remaining_time);

        AnimeName.setText(AnimeTitle);
        int Episode = Current + 1;

        EpisodeName.setText("Episode: " + Episode);


        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                gestureDetector.onTouchEvent(event);
                scaleGestureDetector.onTouchEvent(event);
                return true;
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
                if (video_quality != null && video_quality.size() > 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlayer.this, R.style.MyDialogTheme);
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

        checkPrevious();
        previous_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((Current - 1) >= 0) {
                    Current = Current - 1;

                    int Episode = Current + 1;
                    EpisodeName.setText("Episode: " + Episode);
                    if (AniList_stream != null) {
                        AniList_stream.Cancel();
                    }
                    if (player != null) {
                        player.stop();
                    }
                    AniList_stream = new AniList.AniList_stream(VideoPlayer.this);
                    AniList_stream.execute();


                }
                video_quality.clear();
                video_subtitles.clear();
                checkPrevious();
                checkNext();
            }
        });
        checkNext();
        next_eps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int next_int = Current + 1;
                int Episode = next_int + 1;
                EpisodeName.setText("Episode: " + Episode);
                if (next_int < episodeID_list.size()) {
                    Current = next_int;
                    if (AniList_stream != null) {
                        AniList_stream.Cancel();
                    }

                    if (player != null) {
                        player.stop();
                    }
                    AniList_stream = new AniList.AniList_stream(VideoPlayer.this);
                    AniList_stream.execute();
                }
                video_quality.clear();
                video_subtitles.clear();
                checkPrevious();
                checkNext();
            }
        });

        AniList_stream = new AniList.AniList_stream(this);
        AniList_stream.execute();
        // Add a Player.EventListener to the player
        // Create a handler and a runnable to update the remaining time TextView on each player tick
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


    public void checkPrevious() {
        if ((Current - 1) >= 0) {

            previous_eps.setEnabled(true);
            previous_eps.setAlpha(1.0f);
        } else {
            previous_eps.setEnabled(false);
            previous_eps.setAlpha(0.5f);
        }
    }

    public void checkNext() {
        if ((Current + 1) < episodeID_list.size()) {

            next_eps.setEnabled(true);
            next_eps.setAlpha(1.0f);
        } else {
            next_eps.setEnabled(false);
            next_eps.setAlpha(0.5f);
        }
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
        if (AniList_stream != null) {
            AniList_stream.Cancel();
        }
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
        if (AniList_stream != null) {
            AniList_stream.Cancel();
        }

        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }

        }

    }

    //Gensture Controls
    private static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        Activity activity;
        private PlayerView playerView;
        private float scaleFactor = 1f;

        public ScaleListener(Activity activity, PlayerView playerView) {
            this.playerView = playerView;
            this.activity = activity;
        }


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Get the scale factor from the detector
            float scaleFactor = detector.getScaleFactor();
            if (scaleFactor > 1f) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            } else {
                // Zoom out
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }
            // Handle the zoom action
            // For example, you can change the size of a view based on the scale factor
            // or update a zoom level variable to be used in your drawing code
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Handle single tap here
            if (videoView.isControllerVisible()) {
                videoView.hideController();
            } else {
                videoView.showController();
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            long seekPosition = e.getX() < videoView.getWidth() / 2 ?
                    player.getCurrentPosition() - 10000L : // seek 10 seconds backward
                    player.getCurrentPosition() + 10000L; // seek 10 seconds forward
            // seek the player to the new position
            player.seekTo(seekPosition);
            if (!videoView.isControllerVisible()) {


                // show the button with an animation
                float x = e.getX();
                float halfScreenWidth = videoView.getWidth() / 2.0f;

                int buttonId = x < halfScreenWidth ? R.id.temp_rewind : R.id.temp_fast_forward;
                ImageView doubleTapButton = VideoPlayer.this.findViewById(buttonId);
                doubleTapButton.setVisibility(View.VISIBLE);
                doubleTapButton.setScaleX(0.0f);
                doubleTapButton.setScaleY(0.0f);
                doubleTapButton.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                doubleTapButton.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }


            return super.onDoubleTap(e);

        }
    }
}