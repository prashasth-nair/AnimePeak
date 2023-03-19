package com.example.animepeak.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class Anime_Details extends AppCompatActivity {

    ImageView Anime_Image;
    TextView Release;
    TextView Status;
    CardView anime_details;
    RelativeLayout episode_text;
    ImageView loading;
    String Title;
    String Ani_ID;
    RecyclerView recyclerView;
    JSONArray episodes = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime_details);


        Toolbar customToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        loading = findViewById(R.id.loading);
        Release = findViewById(R.id.Anime_release);
        Status = findViewById(R.id.Anime_status);
        anime_details =findViewById(R.id.ani_details);
        Ani_ID = intent.getStringExtra("ID");

        episode_text = findViewById(R.id.episode_text);
        getSupportActionBar().setTitle(Title);

        Anime_Image = findViewById(R.id.Anime_Image);
        recyclerView =  findViewById(R.id.episode_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes,Anime_Details.this);
        recyclerView.setAdapter(ani_details_adapter);
        new Anime_Details.GetJsonTask().execute();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class GetJsonTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            anime_details.setVisibility(View.GONE);
            episode_text.setVisibility(View.GONE);
            Glide.with(Anime_Details.this)
                    .asGif()
                    .load(R.raw.loading_animation)
                    .into(loading);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://gogoanime.consumet.stream/anime-details/"+Ani_ID);
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
            loading.setVisibility(View.GONE);
            anime_details.setVisibility(View.VISIBLE);
            episode_text.setVisibility(View.VISIBLE);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String img = jsonObject.getString("animeImg");
                String releasedDate = jsonObject.getString("releasedDate");
                String status = jsonObject.getString("status");
                episodes = jsonObject.getJSONArray("episodesList");

                Release.setText(releasedDate);
                Status.setText(status);
                Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(episodes,Anime_Details.this);
                recyclerView.setAdapter(ani_details_adapter);
                Glide.with(Anime_Details.this)
                        .load(img)
                        .into(Anime_Image);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}