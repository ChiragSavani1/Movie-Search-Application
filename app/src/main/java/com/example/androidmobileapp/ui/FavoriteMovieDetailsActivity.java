package com.example.androidmobileapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.androidmobileapp.databinding.ActivityFavoriteMovieDetailsBinding;
import com.example.androidmobileapp.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class FavoriteMovieDetailsActivity extends AppCompatActivity {

    private ActivityFavoriteMovieDetailsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        movieId = getIntent().getStringExtra("MOVIE_ID");

        loadMovieDetails();

        binding.updateButton.setOnClickListener(v -> updateDescription());
        binding.deleteButton.setOnClickListener(v -> deleteDescription());
        binding.backButton.setOnClickListener(v -> finish());
    }

    private void loadMovieDetails() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("favourites").document(uid)
                .collection("movies").document(movieId)
                .get()
                .addOnSuccessListener(doc -> {
                    Movie movie = doc.toObject(Movie.class);
                    if (movie != null) {
                        Glide.with(this)
                                .load(movie.getPosterUrl())
                                .placeholder(android.R.drawable.ic_menu_report_image)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(binding.posterImageView);
                        binding.titleTextView.setText(movie.getTitle());
                        binding.descriptionTextView.setText(movie.getPlot());
                    }
                });
    }

    private void updateDescription() {
        String newDesc = binding.editDescriptionEditText.getText().toString();
        if (newDesc.isEmpty()) {
            Toast.makeText(this, "Enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        db.collection("favourites").document(uid)
                .collection("movies").document(movieId)
                .update("plot", newDesc)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    binding.descriptionTextView.setText(newDesc);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show());
    }

    private void deleteDescription() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("favourites").document(uid)
                .collection("movies").document(movieId)
                .update("plot", "")
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    binding.descriptionTextView.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
    }
}
