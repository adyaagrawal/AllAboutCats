package com.example.allaboutcats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CATAPI {

    @GET("search")
    Call<List<Random>> getRandom();

    @GET("categories")
    Call<List<categorylist>> getcategorylist();

    @GET("search")
    Call<List<catclass>> getCategory(@Query("category_ids") int catid);

}
