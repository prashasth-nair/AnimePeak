package com.example.animepeak.Sources;

import static com.example.animepeak.Activity.Anime_Details.Ani_ID;
import static com.example.animepeak.Activity.Anime_Details.Anime_Image;
import static com.example.animepeak.Activity.Anime_Details.Release;
import static com.example.animepeak.Activity.Anime_Details.anime_details;
import static com.example.animepeak.Activity.Anime_Details.desc;
import static com.example.animepeak.Activity.Anime_Details.details_loading;
import static com.example.animepeak.Activity.Anime_Details.details_recyclerView;
import static com.example.animepeak.Activity.Anime_Details.episodeID_list;
import static com.example.animepeak.Activity.Anime_Details.episode_text;
import static com.example.animepeak.Activity.Anime_Details.episodes;
import static com.example.animepeak.Activity.Anime_Details.expandableTextView;
import static com.example.animepeak.Activity.Anime_Details.extractEpisodeIds;
import static com.example.animepeak.Activity.Anime_Details.genre_recyclerView;
import static com.example.animepeak.Activity.Anime_Details.genres;
import static com.example.animepeak.Activity.Anime_Details.img;
import static com.example.animepeak.Activity.Anime_Details.net_error_ani_details;
import static com.example.animepeak.Activity.Anime_Details.releasedDate;
import static com.example.animepeak.Activity.Anime_Details.status;

import static com.example.animepeak.Activity.VideoPlayer.Current;

import static com.example.animepeak.Activity.VideoPlayer.exo_quality_txt;
import static com.example.animepeak.Activity.VideoPlayer.exo_remaining_time;
import static com.example.animepeak.Activity.VideoPlayer.next_eps;
import static com.example.animepeak.Activity.VideoPlayer.player;
import static com.example.animepeak.Activity.VideoPlayer.previous_eps;
import static com.example.animepeak.Activity.VideoPlayer.sources;
import static com.example.animepeak.Activity.VideoPlayer.videoView;

import static com.example.animepeak.Activity.VideoPlayer.video_quality;
import static com.example.animepeak.Activity.VideoPlayer.video_quality_num;
import static com.example.animepeak.Fragments.HomeFragment.Home_IDList;
import static com.example.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static com.example.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static com.example.animepeak.Fragments.HomeFragment.home_loading;
import static com.example.animepeak.Fragments.HomeFragment.network_error;
import static com.example.animepeak.Fragments.HomeFragment.recyclerView;
import static com.example.animepeak.Fragments.SearchFragment.Search_IDList;
import static com.example.animepeak.Fragments.SearchFragment.Search_TitleUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_imageUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_loading;
import static com.example.animepeak.Fragments.SearchFragment.not_found;
import static com.example.animepeak.Fragments.SearchFragment.searchBar;
import static com.example.animepeak.Fragments.SearchFragment.searchView;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.Anime_Details;

