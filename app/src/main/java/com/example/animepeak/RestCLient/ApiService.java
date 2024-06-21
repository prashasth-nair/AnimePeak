package com.example.animepeak.RestCLient;

import com.example.animepeak.Model.AnimeResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/anime/gogoanime/popular")
    Call<AnimeResponseModel> getPopular();
}
