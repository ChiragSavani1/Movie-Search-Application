package com.example.androidmobileapp.ui;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private SearchViewModel viewModel;
    private FirebaseFirestore db;
    private Movie currentMovie;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        setupObservers();

        String movieId = getIntent().getStringExtra("MOVIE_ID");
        if (movieId != null) {
            viewModel.getMovieDetails(movieId);
        }

        binding.backButton.setOnClickListener(v -> finish());

        binding.favButton.setOnClickListener(v -> {
            FirebaseUser user = auth.getCurrentUser();

            if (user == null) {
                Toast.makeText(this, "Please login to add favorites", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentMovie != null) {
                Map<String, Object> movieMap = new HashMap<>();
                movieMap.put("imdbID", currentMovie.getImdbID());
                movieMap.put("title", currentMovie.getTitle());
                movieMap.put("year", currentMovie.getYear());
                movieMap.put("studio", currentMovie.getStudio());
                movieMap.put("rating", currentMovie.getRating());
                movieMap.put("posterUrl", currentMovie.getPosterUrl());
                movieMap.put("plot", currentMovie.getPlot());

                // Save in: favourites/{uid}/movies/{imdbID}
                db.collection("favourites")
                        .document(user.getUid())
                        .collection("movies")
                        .document(currentMovie.getImdbID())
                        .set(movieMap)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Added to Your Favourites", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
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
        currentMovie = movie; // store current movie for later use

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