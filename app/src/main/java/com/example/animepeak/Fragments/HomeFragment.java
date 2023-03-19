package com.example.animepeak.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.R;

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
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    ImageView loading;
    private final List<String> TitleUrlList = new ArrayList<>();
    private final List<String> imageUrlList = new ArrayList<>();
    private final List<String> IDList = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);
        loading = (ImageView) getView().findViewById(R.id.loading);
        recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        mainAdapter = new MainAdapter(getActivity(), TitleUrlList, imageUrlList, IDList);
        recyclerView.setAdapter(mainAdapter);
// Inflate the layout for this fragment
        new GetJsonTask().execute();

    }

    private class GetJsonTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "Hello";

        private static final String API_ENDPOINT = "https://gogoanime.consumet.stream/popular?page=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Glide.with(getActivity())
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(loading);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                if (TitleUrlList.size() == 0) {
                    for (int page = 1; page <= 4; page++) {
                        // Create a URL object for the API endpoint with the current page number

                        URL url = new URL(API_ENDPOINT + page);

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

                        JSONArray animeList = new JSONArray(String.valueOf(response));


//                        Log.d("Anime", String.valueOf(animeList));
                        for (int i = 0; i < animeList.length(); i++) {
                            JSONObject anime = animeList.getJSONObject(i);
                            String title = anime.getString("animeTitle");

                            String image = anime.getString("animeImg");


                            String ani_id = anime.getString("animeId");
//                        Log.d("Details:", "Title: " + title + " Rank: " + rank + " Score " + score + " Image: " + image);
                            // Do something with the anime data...
                            if (!title.equals("") && !image.equals("") && !ani_id.equals("")) {
                                TitleUrlList.add(title);
                                imageUrlList.add(image);
                                IDList.add(ani_id);

                            }
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
            loading.setVisibility(View.GONE);
            if (isAdded()) {
                mainAdapter = new MainAdapter(getActivity(), TitleUrlList, imageUrlList, IDList);
                recyclerView.setAdapter(mainAdapter);
            }
        }
    }
}
