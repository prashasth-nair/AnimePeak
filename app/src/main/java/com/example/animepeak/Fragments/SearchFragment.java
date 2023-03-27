package com.example.animepeak.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;


import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    public static EditText search_Box;
    public static ImageButton search_button;
    public static TextView not_found;
    public static RecyclerView searchView;
    public static SearchAdapter searchAdapter;
    public static ImageView Search_loading;
    public static List<String> Search_TitleUrlList = new ArrayList<>();
    public static List<String> Search_imageUrlList = new ArrayList<>();
    public static List<String> Search_IDList = new ArrayList<>();


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_Box = getView().findViewById(R.id.searchEditText);

        search_Box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Search_TitleUrlList.clear();
                    Search_imageUrlList.clear();
                    Search_IDList.clear();
                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                    String Source = sharedpreferences.getString("Source_Name", "GogoAnime");


                    if (Source.equals("GogoAnime")) {
                        new GogoAnime.GogoAnime_search(getActivity(), isAdded()).execute();
                    } else if (Source.equals("Zoro")) {
                        new Zoro.Zoro_search(getActivity(), isAdded()).execute();
                    } else if (Source.equals("Hanime")) {
                        new Hanime.Hanime_search(getActivity(), isAdded()).execute();
                    }
//


                    return true;
                }
                return false;
            }
        });

        not_found = getView().findViewById(R.id.not_found);
        search_button = getView().findViewById(R.id.searchButton);
        searchView = (RecyclerView) getView().findViewById(R.id.search_recycle);
        Search_loading = (ImageView) getView().findViewById(R.id.loading);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
        }
        searchAdapter = new SearchAdapter(getActivity(), Search_TitleUrlList, Search_imageUrlList, Search_IDList);
        // notify the adapter that the data has changed
        searchAdapter.notifyDataSetChanged();
        searchView.setAdapter(searchAdapter);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search_TitleUrlList.clear();
                Search_imageUrlList.clear();
                Search_IDList.clear();
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                String Source = sharedpreferences.getString("Source_Name", "GogoAnime");


                if (Source.equals("GogoAnime")) {
                    new GogoAnime.GogoAnime_search(getActivity(), isAdded()).execute();
                } else if (Source.equals("Zoro")) {
                    new Zoro.Zoro_search(getActivity(), isAdded()).execute();
                } else if (Source.equals("Hanime")) {
                    new Hanime.Hanime_search(getActivity(), isAdded()).execute();
                }
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));

        } else {
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));

        }
    }

}