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

    @GET("breeds")
    Call<List<breedclass>> getbreedlist();

    @GET("search")
    Call<List<catclass>> getCategory(@Query("category_ids") int catid);

    @GET("search")
    Call<List<breedinfoclass>> getBreed(@Query("breed_ids") String breedid);
}
