package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.bottomNavigationView;

<<<<<<< HEAD

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
=======
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
import android.content.res.Configuration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
=======
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.Profile;
import com.example.animepeak.Adapters.MainAdapter;

import com.example.animepeak.R;
import com.example.animepeak.Sources.GogoAnime;
<<<<<<< HEAD
import com.example.animepeak.Sources.Zoro;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
=======
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)


import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Objects;


public class HomeFragment extends Fragment {
=======

@SuppressLint("StaticFieldLeak")
public class HomeFragment extends Fragment {

>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    public static ImageView home_loading;
    public static TextView network_error;
    TextView titleText;
    public ImageView profile;
    public static List<String> Home_TitleUrlList = new ArrayList<>();
    public static List<String> Home_imageUrlList = new ArrayList<>();
    public static List<String> Home_IDList = new ArrayList<>();
    public static GogoAnime.Gogoanime_popular gogoanime_popular;
<<<<<<< HEAD
    public static Zoro.Zoro_popular zoro_popular;

    CardView profile_card;
    String Source;
    private FirebaseAuth mAuth;
    public Uri personPhoto;
=======

    CardView profile_card;
    public Uri personPhoto;
    private int currentPage = 1;
    public static GridLayoutManager LayoutManager;
    public static boolean isLoading = false;

    public static int pos = 0;
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)

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
<<<<<<< HEAD
        mAuth = FirebaseAuth.getInstance();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
=======
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
        if (acct != null) {
            personPhoto = acct.getPhotoUrl();
            Glide.with(this)
                    .load(personPhoto)
                    .into(profile);
<<<<<<< HEAD
        }else{
=======
        } else {
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
            Glide.with(this)
                    .load(R.raw.boy1)
                    .into(profile);
        }
    }


<<<<<<< HEAD
=======
    private void fetchData(int page) {
        isLoading = true;

        // Make an API request with the updated page number
        String apiUrl = "https://api.consumet.org/anime/gogoanime/top-airing?page=" + page;

        gogoanime_popular = new GogoAnime.Gogoanime_popular(getActivity(), isAdded(), apiUrl);

        gogoanime_popular.execute();
    }
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setSelectedItemId(R.id.home);

<<<<<<< HEAD
        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);
        home_loading = (ImageView) getView().findViewById(R.id.loading);
        network_error = (TextView) getView().findViewById(R.id.net_error);
        titleText = (TextView) getView().findViewById(R.id.home_title);
        profile_card = (CardView) getView().findViewById(R.id.profile_card);
        profile = getView().findViewById(R.id.Profile_pic);

        FirebaseApp.initializeApp(getActivity());


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
=======
        recyclerView = requireView().findViewById(R.id.home_recycler);
        home_loading = requireView().findViewById(R.id.loading);
        network_error = requireView().findViewById(R.id.net_error);
        titleText = requireView().findViewById(R.id.home_title);
        profile_card = requireView().findViewById(R.id.profile_card);
        profile = requireView().findViewById(R.id.Profile_pic);

        FirebaseApp.initializeApp(requireActivity());


        profile_card.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Profile.class);
            startActivity(intent);
        });
        int orientation = getResources().getConfiguration().orientation;

        LayoutManager = null;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            LayoutManager = new GridLayoutManager(requireView().getContext(), 2);
            recyclerView.setLayoutManager(LayoutManager);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            LayoutManager = new GridLayoutManager(requireView().getContext(), 4);
            recyclerView.setLayoutManager(LayoutManager);
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
        }

        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);

<<<<<<< HEAD
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
=======
        // Add a scroll listener to your RecyclerView

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the RecyclerView has reached the end of its scroll
                if (!recyclerView.canScrollVertically(0)) {

                    pos = LayoutManager.findLastVisibleItemPosition();

                    currentPage++;
                    fetchData(currentPage);


                }
            }
        });


// Make the initial API request
        fetchData(currentPage);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getView() != null) {

            // Make the initial API request
            fetchData(currentPage);
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
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
<<<<<<< HEAD
            
            if (gogoanime_popular != null  ) {

//                gogoanime_popular.cancel(true);
//                gogoanime_popular = null;
            }
            if (zoro_popular != null ) {
                zoro_popular.cancel(true);
                zoro_popular = null;
            }
=======

>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
<<<<<<< HEAD
        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null  ) {
       
//            gogoanime_popular.cancel(true);
            gogoanime_popular = null;
        }
        if (zoro_popular != null ) {
            zoro_popular.cancel(true);
            zoro_popular = null;
        }
=======
        currentPage = 0;
        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null) {

            gogoanime_popular = null;
        }
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)


    }

<<<<<<< HEAD

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
=======
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
    @Override
    public void onDetach() {
        super.onDetach();
        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
<<<<<<< HEAD
        if (gogoanime_popular != null  ) {
        
//            gogoanime_popular.cancel(true);
            gogoanime_popular = null;
        }
        if (zoro_popular != null ) {
            zoro_popular.cancel(true);
            zoro_popular = null;
        }
=======
        if (gogoanime_popular != null) {
            gogoanime_popular = null;
        }
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)

    }


    @Override
<<<<<<< HEAD
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
=======
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 2));
        }
        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);
>>>>>>> 5ae3732 (Removed Zoro,Added infinte scroll (#23),Changed from few depreciated api to latest,Fixed few bugs)
    }
}
