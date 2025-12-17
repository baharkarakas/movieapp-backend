package com.bahar.movieapp.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "liked_movies",
        uniqueConstraints = @UniqueConstraint(name = "ux_liked_unique", columnNames = {"user_id", "movie_id"})
)
public class LikedMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public LikedMovie() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Movie getMovie() { return movie; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setMovie(Movie movie) { this.movie = movie; }
}
