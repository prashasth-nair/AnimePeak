package com.example.animepeak.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.Adapters.SearchAdapter;
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


public class SearchFragment extends Fragment {
    EditText search_Box;
    ImageButton search_button;
    TextView not_found;
    RecyclerView searchView;
    SearchAdapter searchAdapter;
    ImageView loading;
    private final List<String> TitleUrlList = new ArrayList<>();
    private final List<String> imageUrlList = new ArrayList<>();
    private final List<String> IDList = new ArrayList<>();


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_Box = getView().findViewById(R.id.searchEditText);

        search_Box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    TitleUrlList.clear();
                    imageUrlList.clear();
                    IDList.clear();
                    new GetJsonTask().execute();

                    return true;
                }
                return false;
            }
        });

        not_found = getView().findViewById(R.id.not_found);
        search_button = getView().findViewById(R.id.searchButton);
        searchView = (RecyclerView) getView().findViewById(R.id.search_recycle);
        loading = (ImageView) getView().findViewById(R.id.loading);

        searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        searchAdapter= new SearchAdapter(getActivity(), TitleUrlList, imageUrlList, IDList);
        // notify the adapter that the data has changed
        searchAdapter.notifyDataSetChanged();
        searchView.setAdapter(searchAdapter);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TitleUrlList.clear();
                imageUrlList.clear();
                IDList.clear();
                new GetJsonTask().execute();
            }
        });
    }

    private class GetJsonTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "Hello";

        private static final String API_ENDPOINT = "https://gogoanime.consumet.stream/search?keyw=";

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


                String query = search_Box.getText().toString();
                query = query.replace(" ", "-");
                URL url = new URL(API_ENDPOINT +query);

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
                    if (!title.equals("") && !image.equals("") && !ani_id.equals("")) {
                        TitleUrlList.add(title);
                        imageUrlList.add(image);
                        IDList.add(ani_id);

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
                // clear the data from the adapter


                searchAdapter= new SearchAdapter(getActivity(), TitleUrlList, imageUrlList, IDList);
                // notify the adapter that the data has changed
                searchAdapter.notifyDataSetChanged();
                searchView.setAdapter(searchAdapter);
            }
            if (TitleUrlList.size()==0){
                String query = search_Box.getText().toString();

                not_found.setText("Cannot Find Anime \'"+query+"\'");
                not_found.setVisibility(View.VISIBLE);
            }else {
                not_found.setVisibility(View.GONE);
            }
        }
    }

}