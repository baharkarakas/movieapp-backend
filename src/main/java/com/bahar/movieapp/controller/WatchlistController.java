package com.bahar.movieapp.controller;

import com.bahar.movieapp.dto.WatchlistDtos.*;
import com.bahar.movieapp.entity.*;
import com.bahar.movieapp.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistItemRepository watchlistRepo;
    private final CategoryRepository categoryRepo;
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;

    public WatchlistController(WatchlistItemRepository watchlistRepo,
                               CategoryRepository categoryRepo,
                               MovieRepository movieRepo,
                               UserRepository userRepo) {
        this.watchlistRepo = watchlistRepo;
        this.categoryRepo = categoryRepo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
    }

    // GET all my watchlist items
    @GetMapping
    public List<WatchlistItemResponse> all(Authentication auth) {
        String email = auth.getName();
        return watchlistRepo.findAllByUser_Email(email).stream()
                .map(this::toResponse)
                .toList();
    }

    // GET my items by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> byCategory(@PathVariable Long categoryId, Authentication auth) {
        String email = auth.getName();

        // Ensure category belongs to this user
        var cat = categoryRepo.findByIdAndUser_Email(categoryId, email).orElse(null);
        if (cat == null) return ResponseEntity.status(404).body("Category not found");

        return ResponseEntity.ok(
                watchlistRepo.findAllByUser_EmailAndCategory_Id(email, categoryId).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // POST add movie to category
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody AddWatchlistRequest req, Authentication auth) {
        String email = auth.getName();

        // category must belong to user
        Category category = categoryRepo.findByIdAndUser_Email(req.categoryId, email).orElse(null);
        if (category == null) return ResponseEntity.status(404).body("Category not found");

        Movie movie = movieRepo.findById(req.movieId).orElse(null);
        if (movie == null) return ResponseEntity.status(404).body("Movie not found");

        if (watchlistRepo.existsByUser_EmailAndCategory_IdAndMovie_Id(email, req.categoryId, req.movieId)) {
            return ResponseEntity.badRequest().body("Already in this category");
        }

        User user = userRepo.findByEmail(email).orElseThrow();

        WatchlistItem item = new WatchlistItem();
        item.setUser(user);
        item.setCategory(category);
        item.setMovie(movie);

        WatchlistItem saved = watchlistRepo.save(item);
        return ResponseEntity.ok(toResponse(saved));
    }

    // DELETE remove by item id (only if mine)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> delete(@PathVariable Long itemId, Authentication auth) {
        String email = auth.getName();

        WatchlistItem item = watchlistRepo.findByIdAndUser_Email(itemId, email).orElse(null);
        if (item == null) return ResponseEntity.status(404).body("Watchlist item not found");

        watchlistRepo.delete(item);
        return ResponseEntity.noContent().build();
    }

    private WatchlistItemResponse toResponse(WatchlistItem w) {
        return new WatchlistItemResponse(
                w.getId(),
                w.getCategory().getId(), w.getCategory().getName(),
                w.getMovie().getId(), w.getMovie().getTitle(), w.getMovie().getDirector(),
                w.getMovie().getYear(), w.getMovie().getPosterUrl()
        );
    }
}
