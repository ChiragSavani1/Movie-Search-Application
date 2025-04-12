package com.example.androidmobileapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidmobileapp.adapters.MovieAdapter;
import com.example.androidmobileapp.databinding.ActivityFavoriteMoviesBinding;
import com.example.androidmobileapp.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private ActivityFavoriteMoviesBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MovieAdapter adapter;
    private ArrayList<Movie> favMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        favMovies = new ArrayList<>();

        adapter = new MovieAdapter(favMovies, movie -> {
            Intent intent = new Intent(this, FavoriteMovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getImdbID());
            startActivity(intent);
        });

        binding.favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.favRecyclerView.setAdapter(adapter);

        binding.navSearchButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        binding.navFavButton.setOnClickListener(v -> {
            Toast.makeText(this, "You're already on Favourites", Toast.LENGTH_SHORT).show();
        });

        fetchFavorites(currentUser.getUid());
    }

    private void fetchFavorites(String uid) {
        db.collection("favourites").document(uid).collection("movies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favMovies.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject(Movie.class);
                        favMovies.add(movie);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching favorites", Toast.LENGTH_SHORT).show());
    }
}
