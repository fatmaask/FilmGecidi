package com.fatmaasik.filmgecidi;

/**
 * Created by Asus on 9.02.2017.
 */

public class Movie {
    private String id;
    private String title;
    private String year;
    private String image;
    private String video;
    private String rating;
    private String country;
    private String runtime;
    private String actor;
    private String synopsis;
    private String director;
    private String genre;

    public Movie(){

    }

    public Movie(String id, String title, String image, String year, String video, String rating, String country, String runtime, String actor, String synopsis, String director, String genre) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.year = year;
        this.video = video;
        this.rating = rating;
        this.country = country;
        this.runtime = runtime;
        this.actor = actor;
        this.synopsis = synopsis;
        this.director = director;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}