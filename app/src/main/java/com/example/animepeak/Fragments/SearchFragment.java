package com.example.animepeak.Fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.Functions.suggest_object;
import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Hanime;
import com.example.animepeak.Sources.Zoro;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;


import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {
    public  static MaterialSearchBar searchBar;
    public static TextView not_found;
    public static RecyclerView searchView;
    public static SearchAdapter searchAdapter;
    public static ImageView Search_loading;
    public static List<String> Search_TitleUrlList = new ArrayList<>();
    public static List<String> Search_imageUrlList = new ArrayList<>();
    public static List<String> Search_IDList = new ArrayList<>();
    GogoAnime.GogoAnime_search gogoAnime_search;
    Zoro.Zoro_search zoro_search;
    Hanime.Hanime_search hanime_search;



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
        searchBar = getView().findViewById(R.id.searchBar);


        //enable searchbar callbacks

        searchBar.setOnSearchActionListener(this);

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
        } else if (Source.equals("Zoro")) {
            zoro_search = new Zoro.Zoro_search(getActivity(), isAdded(),query);
            zoro_search.execute();
        } else if (Source.equals("Hanime")) {
            hanime_search = new Hanime.Hanime_search(getActivity(), isAdded(),query);
            hanime_search.execute();
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
        if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
            Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            Search_TitleUrlList.clear();
            Search_imageUrlList.clear();
            Search_IDList.clear();
            SearchAdapter searchAdapter = new SearchAdapter(getActivity(), Search_TitleUrlList, Search_imageUrlList, Search_IDList);
            // notify the adapter that the data has changed
            searchAdapter.notifyDataSetChanged();
            searchView.setAdapter(searchAdapter);
        }
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



        if (gogoAnime_search != null) {
            gogoAnime_search.cancel(true);
        }
        if (zoro_search != null) {
            zoro_search.cancel(true);
        }
        if (zoro_search != null) {
            zoro_search.cancel(true);
        }
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}