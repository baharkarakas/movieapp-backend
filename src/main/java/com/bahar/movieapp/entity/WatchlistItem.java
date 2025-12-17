package com.bahar.movieapp.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "watchlist_items",
        uniqueConstraints = @UniqueConstraint(
                name = "ux_watchlist_unique",
                columnNames = {"user_id", "category_id", "movie_id"}
        )
)
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public WatchlistItem() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Category getCategory() { return category; }
    public Movie getMovie() { return movie; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setCategory(Category category) { this.category = category; }
    public void setMovie(Movie movie) { this.movie = movie; }
}
