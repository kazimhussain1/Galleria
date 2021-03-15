package com.example.galleria.home.api;

import com.example.galleria.home.api.response.ImageQueryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeApi {

    @GET("api")
    Call<ImageQueryResponse> getImages(@Query(value = "q", encoded = true) String queryString);

    @GET("api")
    Call<ImageQueryResponse> getImagesPaginated(@Query(value = "q", encoded = true) String queryString, @Query(value = "page") int pageNo);
}
