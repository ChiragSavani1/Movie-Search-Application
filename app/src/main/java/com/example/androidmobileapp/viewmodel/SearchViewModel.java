package com.example.androidmobileapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidmobileapp.models.Movie;
import com.example.androidmobileapp.network.ApiResponse;
import com.example.androidmobileapp.repository.MovieRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final MovieRepository repository = new MovieRepository();
    private final MutableLiveData<ApiResponse<List<Movie>>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<Movie>> movieDetails = new MutableLiveData<>();

    public LiveData<ApiResponse<List<Movie>>> getSearchResults() {
        return searchResults;
    }

    public LiveData<ApiResponse<Movie>> getMovieDetails() {
        return movieDetails;
    }

    public void searchMovies(String query) {
        repository.searchMovies(query, searchResults);
    }

    public void getMovieDetails(String imdbId) {
        repository.getMovieDetails(imdbId, movieDetails);
    }
}