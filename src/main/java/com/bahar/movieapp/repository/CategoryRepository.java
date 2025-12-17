package com.bahar.movieapp.repository;

import com.bahar.movieapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser_Email(String email);
    Optional<Category> findByIdAndUser_Email(Long id, String email);
    boolean existsByUser_EmailAndName(String email, String name);
}
