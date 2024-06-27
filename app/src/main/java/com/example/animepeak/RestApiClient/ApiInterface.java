package com.example.animepeak.RestApiClient;

import com.example.animepeak.Model.AnimeInfoModel;
import com.example.animepeak.Model.AnimeResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("/anime/gogoanime/popular")
    Call<List<AnimeResponseModel>> getPopularAnime();

    @GET("/anime/gogoanime/info/{id}")
    Call<AnimeInfoModel> getAnimeInfo(@Path("id")String id);

    @GET("/anime/gosoanime/anime-list")
    Call<List<AnimeResponseModel>> getAllAnime();
}
