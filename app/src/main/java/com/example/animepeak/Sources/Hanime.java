package com.example.animepeak.Sources;

import static com.example.animepeak.Activity.Anime_Details.Ani_ID;
import static com.example.animepeak.Activity.Anime_Details.Anime_Image;
import static com.example.animepeak.Activity.Anime_Details.Release;
import static com.example.animepeak.Activity.Anime_Details.anime_details;
import static com.example.animepeak.Activity.Anime_Details.details_loading;
import static com.example.animepeak.Activity.Anime_Details.details_recyclerView;
import static com.example.animepeak.Activity.Anime_Details.episode_text;
import static com.example.animepeak.Activity.Anime_Details.episodes;
import static com.example.animepeak.Activity.Anime_Details.expandableTextView;
import static com.example.animepeak.Activity.Anime_Details.img;
import static com.example.animepeak.Activity.Anime_Details.releasedDate;

import static com.example.animepeak.Activity.VideoPlayer.next_eps;
import static com.example.animepeak.Activity.VideoPlayer.player;
import static com.example.animepeak.Activity.VideoPlayer.previous_eps;
import static com.example.animepeak.Activity.VideoPlayer.videoView;
import static com.example.animepeak.Activity.VideoPlayer.video_loading;
import static com.example.animepeak.Fragments.HomeFragment.Home_IDList;
import static com.example.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static com.example.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static com.example.animepeak.Fragments.HomeFragment.home_loading;
import static com.example.animepeak.Fragments.HomeFragment.recyclerView;
import static com.example.animepeak.Fragments.SearchFragment.Search_IDList;
import static com.example.animepeak.Fragments.SearchFragment.Search_TitleUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_imageUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_loading;
import static com.example.animepeak.Fragments.SearchFragment.not_found;
import static com.example.animepeak.Fragments.SearchFragment.searchView;
import static com.example.animepeak.Fragments.SearchFragment.search_Box;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.Anime_Details;
import com.example.animepeak.Adapters.Ani_Details_Adapter;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.R;
import com.google.android.exoplayer2.MediaItem;

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

public class Hanime {
    public static class Hanime_popular extends AsyncTask<Void, Void, Void> {
        @SuppressLint("StaticFieldLeak")
        Activity activity;
        boolean is_added;

        public Hanime_popular(Activity activity, boolean is_added) {
            this.activity = activity;
            this.is_added = is_added;
        }

        private static final String TAG = "Hello";

        private static final String API_ENDPOINT = "https://hani.nsdev.ml/getLanding/trending";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(home_loading);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                if (Home_TitleUrlList.size() == 0) {


                        // Create a URL object for the API endpoint with the current page number

                        URL url = new URL(API_ENDPOINT );

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
                            String title = anime.getString("name");

                            String image = anime.getString("cover_url");


                            String ani_id = anime.getString("id");

                            if (!title.equals("") && !image.equals("") && !ani_id.equals("")) {
                                Home_TitleUrlList.add(title);
                                Home_imageUrlList.add(image);
                                Home_IDList.add(ani_id);

                            }
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
            home_loading.setVisibility(View.GONE);
            if (is_added) {
                MainAdapter mainAdapter = new MainAdapter(activity, Home_TitleUrlList, Home_imageUrlList, Home_IDList);
                recyclerView.setAdapter(mainAdapter);
            }
        }
    }
    public static class Hanime_details extends AsyncTask<Void, Void, String> {
        Activity activity;
        int originalOrientation;
        public static int HID;


        public Hanime_details(Activity activity) {
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

                URL url = new URL("https://hani.nsdev.ml/getInfo/" + Ani_ID);
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
            details_loading.setVisibility(View.GONE);
            anime_details.setVisibility(View.VISIBLE);
            episode_text.setVisibility(View.VISIBLE);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject info = jsonObject.getJSONObject("info");
                img = jsonObject.getString("poster");
                String censor = info.getString("censored");
                releasedDate = info.getString("released_date");
                HID = jsonObject.getInt("id");



                Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes, activity);
                details_recyclerView.setAdapter(ani_details_adapter);

                Release.setText("Release: "+releasedDate);
                Anime_Details.Status.setText("Censor: "+censor);
                Glide.with(activity)
                        .load(img)
                        .into(Anime_Image);
                String desc= jsonObject.getString("description");
                expandableTextView.setText(desc);
                expandableTextView.setReadMoreText("More");
                expandableTextView.setReadLessText("Less");

                expandableTextView.setAnimationDuration(500);
                // Reset the orientation to the original orientation.
                activity.setRequestedOrientation(originalOrientation);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static class Hanime_stream extends AsyncTask<Void, Void, String> {
        Activity activity;

        public Hanime_stream(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            video_loading.bringToFront();
            videoView.setVisibility(View.INVISIBLE);
            previous_eps.setVisibility(View.GONE);
            next_eps.setVisibility(View.GONE);
            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(video_loading);
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

                URL url = new URL("https://hani.nsdev.ml/getVideo/" + Hanime_details.HID);
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
                if (player.isPlaying()) {
                    player.stop();
                }
                video_loading.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray sources = jsonObject.getJSONArray("streams");
                JSONObject source = sources.getJSONObject(1);
                String Link = source.getString("url");

// Create a MediaSource from the URL.
                Uri videoUri = Uri.parse(Link);
// Set the media item to be played.
                player.setMediaItem(MediaItem.fromUri(videoUri));

                player.prepare();

                videoView.setPlayer(player);
                player.setPlayWhenReady(true);
                previous_eps.setVisibility(View.VISIBLE);
                next_eps.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static class Hanime_search extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "Search";

        private static final String API_ENDPOINT = "https://hani.nsdev.ml/search?q=";
        Activity activity;
        boolean is_added;

        public  Hanime_search(Activity activity, boolean is_added) {
            this.activity = activity;
            this.is_added = is_added;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Glide.with(activity)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(Search_loading);
            search_Box.clearFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_Box.getWindowToken(), 0);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {


                String query = search_Box.getText().toString();
                query = query.replace(" ", "-");
                URL url = new URL(API_ENDPOINT + query);

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


//                        Log.d("Anime", String.valueOf(animeList));
                for (int i = 0; i < animeList.length(); i++) {
                    JSONObject anime = animeList.getJSONObject(i);
                    String title = anime.getJSONArray("titles").getString(0);

                    String image = anime.getString("cover_url");


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
                String query = search_Box.getText().toString();

                not_found.setText("Cannot Find Anime \'" + query + "\'");
                not_found.setVisibility(View.VISIBLE);
            } else {
                not_found.setVisibility(View.GONE);
            }
        }
    }
}
