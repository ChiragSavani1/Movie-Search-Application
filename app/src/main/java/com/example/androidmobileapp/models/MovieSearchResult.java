package com.example.androidmobileapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResult {
    @SerializedName("Search")
    private List<Movie> movies;
    @SerializedName("totalResults")
    private String totalResults;
    @SerializedName("Response")
    private String response;
    @SerializedName("Error")
    private String error;

    // Constructor
    public MovieSearchResult(List<Movie> movies, String totalResults, String response, String error) {
        this.movies = movies;
        this.totalResults = totalResults;
        this.response = response;
        this.error = error;
    }

    // Getters
    public List<Movie> getMovies() {
        return movies;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }


    public boolean isSuccessful() {
        return "True".equalsIgnoreCase(response);
    }
}