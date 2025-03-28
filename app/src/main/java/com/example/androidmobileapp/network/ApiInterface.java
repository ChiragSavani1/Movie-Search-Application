package com.example.androidmobileapp.network;

import com.example.androidmobileapp.models.Movie;
import com.example.androidmobileapp.models.MovieSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/")
    Call<MovieSearchResult> searchMovies(
            @Query("s") String query,
            @Query("type") String type,
            @Query("apikey") String apiKey
    );

    @GET("/")
    Call<Movie> getMovieDetails(
            @Query("i") String imdbId,
            @Query("apikey") String apiKey
    );
}