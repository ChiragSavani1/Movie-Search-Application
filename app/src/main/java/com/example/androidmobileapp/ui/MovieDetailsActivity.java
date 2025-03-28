package com.example.androidmobileapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidmobileapp.R;
import com.example.androidmobileapp.databinding.ActivityMovieDetailsBinding;
import com.example.androidmobileapp.models.Movie;
import com.example.androidmobileapp.network.ApiResponse;
import com.example.androidmobileapp.viewmodel.SearchViewModel;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        setupObservers();

        String movieId = getIntent().getStringExtra("MOVIE_ID");
        if (movieId != null) {
            viewModel.getMovieDetails(movieId);
        }

        binding.backButton.setOnClickListener(v -> finish());
    }

    private void setupObservers() {
        viewModel.getMovieDetails().observe(this, response -> {
            if (response.isSuccessful()) {
                Movie movie = response.getData();
                if (movie != null) {
                    displayMovieDetails(movie);
                }
            } else {
                Toast.makeText(this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMovieDetails(Movie movie) {
        // Add error handling for empty poster URL
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(binding.posterImageView);
        } else {
            binding.posterImageView.setImageResource(R.drawable.ic_movie_placeholder);
        }

        binding.titleTextView.setText(movie.getTitle());
        binding.yearTextView.setText(getString(R.string.year_format, movie.getYear()));
        binding.ratingTextView.setText(getString(R.string.rating_format, movie.getRating()));
        binding.studioTextView.setText(getString(R.string.studio_format, movie.getStudio()));
        binding.plotTextView.setText(movie.getPlot());
    }
}