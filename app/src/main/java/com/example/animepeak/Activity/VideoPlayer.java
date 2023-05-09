package com.example.animepeak.Activity;


import static com.example.animepeak.Activity.Anime_Details.episodeID_list;


import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.common.collect.ImmutableList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class VideoPlayer extends AppCompatActivity {
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    public static int Current;
    public static PlayerView videoView;
    public static LinearLayout previous_eps;
    public static LinearLayout next_eps;
    public static LinearLayout exo_track_selection_view;
    public static LinearLayout exo_subtitle_selection_view;
    TextView AnimeName;
    TextView EpisodeName;
    public static TextView exo_quality_txt;

    public static ExoPlayer player;
    private GogoAnime.Gogoanime_stream gogoanime_stream;
    private Zoro.Zoro_stream zoro_stream;
    private Hanime.Hanime_stream hanime_stream;
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


        Intent intent = getIntent();
        Current = intent.getIntExtra("current_episode", 0);
        String AnimeTitle = intent.getStringExtra("Title");

        SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Source = sharedpreferences.getString("Source_Name", "GogoAnime");

        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(this, new GestureListener());
        }
        if (scaleGestureDetector == null) {
            scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(this,videoView));
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
        exo_subtitle_selection_view = findViewById(R.id.exo_subtitle_selection_view);
        exo_quality_txt = findViewById(R.id.exoQuality);


        exo_subtitle_selection_view.setEnabled(false);
        exo_subtitle_selection_view.setAlpha(0.5f);

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

        exo_subtitle_selection_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_subtitles != null && video_subtitles.size() > 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoPlayer.this, R.style.MyDialogTheme);
                    alertDialog.setTitle("Video Subtitles");

                    int checkedItem = video_SUBTITLE_num;
                    String[] video_subtitles_items = video_subtitles.toArray(new String[0]);
                    alertDialog.setSingleChoiceItems(video_subtitles_items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                long LastPosition = player.getCurrentPosition();
                                video_SUBTITLE_num = which;
                                if (which == 0) {
                                    // Set the media item to be played.
                                    player.setMediaItem(MediaItem.fromUri(videoUri));

                                    player.prepare();
                                    player.seekTo(LastPosition);

                                    videoView.setPlayer(player);
                                    player.setPlayWhenReady(true);
                                } else {
                                    JSONObject subtitleobj = subtitles.getJSONObject(which - 1);
                                    String subtitleUri = subtitleobj.getString("url");
                                    MediaItem.SubtitleConfiguration subtitle =
                                            new MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitleUri))
                                                    .setMimeType(MimeTypes.TEXT_VTT) // The correct MIME type (required).
                                                    .setLanguage("en") // MUST, The subtitle language (optional).
                                                    .setSelectionFlags(C.SELECTION_FLAG_DEFAULT) //MUST,  Selection flags for the track (optional).
                                                    .build();
                                    MediaItem mediaItem =
                                            new MediaItem.Builder()
                                                    .setUri(videoUri)
                                                    .setSubtitleConfigurations(ImmutableList.of(subtitle))

                                                    .build();

                                    player.setMediaItem(mediaItem);
                                    player.setPlayWhenReady(true);
                                    player.prepare();
                                    player.seekTo(LastPosition);
                                    player.play();
                                }
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
                                exo_quality_txt.setText("Quality("+quality+")");
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
                    if (gogoanime_stream != null) {
                        gogoanime_stream.cancel(true);
                    }
                    if (zoro_stream != null) {
                        zoro_stream.cancel(true);
                    }
                    if (hanime_stream != null) {
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
                        try {
                            zoro_stream.execute().get();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (Source.equals("Hanime")) {
                        hanime_stream = new Hanime.Hanime_stream(VideoPlayer.this);
                        hanime_stream.execute();
                    }


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
                    if (gogoanime_stream != null) {
                        gogoanime_stream.cancel(true);
                    }
                    if (zoro_stream != null) {
                        zoro_stream.cancel(true);
                    }
                    if (hanime_stream != null) {
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
                video_quality.clear();
                video_subtitles.clear();
                checkPrevious();
                checkNext();
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
        if (gogoanime_stream != null) {
            gogoanime_stream.cancel(true);
        }
        if (zoro_stream != null) {
            zoro_stream.cancel(true);
        }
        if (hanime_stream != null) {
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
    private static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        Activity activity;
        private PlayerView playerView;
        private float scaleFactor = 1f;

        public ScaleListener(Activity activity,PlayerView playerView) {
            this.playerView = playerView;
            this.activity = activity;
        }


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Get the scale factor from the detector
            float scaleFactor = detector.getScaleFactor();
            if (scaleFactor > 1f) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            }else {
                // Zoom out
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }
            // Handle the zoom action
            // For example, you can change the size of a view based on the scale factor
            // or update a zoom level variable to be used in your drawing code
            return true;
        }
    }
    private static class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Handle single tap here
            if (videoView.isControllerVisible()){
                videoView.hideController();
            }else {
                videoView.showController();
            }
            return super.onSingleTapConfirmed(e);
        }
    }
}