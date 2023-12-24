package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.bottomNavigationView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class HomeFragment extends Fragment {

    public static MainAdapter mainAdapter;
    public static RecyclerView recyclerView;
    public static ImageView home_loading;
    public static ImageView more_loading;
    public static TextView network_error;
    TextView titleText;
    public ImageView profile;
    public static List<String> Home_TitleUrlList = new ArrayList<>();
    public static List<String> Home_imageUrlList = new ArrayList<>();
    public static List<String> Home_IDList = new ArrayList<>();
    public static GogoAnime.Gogoanime_popular gogoanime_popular;

    CardView profile_card;
    public Uri personPhoto;
    private int currentPage;
    public static GridLayoutManager LayoutManager;
    public static boolean isLoading = false;

    public static int pos = 0;

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
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (acct != null) {
            personPhoto = acct.getPhotoUrl();
            Glide.with(this)
                    .load(personPhoto)
                    .into(profile);
        } else {
            Glide.with(this)
                    .load(R.raw.boy1)
                    .into(profile);
        }
    }


    private void fetchData(int page) {
        isLoading = true;
        boolean is_more = false;
        // Make an API request with the updated page number

        if (page>1){
            is_more = true;
        }

        gogoanime_popular = new GogoAnime.Gogoanime_popular(getActivity(), isAdded(),is_more,page);

        gogoanime_popular.execute();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerView = requireView().findViewById(R.id.home_recycler);
        home_loading = requireView().findViewById(R.id.loading);
        more_loading = requireView().findViewById(R.id.more_loading);
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
        }

        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);
        currentPage = 1;
        // Make the initial API request
        fetchData(currentPage);

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

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getView() != null) {

            fetchData(currentPage);
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


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null) {
            gogoanime_popular.cancel();
            gogoanime_popular = null;

        }


    }

    @Override
    public void onDetach() {
        super.onDetach();

        Home_TitleUrlList.clear();
        Home_imageUrlList.clear();
        Home_IDList.clear();
        if (gogoanime_popular != null) {
            gogoanime_popular.cancel();
            gogoanime_popular = null;

        }

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 2));
        }
        mainAdapter = new MainAdapter(getActivity(), Home_TitleUrlList, Home_imageUrlList, Home_IDList);
        recyclerView.setAdapter(mainAdapter);
    }
}
