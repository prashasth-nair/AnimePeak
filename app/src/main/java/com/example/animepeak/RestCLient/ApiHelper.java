package com.example.animepeak.RestCLient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://consumet-five-lemon.vercel.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);
}
