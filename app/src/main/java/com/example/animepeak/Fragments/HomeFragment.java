package com.example.animepeak.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

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


import com.example.animepeak.Adapters.MainAdapter;

import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    public static ImageView home_loading;
    public static List<String> Home_TitleUrlList = new ArrayList<>();
    public static List<String> Home_imageUrlList = new ArrayList<>();
    public static List<String> Home_IDList = new ArrayList<>();


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
        home_loading = (ImageView) getView().findViewById(R.id.loading);
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 3));
        }

        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Source = sharedpreferences.getString("Source_Name", "GogoAnime");


        if (Source.equals("GogoAnime")) {
            new GogoAnime.Gogoanime_popular(getActivity(), isAdded()).execute();
        } else if (Source.equals("Zoro")) {
            new Zoro.Zoro_popular(getActivity(), isAdded()).execute();
        }else if (Source.equals("Hanime")) {
            new Hanime.Hanime_popular(getActivity(), isAdded()).execute();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getView() != null) {

            // call the async function here to refresh the data
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            String Source = sharedpreferences.getString("Source_Name", "GogoAnime");

            if (Source.equals("GogoAnime")) {
                Log.d("Item", "Gogo");
                new GogoAnime.Gogoanime_popular(getActivity(), isAdded()).execute();
            } else if (Source.equals("Zoro")) {
                Log.d("Item", "Zoro");
                new Zoro.Zoro_popular(getActivity(), isAdded()).execute();
            }else if (Source.equals("Hanime")) {
                new Hanime.Hanime_popular(getActivity(), isAdded()).execute();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 3));
            mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
            recyclerView.setAdapter(mainAdapter);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
            mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
            recyclerView.setAdapter(mainAdapter);
        }
    }
}
