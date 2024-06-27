package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.bottomNavigationView;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.animepeak.Activity.ProfileActivity;
import com.example.animepeak.Adapters.AnimeAdapter;

import com.example.animepeak.Utils.NetworkHelper;
import com.example.animepeak.Model.AnimeResponseModel;
import com.example.animepeak.R;
import com.example.animepeak.RestApiClient.ApiInterface;
import com.example.animepeak.RestApiClient.RetrofitHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    AnimeAdapter animeAdapter;
    RecyclerView recyclerView;
    ProgressBar home_loading;
    ImageView more_loading,profile;
    TextView network_error,titleText;
    ArrayList<AnimeResponseModel> animeResponseModelArrayList = new ArrayList<>();

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
        // Make an API request with the updated page number

        RetrofitHelper.getRetrofitHelper().create(ApiInterface.class).getPopularAnime().enqueue(new Callback<List<AnimeResponseModel>>() {
            @Override
            public void onResponse(Call<List<AnimeResponseModel>> call, Response<List<AnimeResponseModel>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    animeResponseModelArrayList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AnimeResponseModel>> call, Throwable throwable) {
                Toast.makeText(getActivity(), "error fetching anime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setSelectedItemId(R.id.home);
        currentPage = 1;

        recyclerView = requireView().findViewById(R.id.home_recycler);
        home_loading = requireView().findViewById(R.id.loading);
        more_loading = requireView().findViewById(R.id.more_loading);
        network_error = requireView().findViewById(R.id.net_error);
        titleText = requireView().findViewById(R.id.home_title);
        profile_card = requireView().findViewById(R.id.profile_card);
        profile = requireView().findViewById(R.id.Profile_pic);

        checkInternetConnection();
        FirebaseApp.initializeApp(requireActivity());


        profile_card.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
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

        animeAdapter = new AnimeAdapter(getContext(),animeResponseModelArrayList);
        recyclerView.setAdapter(animeAdapter);
        // Add a scroll listener to your RecyclerView

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the RecyclerView has reached the end of its scroll
                if (!recyclerView.canScrollVertically(0)) {

                    pos = LayoutManager.findLastVisibleItemPosition();
                    more_loading.setVisibility(View.VISIBLE);
                    Glide.with(requireActivity())
                            .asGif()
                            .load(R.raw.loading_animation)
                            .into(more_loading);

// Get the last position in the adapter
                    int lastPosition = animeAdapter.getItemCount();

// Scroll to the last position
                    recyclerView.smoothScrollToPosition(lastPosition);

                    currentPage++;
                    fetchData(currentPage);
                }
            }
        });

    }

    private void checkInternetConnection() {
        if (NetworkHelper.IsConnected(getContext())){
            network_error.setVisibility(View.GONE);
        }else {
            network_error.setVisibility(View.VISIBLE);
        }
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
          animeResponseModelArrayList.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        animeResponseModelArrayList.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        animeResponseModelArrayList.clear();
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireView().getContext(), 2));
        }
        animeAdapter = new AnimeAdapter(getContext(),animeResponseModelArrayList);
        recyclerView.setAdapter(animeAdapter);
    }
}
