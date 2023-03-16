package com.example.animepeak;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    List<String> TitleUrlList = new ArrayList<>();
    List<String> imageUrlList = new ArrayList<>();
    List<String> UrlList = new ArrayList<>();

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
        recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
// Inflate the layout for this fragment
        new GetJsonTask().execute();

    }

    private class GetJsonTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://jimov.herokuapp.com/anime/flv/browse/filter");
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

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Log.d("Image", String.valueOf(TitleUrlList.size()));
                // Data has already been loaded, so use it

                if (TitleUrlList.size() == 0) {
                    JSONObject anime = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        anime = jsonArray.getJSONObject(i);
                        String title = anime.getString("name");
                        String image = anime.getString("image");
                        String url = anime.getString("url");


                        if (!title.equals("") && !image.equals("") && !url.equals("")) {
                            TitleUrlList.add(title);
                            imageUrlList.add(image);
                            UrlList.add(url);

                        }
                    }
                }

                if (isAdded()) {
                    mainAdapter = new MainAdapter(getActivity(), TitleUrlList, imageUrlList, UrlList);
                    recyclerView.setAdapter(mainAdapter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
