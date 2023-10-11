package com.example.animepeak.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.mancj.materialsearchbar.MaterialSearchBar;


import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    public  static MaterialSearchBar searchBar;
    public static TextView not_found;
    public static RecyclerView searchView;
    public static SearchAdapter searchAdapter;
    public static ImageView Search_loading;
    public static List<String> Search_TitleUrlList = new ArrayList<>();
    public static List<String> Search_imageUrlList = new ArrayList<>();
    public static List<String> Search_IDList = new ArrayList<>();
    GogoAnime.GogoAnime_search gogoAnime_search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBar = getView().findViewById(R.id.searchBar);


        //enable searchbar callbacks
        searchBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchBar.setNavButtonEnabled(true);
                searchBar.openSearch();
                return true;
            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Search_TitleUrlList.clear();
                Search_imageUrlList.clear();
                Search_IDList.clear();


                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                String Source = sharedpreferences.getString("Source_Name", "GogoAnime");
                String query = searchBar.getText();



                if (Source.equals("GogoAnime")) {
                    gogoAnime_search = new GogoAnime.GogoAnime_search(getActivity(), isAdded(),query);
                    gogoAnime_search.execute();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {

                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {

                    Search_TitleUrlList.clear();
                    Search_imageUrlList.clear();
                    Search_IDList.clear();
                    SearchAdapter searchAdapter = new SearchAdapter(getActivity(), Search_TitleUrlList, Search_imageUrlList, Search_IDList);
                    // notify the adapter that the data has changed
                    searchAdapter.notifyDataSetChanged();
                    searchView.setAdapter(searchAdapter);
                    searchBar.setNavButtonEnabled(false);
                }
            }
        });

        not_found = getView().findViewById(R.id.not_found);

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

    @Override
    public void onDetach() {
        super.onDetach();
        Search_TitleUrlList.clear();
        Search_imageUrlList.clear();
        Search_IDList.clear();
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}