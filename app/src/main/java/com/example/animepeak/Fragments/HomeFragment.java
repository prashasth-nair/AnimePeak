package com.example.animepeak.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animepeak.Adapters.PopularAnimeAdapter;
import com.example.animepeak.Model.PopularAnimeResponse;
import com.example.animepeak.R;
import com.example.animepeak.RestApiClient.ApiInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    RecyclerView recyclerView;
    private PopularAnimeAdapter popularAnimeAdapter;
    private ProgressBar progressBar;
    private int currentPage = 1;
    private boolean hasNextPage = true;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private TextView noInternet;
    private RelativeLayout relativeLayout;
    ArrayList<PopularAnimeResponse.PopularAnime> popularAnimeArrayList = new ArrayList<>();

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = view.findViewById(R.id.home_recycler);
        progressBar = view.findViewById(R.id.loading);
        noInternet = view.findViewById(R.id.net_error);

        popularAnimeAdapter = new PopularAnimeAdapter(getContext(),popularAnimeArrayList);
        // Get the connectivity manager
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isNetworkAvailable()){
            fetchData(currentPage);
        }else {
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    private boolean isNetworkAvailable() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null&&networkInfo.isAvailable();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh search results when the fragment is resumed
        currentPage++;
        fetchData(currentPage);
        noInternet.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Cancel any ongoing API calls when the fragment is paused
        // This helps to prevent unnecessary network requests
        cancelApiCall();
        noInternet.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up any resources when the fragment is destroyed
        // For example, you can release the Retrofit instance here
        popularAnimeArrayList.clear();
        releaseRetrofitInstance();
        noInternet.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void cancelApiCall() {
        // Cancel any ongoing API calls
        // For example, if you're using Retrofit, you can cancel the request here
    }

    private void releaseRetrofitInstance() {
        // Release the Retrofit instance or any other resources used by the fragment
        // For example, you can call 'retrofit.terminate()' to release the Retrofit instance
    }

    private void fetchData(int page) {
            showProgressBar();
            makeApiCall(page);
            noInternet.setVisibility(View.GONE);

    }
    private void makeApiCall(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-consumet-org-mu.vercel.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        service.getPopularAnime(page).enqueue(new Callback<PopularAnimeResponse>() {
            @Override
            public void onResponse(Call<PopularAnimeResponse> call, Response<PopularAnimeResponse> response) {
                hideProgressBar();
                if (response.isSuccessful()){
                    PopularAnimeResponse popularAnimeResponse = response.body();
                    if(popularAnimeResponse != null){
                        currentPage = popularAnimeResponse.getCurrentPage();
                        hasNextPage = popularAnimeResponse.isHasNextPage();
                        if (!hasNextPage) {
                            return;
                        }
                        popularAnimeArrayList.addAll(popularAnimeResponse.getResults());
                        recyclerView.setAdapter(popularAnimeAdapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                        popularAnimeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularAnimeResponse> call, Throwable throwable) {
                hideProgressBar();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        popularAnimeAdapter=null;
        connectivityManager=null;
        recyclerView=null;
        progressBar=null;
        noInternet=null;
        relativeLayout=null;
    }
}
