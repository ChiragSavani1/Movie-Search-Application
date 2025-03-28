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

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        setupRecyclerView();
        setupObservers();

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
        adapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(SearchActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getImdbID());
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