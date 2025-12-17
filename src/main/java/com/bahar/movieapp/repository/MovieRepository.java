package com.bahar.movieapp.repository;

import com.bahar.movieapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
