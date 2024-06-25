package com.example.animepeak.RestApiClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final String BASE_URL = "https://api-consumet-org-mu.vercel.app";
    static Retrofit retrofitHelper;

    public RetrofitHelper() {
    }

    public static Retrofit getRetrofitHelper() {
        if(retrofitHelper==null){
            retrofitHelper = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitHelper;
    }
}
