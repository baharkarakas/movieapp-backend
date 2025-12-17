package com.bahar.movieapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String director;

    private Integer year;

    @Column(name = "poster_url", length = 1000)
    private String posterUrl;

    public Movie() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public Integer getYear() { return year; }
    public String getPosterUrl() { return posterUrl; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDirector(String director) { this.director = director; }
    public void setYear(Integer year) { this.year = year; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
