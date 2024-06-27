package com.example.animepeak.Fragments;

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

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.animepeak.Adapters.SearchAdapter;
import com.example.animepeak.Model.AnimeResponseModel;
import com.example.animepeak.R;
import com.example.animepeak.RestApiClient.ApiInterface;
import com.example.animepeak.RestApiClient.RetrofitHelper;
import com.mancj.materialsearchbar.MaterialSearchBar;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    MaterialSearchBar searchBar;
    TextView not_found;
    RecyclerView searchView;
    SearchAdapter searchAdapter;
    ProgressBar Search_loading;
    List<AnimeResponseModel> animeInfoModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

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
                animeInfoModelList.clear();
                String query = searchBar.getText();
                RetrofitHelper.getRetrofitHelper().create(ApiInterface.class).getAllAnime().enqueue(new Callback<List<AnimeResponseModel>>() {
                    @Override
                    public void onResponse(Call<List<AnimeResponseModel>> call, Response<List<AnimeResponseModel>> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            animeInfoModelList.addAll(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AnimeResponseModel>> call, Throwable throwable) {

                    }
                });
            }

            @Override
            public void onButtonClicked(int buttonCode) {

                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    animeInfoModelList.clear();
                    SearchAdapter searchAdapter = new SearchAdapter(getActivity(),animeInfoModelList);
                    // notify the adapter that the data has changed
                    searchAdapter.notifyDataSetChanged();
                    searchView.setAdapter(searchAdapter);
                    searchBar.setNavButtonEnabled(false);
                }
            }
        });

        not_found = getView().findViewById(R.id.not_found);

        searchView = getView().findViewById(R.id.search_recycle);
        Search_loading = getView().findViewById(R.id.loading);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            searchView.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
        }
        searchAdapter = new SearchAdapter(getActivity(),animeInfoModelList);
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
       animeInfoModelList.clear();
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}