import com.example.animepeak.Adapters.Ani_Details_Adapter;
import com.example.animepeak.Adapters.Ani_Details_Genre_Adapter;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.R;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.util.Util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class GogoAnime {
    //    GogoAnime
    public static class Gogoanime_popular extends AsyncTask<Void, Void, String> {
        @SuppressLint("StaticFieldLeak")
        Activity activity;
        boolean is_added;

        public Gogoanime_popular(Activity activity, boolean is_added) {
            this.activity = activity;
            this.is_added = is_added;
        }

        private static final String TAG = "Hello";

        private static final String API_ENDPOINT = "https://consumet-rho.vercel.app/anime/gogoanime/top-airing?page=";
        boolean isLoading;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            // Display loading animation
            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(home_loading);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                if (Home_TitleUrlList.isEmpty()) {
                    for (int page = 1; page <= 4; page++) {
                        if (isCancelled()) {
                            Log.d("Here", "Cancel");
                            break; // Exit the loop if the task is canceled
                        }

                        URL url = new URL(API_ENDPOINT + page);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");

                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            response += line;
                        }
                        reader.close();
                        conn.disconnect();


                    }
                }

//                activity.runOnUiThread(() -> network_error.setVisibility(View.GONE));
                isLoading = false;

                return response;
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving top anime: " + e.getMessage());
//                activity.runOnUiThread(() -> network_error.setVisibility(View.VISIBLE));
                isLoading = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (is_added&&response!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                    JSONArray animeList = jsonObject.getJSONArray("results");

                    for (int i = 0; i < animeList.length(); i++) {
                        if (isCancelled()) {
                            Log.d("Here", "Cancel");
                            break; // Exit the loop if the task is canceled
                        }
                        JSONObject anime = animeList.getJSONObject(i);
                        String title = anime.getString("title");
                        String image = anime.getString("image");
                        String ani_id = anime.getString("id");

                        if (!title.isEmpty() && !image.isEmpty() && !ani_id.isEmpty()) {
                            Home_TitleUrlList.add(title);
                            Home_imageUrlList.add(image);
                            Home_IDList.add(ani_id);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                MainAdapter mainAdapter = new MainAdapter(activity, Home_TitleUrlList, Home_imageUrlList, Home_IDList);
                recyclerView.setAdapter(mainAdapter);
            }

            // Check if loading animation is still visible before hiding it
            if (home_loading.getVisibility() == View.VISIBLE) {
                home_loading.setVisibility(View.GONE);
            }

            // Show or hide the network error based on loading status and data availability
            if (!isLoading && Home_TitleUrlList.isEmpty()) {
                network_error.setVisibility(View.VISIBLE);
            } else {
                network_error.setVisibility(View.GONE);
            }
        }


    }

    public static class Gogoanime_details extends AsyncTask<Void, Void, String> {
        Activity activity;
        int originalOrientation;
        int Error;

        public Gogoanime_details(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Store the original orientation.
            originalOrientation = activity.getRequestedOrientation();

            // Lock the orientation to the current orientation.
            int currentOrientation = activity.getResources().getConfiguration().orientation;
            activity.setRequestedOrientation(currentOrientation);
            anime_details.setVisibility(View.GONE);
            episode_text.setVisibility(View.GONE);
            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(details_loading);
        }


        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://consumet-rho.vercel.app/anime/gogoanime/info/" + Ani_ID);
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                episodeID_list = extractEpisodeIds(result);

            } catch (IOException e) {

                Error = 1;

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

            details_loading.setVisibility(View.GONE);
            if (Error == 0) {
                net_error_ani_details.setVisibility(View.GONE);
                anime_details.setVisibility(View.VISIBLE);
                episode_text.setVisibility(View.VISIBLE);

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    img = jsonObject.getString("image");
                    releasedDate = jsonObject.getString("releaseDate");
                    status = jsonObject.getString("status");

                    if (episodes.length() == 0) {

                        episodes = jsonObject.getJSONArray("episodes");


                    }

                    Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, activity);
                    details_recyclerView.setAdapter(ani_details_adapter);

                    genres = jsonObject.getJSONArray("genres");
                    if (genres.length()>4){
                        JSONArray firstThreeItems = new JSONArray();
                        for (int i = 0; i < 4 && i < genres.length(); i++) {
                            firstThreeItems.put(genres.getString(i));

                        }
                        genres = firstThreeItems;
                    }
                    Ani_Details_Genre_Adapter ani_details_genre_adapter = new Ani_Details_Genre_Adapter(activity, genres);
                    genre_recyclerView.setAdapter(ani_details_genre_adapter);

                    Release.setText("Release Date: " + releasedDate);
                    Anime_Details.Status.setText("Status: " + status);
                    Glide.with(activity)
                            .load(img)
                            .into(Anime_Image);


                    desc = jsonObject.getString("description");
                    expandableTextView.setText(desc);
                    expandableTextView.setReadMoreText("More");
                    expandableTextView.setReadLessText("Less");
//
                    expandableTextView.setAnimationDuration(500);

                    // Reset the orientation to the original orientation.
                    activity.setRequestedOrientation(originalOrientation);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                net_error_ani_details.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class GogoAnime_search extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "Search";

        private static final String API_ENDPOINT = "https://consumet-rho.vercel.app/anime/gogoanime/";
        Activity activity;
        boolean is_added;
        String text;

        public GogoAnime_search(Activity activity, boolean is_added, String text) {
            this.activity = activity;
            this.is_added = is_added;
            this.text = text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Search_loading.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(Search_loading);
//            search_Box.clearFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {


//                String query = search_Box.getText().toString();
                text = text.replace(" ", "-");
                URL url = new URL(API_ENDPOINT + text);

                // Open an HTTP connection to the URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method and headers
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                // Read the response body from the input stream
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                // Parse the JSON response into a list of anime objects
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONArray animeList = jsonObject.getJSONArray("results");

                for (int i = 0; i < animeList.length(); i++) {
                    JSONObject anime = animeList.getJSONObject(i);
                    String title = anime.getString("title");

                    String image = anime.getString("image");


                    String ani_id = anime.getString("id");
                    if (!title.equals("") && !image.equals("") && !ani_id.equals("")) {
                        Search_TitleUrlList.add(title);
                        Search_imageUrlList.add(image);
                        Search_IDList.add(ani_id);

                    }

                }

                return null;
            } catch (IOException | JSONException e) {

                Log.e(TAG, "Error retrieving top anime: " + e.getMessage());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Search_loading.setVisibility(View.GONE);

            if (is_added) {
                // clear the data from the adapter


                SearchAdapter searchAdapter = new SearchAdapter(activity, Search_TitleUrlList, Search_imageUrlList, Search_IDList);
                // notify the adapter that the data has changed
                searchAdapter.notifyDataSetChanged();
                searchView.setAdapter(searchAdapter);
            }
            if (Search_TitleUrlList.size() == 0) {
//                String query = search_Box.getText().toString();

//                not_found.setText("Cannot Find Anime \'" + query + "\'");
                not_found.setText("Cannot Find the Anime ");
                not_found.setVisibility(View.VISIBLE);
            } else {
                not_found.setVisibility(View.GONE);
            }
        }
    }

    public static class Gogoanime_stream extends AsyncTask<Void, Void, String> {
        Activity activity;
        JSONObject source;

        public Gogoanime_stream(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            videoView.setVisibility(View.INVISIBLE);
            previous_eps.setVisibility(View.GONE);
            next_eps.setVisibility(View.GONE);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (player.isPlaying()) {
                player.stop();
                player.release();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://consumet-rho.vercel.app/anime/gogoanime/watch/" + episodeID_list.get(Current));
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (player.isPlaying()) {
                    player.stop();
                }

                JSONObject jsonObject = new JSONObject(result);
                sources = jsonObject.getJSONArray("sources");
                for (int i = 0; i < sources.length(); i++) {
                    JSONObject source = sources.getJSONObject(i);
                    String quality = source.getString("quality");
                    if (!quality.equals("backup") && !quality.equals("default")) {
                        video_quality.add(quality);
                    }
                }


                SharedPreferences sharedpreferences = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
                String Current_Quality = sharedpreferences.getString("Video_Quality", "480p");
                video_quality_num = video_quality.indexOf(Current_Quality);
                if (video_quality_num == -1) {
                    exo_quality_txt.setText("Quality(" + video_quality.get(0) + ")");

                    source = sources.getJSONObject(0);
                } else {

                    exo_quality_txt.setText("Quality(" + video_quality.get(video_quality_num) + ")");
                    source = sources.getJSONObject(video_quality_num);
                }
                String Link = source.getString("url");


// Create a MediaSource from the URL.
                Uri videoUri = Uri.parse(Link);

// Set the media item to be played.
                player.setMediaItem(MediaItem.fromUri(videoUri));

                player.prepare();

                videoView.setPlayer(player);
                player.setPlayWhenReady(true);

                videoView.setVisibility(View.VISIBLE);
                previous_eps.setVisibility(View.VISIBLE);
                next_eps.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
