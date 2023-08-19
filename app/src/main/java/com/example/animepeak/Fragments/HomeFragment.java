package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.bottomNavigationView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.animepeak.Activity.Profile;
import com.example.animepeak.Adapters.MainAdapter;

import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
import com.example.animepeak.Sources.Zoro;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    public static ImageView home_loading;
    public static TextView network_error;
    TextView titleText;
    public static List<String> Home_TitleUrlList = new ArrayList<>();
    public static List<String> Home_imageUrlList = new ArrayList<>();
    public static List<String> Home_IDList = new ArrayList<>();
    public static GogoAnime.Gogoanime_popular gogoanime_popular;
    public static Zoro.Zoro_popular zoro_popular;

    CardView profile_card;
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
        titleText = (TextView) getView().findViewById(R.id.home_title);
        profile_card = (CardView) getView().findViewById(R.id.profile_card);

        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (!Home_TitleUrlList.isEmpty()) {
                        // The user has started scrolling
                        int topVisiblePosition = ((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                                .findFirstVisibleItemPosition();
                        recyclerView.setHasFixedSize(true);

                        if (topVisiblePosition == 0) {

                            animateTextSizeChange(titleText, 34);
                            animateProfileSizeChange(profile_card, 85, 85);
                        } else {

                            animateTextSizeChange(titleText, 20);
                            animateProfileSizeChange(profile_card, 65, 65);
                        }
                    }
                }
            }
        });

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
        }

    }


    private void animateProfileSizeChange(View view, int newWidth, int newHeight) {
        ValueAnimator widthAnimator = ValueAnimator.ofInt(view.getWidth(), newWidth);
        widthAnimator.setDuration(300);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                view.getLayoutParams().width = width;
                view.requestLayout();
            }
        });
        widthAnimator.start();

        ValueAnimator heightAnimator = ValueAnimator.ofInt(view.getHeight(), newHeight);
        heightAnimator.setDuration(300);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                view.getLayoutParams().height = height;
                view.requestLayout();
            }
        });
        heightAnimator.start();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // Fragment is currently visible
            // Perform actions or update UI accordingly
        } else {
            // Fragment is not currently visible
            // Perform actions or update UI accordingly
            Log.d("visible","false");
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
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // Fragment is not currently visible
            // Perform actions or update UI accordingly
            Home_TitleUrlList.clear();
            Home_imageUrlList.clear();
            Home_IDList.clear();
            
            if (gogoanime_popular != null  ) {

                gogoanime_popular.cancel(true);
                gogoanime_popular = null;
            }
            if (zoro_popular != null ) {
                zoro_popular.cancel(true);
                zoro_popular = null;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null  ) {
       
            gogoanime_popular.cancel(true);
            gogoanime_popular = null;
        }
        if (zoro_popular != null ) {
            zoro_popular.cancel(true);
            zoro_popular = null;
        }


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
    @Override
    public void onDetach() {
        super.onDetach();
        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null  ) {
        
            gogoanime_popular.cancel(true);
            gogoanime_popular = null;
        }
        if (zoro_popular != null ) {
            zoro_popular.cancel(true);
            zoro_popular = null;
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
