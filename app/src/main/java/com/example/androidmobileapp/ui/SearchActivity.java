package com.example.androidmobileapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidmobileapp.adapters.MovieAdapter;
import com.example.androidmobileapp.databinding.ActivitySearchBinding;
import com.example.androidmobileapp.models.Movie;
import com.example.androidmobileapp.viewmodel.SearchViewModel;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private MovieAdapter adapter;
    private final ArrayList<Movie> favMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        setupRecyclerView();
        setupObservers();

        binding.navSearchButton.setOnClickListener(v -> {
            // Already on SearchActivity, maybe show a toast or do nothing
            Toast.makeText(this, "You're already on Search", Toast.LENGTH_SHORT).show();
        });

        binding.navFavButton.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoriteMoviesActivity.class));
        });

        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchField.getText().toString().trim();
            if (!query.isEmpty()) {
                viewModel.searchMovies(query);
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(favMovies, movie -> {
            Intent intent = new Intent(SearchActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getImdbID());
            intent.putExtra("FROM", "search");
            startActivity(intent);

        });


        binding.moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.moviesRecyclerView.setAdapter(adapter);
        binding.moviesRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupObservers() {
        viewModel.getSearchResults().observe(this, response -> {
            if (response.isSuccessful()) {
                List<Movie> movies = response.getData();
                if (movies != null && !movies.isEmpty()) {
                    adapter.setMovies(movies);
                } else {
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}