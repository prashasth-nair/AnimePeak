package com.example.animepeak.Activity;



import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.Ani_Details_Adapter;
import com.example.animepeak.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


public class VideoPlayer extends AppCompatActivity {
    String EpisodeID;
    VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // Hide the status bar and navigation bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        EpisodeID = intent.getStringExtra("ID");
        new VideoPlayer.GetJsonTask().execute();


    }
    private class GetJsonTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://gogoanime.consumet.stream/vidcdn/watch/"+EpisodeID);
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray sources = jsonObject.getJSONArray("sources_bk");
                JSONObject source = sources.getJSONObject(0);
                String Link = source.getString("file");
                // Set the MediaController for the VideoView
                MediaController mediaController = new MediaController(VideoPlayer.this);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

//                String videoUrl = "https://example.com/video.mp4";

                Uri videoUri = Uri.parse(Link);
                videoView.setVideoURI(videoUri);

                videoView.requestFocus();
                videoView.start();



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}