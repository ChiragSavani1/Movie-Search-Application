package com.example.androidmobileapp.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.androidmobileapp.models.Movie;
import com.example.androidmobileapp.models.MovieSearchResult;
import com.example.androidmobileapp.network.ApiClient;
import com.example.androidmobileapp.network.ApiInterface;
import com.example.androidmobileapp.network.ApiResponse;
import com.example.androidmobileapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiInterface apiService = ApiClient.getClient();

    public void searchMovies(String query, MutableLiveData<ApiResponse<List<Movie>>> liveData) {
        // Change this line to include type=movie
        Call<MovieSearchResult> call = apiService.searchMovies(query, "movie", Constants.API_KEY);

        call.enqueue(new Callback<MovieSearchResult>() {
            @Override
            public void onResponse(Call<MovieSearchResult> call, Response<MovieSearchResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(new ApiResponse<>(response.body().getMovies()));
                } else {
                    liveData.setValue(new ApiResponse<>("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResult> call, Throwable t) {
                liveData.setValue(new ApiResponse<>(t.getMessage()));
            }
        });
    }

    public void getMovieDetails(String imdbId, MutableLiveData<ApiResponse<Movie>> liveData) {
        Call<Movie> call = apiService.getMovieDetails(imdbId, Constants.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(new ApiResponse<>(response.body()));
                } else {
                    liveData.setValue(new ApiResponse<>("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                liveData.setValue(new ApiResponse<>(t.getMessage()));
            }
        });
    }
}