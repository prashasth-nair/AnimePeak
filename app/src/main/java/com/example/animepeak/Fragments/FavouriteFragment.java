package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Adapters.FavAdapter.fav_activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animepeak.Adapters.FavAdapter;
import com.example.animepeak.Adapters.MainAdapter;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;

import java.util.ArrayList;
import java.util.Objects;

public class FavouriteFragment extends Fragment {
    public static RecyclerView fav_recycler;
    public static TextView no_fav;
    TextView FavTitle;
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
        FavTitle = getView().findViewById(R.id.fav_title);

        fav_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topVisiblePosition = ((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                        .findFirstVisibleItemPosition();
                if (dy > 0) {
                    // The user has scrolled down, so shrink the title text
                    animateTextSizeChange(FavTitle, 20);
                }


                if (topVisiblePosition == 0) {
                    // The user has scrolled to the top, so expand the title text
                    animateTextSizeChange(FavTitle, 34);
                }
            }
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Source = sharedpreferences.getString("Source_Name", "GogoAnime");
        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
        }

        if (fav_list.size()==0){
            no_fav.setVisibility(View.VISIBLE);
        }else {
            no_fav.setVisibility(View.GONE);
        }
        countSource();

    }
    private void animateTextSizeChange(final TextView textView, final int newSize) {

        ValueAnimator animator = ValueAnimator.ofFloat(textView.getTextSize(), convertDpToPixel(newSize, requireActivity()));
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
            }
        });
        animator.start();
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
            countSource();
        } else {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
            countSource();
        }
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