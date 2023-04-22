package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.bottomNavigationView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.animepeak.Adapters.MainAdapter;

import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    public static ImageView home_loading;
    public static TextView network_error;
    public static List<String> Home_TitleUrlList = new ArrayList<>();
    public static List<String> Home_imageUrlList = new ArrayList<>();
    public static List<String> Home_IDList = new ArrayList<>();
    private GogoAnime.Gogoanime_popular gogoanime_popular;
    private Zoro.Zoro_popular zoro_popular;
    private Hanime.Hanime_popular hanime_popular;
    String Source;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);
        home_loading = (ImageView) getView().findViewById(R.id.loading);
        network_error = (TextView) getView().findViewById(R.id.net_error);

        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
        }

        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Source = sharedpreferences.getString("Source_Name", "GogoAnime");


        if (Source.equals("GogoAnime")) {
           gogoanime_popular = new GogoAnime.Gogoanime_popular(getActivity(), isAdded());
           gogoanime_popular.execute();
        } else if (Source.equals("Zoro")) {
            zoro_popular = new Zoro.Zoro_popular(getActivity(), isAdded());
            zoro_popular.execute();
        } else if (Source.equals("Hanime")) {
            hanime_popular = new Hanime.Hanime_popular(getActivity(), isAdded());
            hanime_popular.execute();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getView() != null) {

            // call the async function here to refresh the data
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            Source = sharedpreferences.getString("Source_Name", "GogoAnime");

            if (Source.equals("GogoAnime")) {
                gogoanime_popular = new GogoAnime.Gogoanime_popular(getActivity(), isAdded());
                gogoanime_popular.execute();
            } else if (Source.equals("Zoro")) {
                zoro_popular = new Zoro.Zoro_popular(getActivity(), isAdded());
                zoro_popular.execute();
            } else if (Source.equals("Hanime")) {
                hanime_popular = new Hanime.Hanime_popular(getActivity(), isAdded());
                hanime_popular.execute();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Home_TitleUrlList.clear();
        if (gogoanime_popular != null) {
            gogoanime_popular.cancel(true);
        }
        if (zoro_popular != null) {
            zoro_popular.cancel(true);
        }
        if (hanime_popular != null) {
            hanime_popular.cancel(true);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Home_TitleUrlList.clear();
        if (gogoanime_popular != null) {
            gogoanime_popular.cancel(true);
        }
        if (zoro_popular != null) {
            zoro_popular.cancel(true);
        }
        if (hanime_popular != null) {
            hanime_popular.cancel(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
            mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
            recyclerView.setAdapter(mainAdapter);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
            mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
            recyclerView.setAdapter(mainAdapter);
        }
    }
}
