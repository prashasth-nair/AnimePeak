package com.example.animepeak.RestApiClient;

import com.example.animepeak.Model.AnimeInfoModel;
import com.example.animepeak.Model.PopularAnimeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/anime/gogoanime/popular")
    Call<PopularAnimeResponse> getPopularAnime(@Query("page") int page);

    @GET("/anime/gogoanime/info/{id}")
    Call<AnimeInfoModel> getAnimeInfo(@Path("id")String id);

    @GET("/anime/gosoanime/anime-list")
    Call<List<PopularAnimeResponse>> getAllAnime();
}
