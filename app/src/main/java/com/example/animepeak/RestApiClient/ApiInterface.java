package com.example.animepeak.RestApiClient;

import com.example.animepeak.Model.AnimeResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/anime/gogoanime/popular")
    Call<AnimeResponseModel> getPopularAnime();
}
