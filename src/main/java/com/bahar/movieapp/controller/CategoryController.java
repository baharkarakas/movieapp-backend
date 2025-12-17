package com.bahar.movieapp.controller;

import com.bahar.movieapp.dto.CategoryDtos.*;
import com.bahar.movieapp.entity.Category;
import com.bahar.movieapp.entity.User;
import com.bahar.movieapp.repository.CategoryRepository;
import com.bahar.movieapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // READ (my categories)
    @GetMapping
    public List<CategoryResponse> myCategories(Authentication auth) {
        String email = auth.getName();
        return categoryRepository.findAllByUser_Email(email)
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryRequest req, Authentication auth) {
        String email = auth.getName();

        if (categoryRepository.existsByUser_EmailAndName(email, req.name)) {
            return ResponseEntity.badRequest().body("Category name already exists");
        }

        User user = userRepository.findByEmail(email).orElseThrow();

        Category c = new Category();
        c.setName(req.name);
        c.setUser(user);

        Category saved = categoryRepository.save(c);
        return ResponseEntity.ok(new CategoryResponse(saved.getId(), saved.getName()));
    }

    // UPDATE (only if it belongs to me)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody UpdateCategoryRequest req,
                                    Authentication auth) {
        String email = auth.getName();

        Category c = categoryRepository.findByIdAndUser_Email(id, email)
                .orElse(null);

        if (c == null) {
            return ResponseEntity.status(404).body("Category not found");
        }

        c.setName(req.name);
        Category saved = categoryRepository.save(c);
        return ResponseEntity.ok(new CategoryResponse(saved.getId(), saved.getName()));
    }

    // DELETE (only if it belongs to me)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();

        Category c = categoryRepository.findByIdAndUser_Email(id, email)
                .orElse(null);

        if (c == null) {
            return ResponseEntity.status(404).body("Category not found");
        }

        categoryRepository.delete(c);
        return ResponseEntity.noContent().build();
    }
}
