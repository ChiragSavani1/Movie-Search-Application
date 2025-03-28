package com.example.androidmobileapp.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("imdbID")
    private String imdbID;

    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Production")
    private String studio;

    @SerializedName("imdbRating")
    private String rating;

    @SerializedName("Poster")
    private String posterUrl;

    @SerializedName("Plot")
    private String plot;


    public Movie(String imdbID, String title, String year, String studio, String rating,
                 String posterUrl, String plot) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.studio = studio;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.plot = plot;

    }

    // Getters
    public String getImdbID() { return imdbID; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getStudio() { return studio; }
    public String getRating() { return rating; }
    public String getPosterUrl() { return posterUrl; }
    public String getPlot() { return plot; }


    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}