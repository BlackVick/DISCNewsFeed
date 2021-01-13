package com.eswofficial.discnews.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    //fetch news
    @GET("top-headlines")
    Call<NewsResponse> fetchNews(
            @Query("sources") String sources,
            @Query("apiKey") String apiKey
    );

}
