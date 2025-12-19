package movieapp.controller;

import jakarta.validation.Valid;
import movieapp.dto.CategoryRequest;
import movieapp.dto.CategoryResponse;
import movieapp.dto.WatchlistDtos.WatchlistItemResponse;
import movieapp.entity.Category;
import movieapp.entity.User;
import movieapp.repository.CategoryRepository;
import movieapp.repository.UserRepository;
import movieapp.repository.WatchlistItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/collections", "/api/categories"})
public class CategoryController {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final WatchlistItemRepository watchlistRepo;

    public CategoryController(CategoryRepository categoryRepo,
                              UserRepository userRepo,
                              WatchlistItemRepository watchlistRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.watchlistRepo = watchlistRepo;
    }

    // ✅ Public collections (no login)
    @GetMapping("/public")
    public List<CategoryResponse> publicCollections() {
        return categoryRepo.findByIsPublicTrueOrderByIdDesc()
                .stream()
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getDescription(),
                        c.isPublic(),
                        c.getUser().getId()
                ))
                .toList();
    }

    // ✅ My collections (login)
    @GetMapping("/me")
    public List<CategoryResponse> myCollections(Authentication auth) {
        User me = userRepo.findByEmail(auth.getName()).orElseThrow();
        return categoryRepo.findByUser_IdOrderByIdDesc(me.getId())
                .stream()
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getDescription(),
                        c.isPublic(),
                        c.getUser().getId()
                ))
                .toList();
    }

    // ✅ Create collection (login)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest req, Authentication auth) {
        User me = userRepo.findByEmail(auth.getName()).orElseThrow();

        Category c = new Category();
        c.setName(req.getName());
        c.setDescription(req.getDescription());
        c.setPublic(Boolean.TRUE.equals(req.getIsPublic()));
        c.setUser(me);

        Category saved = categoryRepo.save(c);
        return ResponseEntity.ok(new CategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.isPublic(),
                me.getId()
        ));
    }

    // ✅ Update collection (only owner)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CategoryRequest req,
                                    Authentication auth) {
        User me = userRepo.findByEmail(auth.getName()).orElseThrow();
        Category c = categoryRepo.findById(id).orElseThrow();

        if (!c.getUser().getId().equals(me.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        c.setName(req.getName());
        c.setDescription(req.getDescription());
        c.setPublic(Boolean.TRUE.equals(req.getIsPublic()));

        Category saved = categoryRepo.save(c);
        return ResponseEntity.ok(new CategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.isPublic(),
                me.getId()
        ));
    }

    // ✅ Delete collection (only owner)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        User me = userRepo.findByEmail(auth.getName()).orElseThrow();
        Category c = categoryRepo.findById(id).orElseThrow();

        if (!c.getUser().getId().equals(me.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        categoryRepo.delete(c);
        return ResponseEntity.noContent().build();
    }

    // ✅ Public collection items (no login)
    @GetMapping("/{id}/items")
    public ResponseEntity<?> publicCollectionItems(@PathVariable Long id) {
        Category c = categoryRepo.findById(id).orElse(null);
        if (c == null) return ResponseEntity.status(404).body("Collection not found");
        if (!c.isPublic()) return ResponseEntity.status(403).body("This collection is private");

        var items = watchlistRepo.findAllPublicByCategoryIdWithDetails(id)
                .stream()
                .map(w -> new WatchlistItemResponse(
                        w.getId(),
                        w.getCategory().getId(),
                        w.getCategory().getName(),
                        w.getMovie().getId(),
                        w.getMovie().getTitle(),
                        w.getMovie().getDirector(),
                        w.getMovie().getYear(),
                        w.getMovie().getPosterUrl()
                ))
                .toList();

        return ResponseEntity.ok(items);
    }
}
