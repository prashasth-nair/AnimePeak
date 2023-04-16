package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Adapters.FavAdapter.fav_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animepeak.Adapters.FavAdapter;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    public static RecyclerView fav_recycler;
    public static TextView no_fav;
    String Source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fav_recycler = getView().findViewById(R.id.fav_recycler);
        no_fav = getView().findViewById(R.id.no_fav);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Source = sharedpreferences.getString("Source_Name", "GogoAnime");
        fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        if (fav_list.size()==0){
            no_fav.setVisibility(View.VISIBLE);
        }else {
            no_fav.setVisibility(View.GONE);
        }
        countSource();

    }

    public ArrayList<Fav_object> temp_fav_list(){
        // Create a temporary ArrayList to store items with a source of "gogo"
        ArrayList<Fav_object> tempArray = new ArrayList<>();

        // Loop through each item in fav_list
        for (Fav_object item : fav_list) {
            // Get the source string for this item
            String source = item.getFavSource();

            // Check if the source for this item is "gogo"
            if (source.equals(Source)) {
                // Add this item to the temporary ArrayList
                tempArray.add(item);
            }
        }
        return tempArray;
    }

    @Override
    public void onResume() {
        super.onResume();
        countSource();
    }
    public void countSource(){
        int gogoCount = 0;
        int zoroCount = 0;
        int hanimeCount = 0;
        // Loop through each item in fav_list
        for (Fav_object item : fav_list) {
            // Get the source string for this item
            String source = item.getFavSource();

            // Check if the source for this item is "gogo"
            if (source.equals("GogoAnime")) {
                // Increment the count for "gogo"
                gogoCount++;
            }else if (source.equals("Zoro")) {
                // Increment the count for "Zoro"
                zoroCount++;
            }else if (source.equals("Hanime")) {
                // Increment the count for "Hanime"
                hanimeCount++;
            }
        }
        ArrayList<Fav_object> temp_list = temp_fav_list();
        FavAdapter favAdapter = new FavAdapter(getActivity(), temp_list,Source,gogoCount,zoroCount,hanimeCount);
        fav_recycler.setAdapter(favAdapter);
    }
}